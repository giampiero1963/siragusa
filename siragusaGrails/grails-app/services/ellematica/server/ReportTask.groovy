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
import com.sun.star.bridge.*;

import com.sun.star.beans.*;
import com.sun.star.chart.*;
import com.sun.star.container.*;
import com.sun.star.document.*;
import com.sun.star.drawing.*;
import com.sun.star.frame.*;
import com.sun.star.lang.*;
import com.sun.star.sheet.*;
import com.sun.star.table.*;
import com.sun.star.text.*;
import com.sun.star.uno.*;
import com.sun.star.util.*;
import com.sun.star.view.*;

import com.sun.star.comp.servicemanager.*;
import com.sun.star.bridge.*;


import com.sun.star.style.*;
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
import com.sun.star.sheet.XSheetCellRange
import com.sun.star.sheet.XSpreadsheet
import com.sun.star.sheet.XSpreadsheetDocument
import com.sun.star.table.XCell
import com.sun.star.task.ErrorCodeIOException;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.container.XIndexAccess;
import ellematica.graphics.DocumentValues
import ellematica.graphics.ValoreConValuta


import org.artofsolving.jodconverter.office.*
import org.springframework.stereotype.Component

@Component
class ReportTask {
	
	def sheetInfo=[
		 journal:[
			name: "Journal",
			euro: "cambio",
			startFrom:[4,1] as int[],
			propsOrder:infoJournal(),
			dateType:[date:true,shipment:true]
				
			]
		 
		 ,hk:[
			 name: "HK",
			 startFrom:[4,1] as int[],
			 propsOrder:[
				 "creation",
				 "customer",
				 "supplier",
				 "value"
				 ]
			 ]
		 ,dz:[
			 name: "DZ",
			 startFrom:[4,1] as int[],
			 propsOrder:[
				 "creation",
				 "customer",
				 "supplier",
				 "value"
				 ]
			 ]
	]
	
	
	def names=["journal","hk","dz"]

def infoJournal(){
	[
		"type",
		"doc",
		"date",
		"customer",
		"supplier",
		"shipment",
		"valuta",
		"deposit",
		"importo",
		"hk",
		"dz",
//		"shipped",
//		"payed",
//		"payedHK",
//		"payedDZ",
		"note"
		]
	}
	
	def formula(row,col){
		def ch=('A'..'Z')[col]
		def r=row+1
		"=${ch}${r}"
	}

	def setCol(sheet,euro,r,c,row,cdata){
		def mult= (row.type=="CN")? -1:1
		switch(cdata){
			case String:
				OOoCalcUtils.setCellString( sheet, c as int, r as int, cdata );
				break
			case Double:
				
					OOoCalcUtils.setCellValue( sheet, c as int, r as int, cdata * mult);
				
				break
			case java.util.Date:
					assignDate(sheet,c,r,cdata)
				break
			
			case Boolean:
				OOoCalcUtils.setCellString( sheet, c as int, r as int, cdata?"Y":"N" );
				break
		}
		return 0
	}
	
	
	def setColStat(sheet,euro,r,c,cdata){
	
		OOoCalcUtils.setCellFormula( sheet, c as int, r as int, cdata );									
		0
	}
	
	
	
	def report={par,data->
		
		
		def xcomponent=par.document
		def numberFormatsSupplier=par.numberFormatsSupplier
		def sheets=par.sheets

	
		
		names.each { name->
			def info=sheetInfo[name]
			if(! info) return
			
			Object sheetObj = sheets.getByName( info.name ); // By name

			XSpreadsheet sheet = cast(XSpreadsheet.class, sheetObj );
			int sr = info.startFrom[0]
			int sc = info.startFrom[1] 
			def euro=info.euro
			def theData=data[name]
			def dateType=info.dateType
			assignDateMyName(sheet,"data_report",new java.util.Date())
			theData.eachWithIndex {tmp,crow ->
				def row=theData[crow]
				def r=sr+crow
				def offset=0
				
				info.propsOrder.eachWithIndex{tt, ccol ->
					def prop=info.propsOrder[ccol]
					def cdata=row[prop]
					def c=sc+ccol+offset
					offset+=setCol(sheet,euro,r,c,row,cdata) 			
				}
				
			}
		}
			
		
				 
	}

	def assignDate(sheet,int x,int y,java.util.Date date){
		def cal = Calendar.instance
		cal.time=date
		int anno=cal[Calendar.YEAR]
		int mese=cal[Calendar.MONTH]+1
		int giorno=cal[Calendar.DAY_OF_MONTH]
		OOoCalcUtils.setCellFormula( sheet, x as int, y as int, "=DATE(${anno};${mese};${giorno})" );
	}
	
