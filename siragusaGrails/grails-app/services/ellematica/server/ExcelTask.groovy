package ellematica.server
import java.io.File;

import org.artofsolving.jodconverter.office.OfficeContext;
import org.artofsolving.jodconverter.office.OfficeException
import org.artofsolving.jodconverter.office.OfficeTask

import com.sun.star.lang.XComponent;




//---------------------------------------------------------------------- 
//  OpenOffice.org imports 
//---------------------------------------------------------------------- 

import com.sun.star.beans.*;
import com.sun.star.bridge.*;
import com.sun.star.chart.*;
import com.sun.star.container.*;
import com.sun.star.document.*;
import com.sun.star.drawing.*;
import com.sun.star.frame.*;
import com.sun.star.lang.*;
import com.sun.star.sheet.*;
import com.sun.star.table.*;
import com.sun.star.text.*;
import com.sun.star.util.*;
import com.sun.star.view.*;

import com.sun.star.comp.servicemanager.*;

import com.sun.star.style.*;

import static org.artofsolving.jodconverter.office.OfficeUtils.SERVICE_DESKTOP;
import static org.artofsolving.jodconverter.office.OfficeUtils.cast;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUnoProperties;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUrl;
import ellematica.graphics.DocumentValues
import ellematica.graphics.ValoreConValuta



import org.artofsolving.jodconverter.office.*


/** 
 * 
 * @author  Danny Brewer 
 */ 
public class ExcelTask implements OfficeTask{ 
    String inputFile
		String outputFile
		def loadProperties
		def storeProperties=[FilterName: "MS Excel 97"]
		def data

		public  loadDocument(OfficeContext context, File inputFile){
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
	
		public void storeDocument(XComponent document, File outputFile)  {
			
			cast(XStorable.class, document).storeToURL(toUrl(outputFile), toUnoProperties(storeProperties));
		}
		
    
		void execute(OfficeContext context) throws OfficeException{
			XComponent document     
			XSpreadsheetDocument    spreadsheetDoc
			XNumberFormatsSupplier  numberFormatsSupplier
			XModel                  modelDoc
			XController 						calcDocController
			XFrame                  calcDocFrame
			XSpreadsheets 					sheets
			try {
				def file= new File(inputFile)
				document = loadDocument(context,file);
				spreadsheetDoc   = cast(XSpreadsheetDocument.class, document );
				numberFormatsSupplier  = cast(XNumberFormatsSupplier.class, document );
				modelDoc        = cast(XModel.class, spreadsheetDoc );
				calcDocController = modelDoc.currentController
				// Get the frame from the controller.
				calcDocFrame = calcDocController.frame;
				sheets = spreadsheetDoc.sheets;
				def par=[document:document,numberFormatsSupplier:numberFormatsSupplier,sheets:sheets]
				
				if(modifyDocument) modifyDocument(par,data)
				storeDocument(document,new File(outputFile))				
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
		
    def modifyDocument
  
} 

