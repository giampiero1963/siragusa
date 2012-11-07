package ellematica.server


import com.sun.star.document.UpdateDocMode;
import java.util.logging.Logger
import org.springframework.stereotype.*



import org.artofsolving.jodconverter.OfficeDocumentConverter
import org.artofsolving.jodconverter.OfficeDocumentUtils;
import org.artofsolving.jodconverter.document.DocumentFamily;

import com.sun.star.lang.XComponent;


import static org.artofsolving.jodconverter.office.OfficeUtils.SERVICE_DESKTOP;
import static org.artofsolving.jodconverter.office.OfficeUtils.cast;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUnoProperties;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUrl;
;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory
import com.sun.star.lang.XMultiServiceFactory
import com.sun.star.sheet.XSheetCellRange
import com.sun.star.sheet.XSpreadsheet
import com.sun.star.sheet.XSpreadsheetDocument
import com.sun.star.table.XCell
import com.sun.star.task.ErrorCodeIOException;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess
import ellematica.graphics.DocumentValues
import ellematica.graphics.ValoreConValuta



import org.artofsolving.jodconverter.office.*
import org.artofsolving.jodconverter.util.PlatformUtils


import org.springframework.stereotype.Component

@Component
class OfficeService extends Observable {
	OfficeManager officeManager
	OfficeDocumentConverter converter
	ReportTask reportTask

	def openofficeHome=PlatformUtils.windows?System.env.OFFICE_HOME:"/Applications/OpenOffice.org.app/Contents"	
	def loadProperties=[Hidden:true,ReadOnly:false,UpdateDocMode: UpdateDocMode.QUIET_UPDATE]
	


	public OfficeService(){
		officeManager = new DefaultOfficeManagerConfiguration()
		.setOfficeHome(openofficeHome)
		.setConnectionProtocol(OfficeConnectionProtocol.SOCKET)
		//.setPipeNames("${home.path}/office1", "${home.path}/office2")
		.setTaskExecutionTimeout(30000L)
		.buildOfficeManager();
		officeManager.start();
		converter = new OfficeDocumentConverter(officeManager);
		
	}
	
	
	def locale( context){
		XMultiComponentFactory xMultiComponentFactory = context.getServiceManager();
		Object oProvider =
			 xMultiComponentFactory.createInstanceWithContext("com.sun.star.configuration.ConfigurationProvider", context);
		XMultiServiceFactory xConfigurationServiceFactory =cast(XMultiServiceFactory.class, oProvider);

		PropertyValue[] lArgs    = new PropertyValue[1];
		lArgs[0] = new PropertyValue();
		lArgs[0].Name  = "nodepath";
		lArgs[0].Value = "/org.openoffice.Setup/L10N";
		 
		Object configAccess =  xConfigurationServiceFactory.createInstanceWithArguments(
			 "com.sun.star.configuration.ConfigurationAccess",lArgs);

		XNameAccess xNameAccess = cast(XNameAccess.class, configAccess);
		 
		System.out.println(xNameAccess.getByName("ooLocale"));
	}
	
	def convert(src,dest){
		converter.convert(new File(src), new File(dest))
	}
	private  loadDocument(OfficeContext context, File inputFile){
		if (!inputFile.exists()) {
			throw new OfficeException("input document not found");
		}
		def loader = cast(XComponentLoader.class, context.getService(SERVICE_DESKTOP));


		def document = loader.loadComponentFromURL(toUrl(inputFile), "_blank", 0, toUnoProperties(loadProperties));

		if (document == null) {
			throw new OfficeException("could not load document: "  + inputFile.name);
		}
		return document;
	}





	def  fields=[
		[name:"dataordine",col:"C12",tipo:"d"],
		[name:"numero_doc",col:"C13",tipo:"f"],
		[name:"numord",col:"C14",tipo:"f"],
		[name:"data_imbarco",col:"C15",tipo:"d"],
		[name:"importo",col:"E13",tipo:"v"]
		]

	def getValues(XSpreadsheetDocument document,File file){
		def sheets=document.sheets
		XIndexAccess sheetIA = cast(XIndexAccess.class,sheets)
		XSpreadsheet sheet = cast(XSpreadsheet.class,sheetIA.getByIndex(0))
		
		def values=new DocumentValues()
		fields.each {
			def range = sheet.getCellRangeByName(it.col)
			XCell cell = range.getCellByPosition(0,0)
			values[it.name]=getField(cell,it.tipo,document);
		}
		
		values.filename=file.name
		setChanged()
		notifyObservers(values)
		
	}
	