	def assignDateMyName(sheet,String rif, java.util.Date date){
		def cal = Calendar.instance
		cal.time=date
		int anno=cal[Calendar.YEAR]
		int mese=cal[Calendar.MONTH]+1
		int giorno=cal[Calendar.DAY_OF_MONTH]
		try{
			OOoCalcUtils.setCellFormula( sheet, rif,"=DATE(${anno};${mese};${giorno})" );
		}catch(e){
		
		}
	}
	
	def statProps=[
		"importo","importoHK","importoDZ"
		]
	
	def statistics={par,data->
		Object sheetObj = par.sheets.getByName( "Summary" ); // By name

		XSpreadsheet sheet = cast(XSpreadsheet.class, sheetObj );
		int sr = 4
		int sc = 1
		def euro="cambio"
		assignDateMyName(sheet,"data_report",new java.util.Date())
		data.complessivi.eachWithIndex {row,crow ->
			
			
				def r=sr+crow
				def offset=0
				
				setCol(sheet,euro,r,sc,row,row.date)
				offset++
				statProps.eachWithIndex{prop,ccol->
					def c=ccol+sc+offset
					int iusd=(row.dati.USD[prop]*100);
					int ieur=(row.dati.EUR[prop]*100)
					def val="=(${iusd}/${euro}+${ieur})/100"
					offset+=setColStat(sheet,euro,r,c,val)
				}
				
			
		}
	}
	
	
		

	def getNumberFormat(document,cell){
		XPropertySet cellProp = cast(XPropertySet.class, cell);
		XNumberFormatsSupplier xNumberFormatsSupplier = cast(XNumberFormatsSupplier.class, document);
		// Get the number formats from the supplier
		XNumberFormats xNumberFormats = xNumberFormatsSupplier.getNumberFormats();
		
		xNumberFormats.getByKey(0)
		// Get the default locale
		Locale defaultLocale = new Locale();
	}	
	
}




/*
 * 
 * 
 * //
		
		OOoCalcUtils.setCellString( sheet, 1, 0, "Sales" );
		OOoCalcUtils.setCellString( sheet, 2, 0, "End Date" );

		OOoCalcUtils.setCellString( sheet, 0, 1, "Jan" );
		OOoCalcUtils.setCellString( sheet, 0, 2, "Feb" );
		OOoCalcUtils.setCellString( sheet, 0, 3, "Mar" );
		OOoCalcUtils.setCellString( sheet, 0, 4, "Apr" );
		OOoCalcUtils.setCellString( sheet, 0, 5, "May" );
		OOoCalcUtils.setCellString( sheet, 0, 6, "Jun" );
		OOoCalcUtils.setCellString( sheet, "A8", "Jul" );
		OOoCalcUtils.setCellString( sheet, "A9", "Aug" );
		OOoCalcUtils.setCellString( sheet, "A10", "Sep" );
		OOoCalcUtils.setCellString( sheet, "A11", "Oct" );
		OOoCalcUtils.setCellString( sheet, "A12", "Nov" );
		OOoCalcUtils.setCellString( sheet, "A13", "Dec" );

		OOoCalcUtils.setCellValue( sheet, 1, 1, 3826.37 );
		OOoCalcUtils.setCellValue( sheet, 1, 2, 3504.21 );
		OOoCalcUtils.setCellValue( sheet, 1, 3, 2961.45 );
		OOoCalcUtils.setCellValue( sheet, 1, 4, 2504.12 );
		OOoCalcUtils.setCellValue( sheet, 1, 5, 2713.98 );
		OOoCalcUtils.setCellValue( sheet, 1, 6, 2248.17 );
		OOoCalcUtils.setCellValue( sheet, "B8", 1802.13 );
		OOoCalcUtils.setCellValue( sheet, "B9", 2003.22 );
		OOoCalcUtils.setCellValue( sheet, "B10", 1502.54 );
		OOoCalcUtils.setCellValue( sheet, "B11", 1207.68 );
		OOoCalcUtils.setCellValue( sheet, "B12", 1319.71 );
		OOoCalcUtils.setCellValue( sheet, "B13", 786.03 );

		OOoCalcUtils.setCellFormula( sheet, 2, 1, "=DATE(2004;01;31)" );
		OOoCalcUtils.setCellFormula( sheet, 2, 2, "=DATE(2004;02;29)" );
		OOoCalcUtils.setCellFormula( sheet, 2, 3, "=DATE(2004;03;31)" );
		OOoCalcUtils.setCellFormula( sheet, 2, 4, "=DATE(2004;04;30)" );
		OOoCalcUtils.setCellFormula( sheet, 2, 5, "=DATE(2004;05;31)" );
		OOoCalcUtils.setCellFormula( sheet, 2, 6, "=DATE(2004;06;30)" );
		OOoCalcUtils.setCellFormula( sheet, "C8", "=DATE(2004;07;31)" );
		OOoCalcUtils.setCellFormula( sheet, "C9", "=DATE(2004;08;31)" );
		OOoCalcUtils.setCellFormula( sheet, "C10", "=DATE(2004;09;30)" );
		// Note that these last three dates are not set as DATE() function calls.
		OOoCalcUtils.setCellFormula( sheet, "C11", "10/31/2004" );
		OOoCalcUtils.setCellFormula( sheet, "C12", "11/30/2004" );
		OOoCalcUtils.setCellFormula( sheet, "C13", "12/31/2004" );
		//
		//----------
		
		//----------
		//  Format the date cells as dates.
		//
		
		XNumberFormats numberFormats_XNumberFormats = calcDoc_XNumberFormatsSupplier.getNumberFormats();
		XNumberFormatTypes numberFormats_XNumberFormatTypes = QI.XNumberFormatTypes( numberFormats_XNumberFormats );
		// At this point, the variables
		//   numberFormats_XNumberFormats
		//   numberFormats_XNumberFormatTypes
		//  point to the same service.  Just different interfaces of it.
		
		Locale locale = new Locale();
		int nDateKey = numberFormats_XNumberFormatTypes.getStandardFormat( com.sun.star.util.NumberFormat.DATE, locale );
		
		XCellRange cellRange_XCellRange = sheet.getCellRangeByName( "C2:C13" );
		OOoUtils.setIntProperty( cellRange_XCellRange, "NumberFormat", nDateKey );
		//
		//----------

		//----------
		//  Now add a chart to the spreadsheet.
		//
		cellRange_XCellRange = sheet.getCellRangeByName( "A1:B13" );
		XCellRangeAddressable cellRange_XCellRangeAddressable = QI.XCellRangeAddressable( cellRange_XCellRange );
		// At this point, the variables
		//   cellRange_XCellRange
		//   cellRange_XCellRangeAddressable
		//  point to the same service.  Just different interfaces of it.
		
		CellRangeAddress cellRangeAddress = cellRange_XCellRangeAddressable.getRangeAddress();
		
		XTableChartsSupplier sheet_XTableChartsSupplier = QI.XTableChartsSupplier( sheet );
		// At this point, the variables
		//   sheet
		//   sheet_XTableChartsSupplier
		//  point to the same service.  Just different interfaces of it.
		
		// Get the collection of charts from the sheet.
		XTableCharts charts = sheet_XTableChartsSupplier.getCharts();
		
		// Add a new chart with a specific name,
		//  in a specific rectangle on the drawing page,
		//  and connected to specific cells of the spreadsheet.
		CellRangeAddress[] addresses= [cellRangeAddress]
		charts.addNewByName( "Sales",
										OOoUtils.makeRectangle( 8000, 1000, 16000, 10000 ),
										addresses,
										true, true );
		
		// From the collection of charts, get the new chart we just created.
		Object chart = charts.getByName( "Sales" );
		XTableChart chart_XTableChart = QI.XTableChart( chart );
		XEmbeddedObjectSupplier chart_XEmbeddedObjectSupplier = QI.XEmbeddedObjectSupplier( chart );
		// At this point, the variables
		//   chart
		//   chart_XTableChart
		//   chart_XEmbeddedObjectSupplier
		//  point to the same service.  Just different interfaces of it.
		
		// Get the chart document model.
		Object chartDoc = chart_XEmbeddedObjectSupplier.getEmbeddedObject();
		XChartDocument chartDoc_XChartDocument = QI.XChartDocument( chartDoc );
		XMultiServiceFactory chartDoc_XMultiServiceFactory = QI.XMultiServiceFactory( chartDoc );
		// At this point, the variables
		//   chartDoc
		//   chartDoc_XChartDocument
		//   chartDoc_XMultiServiceFactory
		//  point to the same service.  Just different interfaces of it.
		
		// Get the drawing text shape of the title of the chart.
		XShape titleTextShape = chartDoc_XChartDocument.getTitle();
		// Change the title.
		OOoUtils.setStringProperty( titleTextShape, "String", "Sales Chart" );
		
		// Create a diagram.
		Object diagram = chartDoc_XMultiServiceFactory.createInstance( "com.sun.star.chart.BarDiagram" );
		XDiagram diagram_XDiagram = QI.XDiagram( diagram );
		// At this point, the variables
		//   diagram
		//   diagram_XDiagram
		//  point to the same service.  Just different interfaces of it.
		
		// Set its parameters.
		OOoUtils.setBooleanProperty( diagram, "Vertical", true );
		
		// Make the chart use this diagram.
		chartDoc_XChartDocument.setDiagram( diagram_XDiagram );
		
		// Ask the chart what diagram it is using.
		// (Unnecessary, since variable oDiagram already contains this value -- see previous statement.)
		diagram_XDiagram = chartDoc_XChartDocument.getDiagram();
		
		// Make more changes to the diagram.
		OOoUtils.setIntProperty( diagram, "DataCaption", com.sun.star.chart.ChartDataCaption.VALUE );
		OOoUtils.setIntProperty( diagram, "DataRowSource", 1 );
		//
		//----------

		//----------
		//  Now demonstrate how to manipulate the sheets.
		//
		// Insert six more sheets into the document.
		int numSheetsCurrently = sheets_XIndexAccess.getCount();
		sheets_XSpreadsheets.insertNewByName( "Fred", (short)(numSheetsCurrently+1) );
		sheets_XSpreadsheets.insertNewByName( "Joe", (short)(numSheetsCurrently+2) );
		sheets_XSpreadsheets.insertNewByName( "Bill", (short)(numSheetsCurrently+3) );
		sheets_XSpreadsheets.insertNewByName( "Sam", (short)(numSheetsCurrently+4) );
		sheets_XSpreadsheets.insertNewByName( "Tom", (short)(numSheetsCurrently+5) );
		sheets_XSpreadsheets.insertNewByName( "David", (short)(numSheetsCurrently+6) );
		
		// Now find a sheet named "Sheet2" and get rid of it.
		//sheets_XSpreadsheets.removeByName( "Sheet2" );
		// Now find the sheet named "Sam" and change its name to "Sheet 37"
		//OOoUtils.XNamed_setName( sheets_XSpreadsheets.getByName( "Sam" ), "Sheet 37" );
		
		//
		//----------
		
		//----------
		//  Examples of getting or setting the selection.
		
		// From the controller, get a different interface of the controller.
		XSelectionSupplier calcDocController_XSelectionSupplier = QI.XSelectionSupplier( calcDocController );
		// At this point, the variables
		//   calcDocController
		//   calcDocController_XSelectionSupplier
		//  point to the same service.  Just different interfaces of it.
		
		//  Select some cells in the spreadsheet.
		
		// Select a certian cell by position.
		XCell oneCell = sheet.getCellByPosition( 0, 4 );  // Cell A5
		calcDocController_XSelectionSupplier.select( oneCell );
		
		// Select some cells by position.
		XCellRange oneOrMoreCells = sheet.getCellRangeByPosition( 0, 4, 1, 6 ); // Cells A5:B7
		calcDocController_XSelectionSupplier.select( oneOrMoreCells );
		
		// Select a single cell by name.
		oneOrMoreCells = sheet.getCellRangeByName( "A8" );  // a group of one cell
		oneCell = oneOrMoreCells.getCellByPosition( 0, 0 ); // get the top left cell of the group
		calcDocController_XSelectionSupplier.select( oneCell );
//            calcDocController_XSelectionSupplier.select( oneOrMoreCells ); // we could have done this instead
		
		// Select a range of cells by name.
		oneOrMoreCells = sheet.getCellRangeByName( "A8:C13" );
		calcDocController_XSelectionSupplier.select( oneOrMoreCells );

		// Let's find out what is selected.
		Object selection = calcDocController_XSelectionSupplier.getSelection();
		if( selection == null ) {
				System.out.println( "Nothing is selected." );
		} else if( QI.XCellRangeAddressable( selection ) != null ) {
				CellRangeAddress cellsAddress = QI.XCellRangeAddressable( selection ).getRangeAddress();
				System.out.println( "A range of cells are selected.  sheet=" + cellsAddress.Sheet
													 + ", col1=" + cellsAddress.StartColumn + ", row1=" + cellsAddress.StartRow
																+ ", col2=" + cellsAddress.EndColumn   + ", row2=" + cellsAddress.EndRow );
		} else if( QI.XShapes( selection ) != null ) {
				System.out.println( "One or more drawing shapes selected." );
		} else {
				System.out.println( "Something unknown is selected." );
		}
 							 
 */