	def getCurrency(prop,doc){
		def xNumberFormatsSupplier = cast(com.sun.star.util.XNumberFormatsSupplier.class, doc )

		// Get the number formats from the supplier
		try{
			def xNumberFormats = xNumberFormatsSupplier.getNumberFormats()
			def nf=xNumberFormats.getByKey(prop.Handle)
			def s=nf.getPropertyValue("FormatString");
			return ((s =~ /.*Û.*#/) || (s =~ /.*EUR.*#/))? "eur":"usd"
		}catch(e){
			println("numberFormat")
			return "usd"
		}
		
	}
	
	def months= ["GEN","FEB","MAR","APR","MAG","GIU","LUG","AGO","SET","OTT","NOV","DIC"]
	
	def getValuta(cell,doc){
		com.sun.star.beans.XPropertySet xPropSet = cast(com.sun.star.beans.XPropertySet.class, cell);
		def prop=xPropSet.propertySetInfo.getPropertyByName("NumberFormat");
		getCurrency(prop,doc)
	}
	
	def getField(cell,tipo,doc){
		if(tipo=="v"){
			def val= cell.getValue().toBigDecimal().setScale(2,BigDecimal.ROUND_HALF_UP)
			return new ValoreConValuta(valuta:getValuta(cell,doc),valore:val.toDouble())
		}
		def f=cell.getFormula()
		if(!f) return null
		if(tipo=="f")
			return f
		if(tipo== "d"){
			return getDateField(cell)
		}
		return f		
	}
	
	def dateFormats=["dd/MM/yyyy","dd-MM-yyyy","dd MMM yyyy","MMM dd yyyy", "MM dd yyyy","yyyy dd MM"]
	
	//Logger log
	
	def getDateFromString(value){
		if(! value.trim())  return null;
		
		def v= value[0]=="'"?value[1..-1]:value
		for(format in dateFormats){
			try{
				return Date.parse(format,v)
			}catch(Exception e){
				println("Date <$value> not in '$format' format ")
			}
		}
		try{
			def dd=value[0..1]
			def mm=value[3..5]
			def aa=value[7..10]
			def idx=months.find { it.equalsIgnoreCase(mm) }
			if(idx==-1) return null
			Calendar c=Calendar.newInstance();
			c.set(aa,mm+1,dd)
			return c.time		
		}catch(Exception e){
			log.error("Date <$value> unknown format ")
		}	
				
		null	
	
		
	}
	
	def getDateField(cell){
		def currentValue = String.valueOf(cell.value);
		if(currentValue=="0.0") return getDateFromString(cell.formula)
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(1899,11,30);
		calendar.add(java.util.Calendar.DATE, Math.round(Float.parseFloat(currentValue)));
		calendar.getTime();
	}

	public static void main(String []arg){
		def o=new OfficeService()
		o.convert("/Users/giampiero/siragusa/upload/SH_ITS_SEM.xls","/Users/giampiero/siragusa/tmp/SH_ITS_SEM.xls")
	}
	
	def startExcelValuesTask(String inputFile){
		def ext=inputFile.substring(inputFile.lastIndexOf('.')+1)
		if(! (ext  in ["xls","xlsx","XLS","XLSX"])) return
		officeManager.execute(getExcelValuesTask(inputFile))

	}
	
	def report(inputFile,outputFile,data){
		def task=new ExcelTask(
			modifyDocument:reportTask.report,
			inputFile:inputFile,
			outputFile:outputFile,
			loadProperties:loadProperties,
			data:data
			)
		officeManager.execute(task)
	}
	
	
	def statistics(inputFile,outputFile,data){
		def task=new ExcelTask(
			modifyDocument:reportTask.statistics,
			inputFile:inputFile,
			outputFile:outputFile,
			loadProperties:loadProperties,
			data:data
			)
		officeManager.execute(task)
	}
	
	def testFile(String inputFile){
		new ExcelTask(inputFile)
	}
	
	def testTask(String inputFile){
		new OfficeTask(){
			void execute(OfficeContext context) throws OfficeException{
				locale(context)	
			}
		}
	}
	def getExcelValuesTask(String inputFile){
		new OfficeTask(){
			void execute(OfficeContext context) throws OfficeException{
				XComponent document = null;
				try {
					def file= new File(inputFile)
					document = loadDocument(context,file);
					XSpreadsheetDocument doc=cast(XSpreadsheetDocument.class, document);
					getValues(doc,file)
				} finally {
					if (document != null) {
						XCloseable closeable = cast(XCloseable.class, document);
						if (closeable != null) {
							closeable.close(true);
						} else {
							document.dispose();
						}
					}
				}
			}
		}
	}
}
