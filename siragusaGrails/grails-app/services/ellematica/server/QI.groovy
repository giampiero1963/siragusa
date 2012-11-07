package ellematica.server

import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.chart.XChartDocument;
import com.sun.star.chart.XDiagram;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.container.XNameReplace;
import com.sun.star.container.XNamed;
import com.sun.star.document.XEmbeddedObjectSupplier;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XLayerManager;
import com.sun.star.drawing.XLayerSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.drawing.XShapes;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XTableChart;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime
import com.sun.star.util.XCloseable;
import com.sun.star.util.XNumberFormatTypes;
import com.sun.star.util.XNumberFormatsSupplier;
import com.sun.star.view.XPrintable;
import com.sun.star.view.XSelectionSupplier;

import static org.artofsolving.jodconverter.office.OfficeUtils.SERVICE_DESKTOP;
import static org.artofsolving.jodconverter.office.OfficeUtils.cast;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUnoProperties;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUrl;

/**
*
* @author  danny brewer
*/
public class QI {
	 private QI() { }
	 
	 // The following are syntax sugar for UnoRuntime.queryInterface().
	 
	 
	 
	 //--------------------------------------------------
	 //  Beans                   com.sun.star.beans.*
	 //--------------------------------------------------
	 
	 static public XPropertySet XPropertySet( Object obj ) {
			 return cast( XPropertySet.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Bridge                  com.sun.star.bridge.*
	 //--------------------------------------------------
	 
	 static public XUnoUrlResolver XUnoUrlResolver( Object obj ) {
			 return cast( XUnoUrlResolver.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Chart                   com.sun.star.chart.*
	 //--------------------------------------------------
	 
	 static public XChartDocument XChartDocument( Object obj ) {
			 return cast( XChartDocument.class, obj );
	 }
	 
	 static public XDiagram XDiagram( Object obj ) {
			 return cast( XDiagram.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Container               com.sun.star.container.*
	 //--------------------------------------------------
	 
	 static public XIndexAccess XIndexAccess( Object obj ) {
			 return cast( XIndexAccess.class, obj );
	 }
	 
	 static public XNameAccess XNameAccess( Object obj ) {
			 return cast( XNameAccess.class, obj );
	 }
	 
	 static public XNameContainer XNameContainer( Object obj ) {
			 return cast( XNameContainer.class, obj );
	 }
	 
	 static public XNameReplace XNameReplace( Object obj ) {
			 return cast( XNameReplace.class, obj );
	 }
	 
	 static public XNamed XNamed( Object obj ) {
			 return cast( XNamed.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Document                com.sun.star.document.*
	 //--------------------------------------------------
	 
	 static public XEmbeddedObjectSupplier XEmbeddedObjectSupplier( Object obj ) {
			 return cast( XEmbeddedObjectSupplier.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Drawing                 com.sun.star.drawing.*
	 //--------------------------------------------------
	 
	 static public XDrawPage XDrawPage( Object obj ) {
			 return cast( XDrawPage.class, obj );
	 }
	 
	 static public XDrawPageSupplier XDrawPageSupplier( Object obj ) {
			 return cast( XDrawPageSupplier.class, obj );
	 }
	 
	 static public XDrawPages XDrawPages( Object obj ) {
			 return cast( XDrawPages.class, obj );
	 }
	 
	 static public XDrawPagesSupplier XDrawPagesSupplier( Object obj ) {
			 return cast( XDrawPagesSupplier.class, obj );
	 }
	 
	 static public XLayerManager XLayerManager( Object obj ) {
			 return cast( XLayerManager.class, obj );
	 }
	 
	 static public XLayerSupplier XLayerSupplier( Object obj ) {
			 return cast( XLayerSupplier.class, obj );
	 }
	 
	 static public XShape XShape( Object obj ) {
			 return cast( XShape.class, obj );
	 }
	 
	 static public XShapes XShapes( Object obj ) {
			 return cast( XShapes.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Frame                   com.sun.star.frame.*
	 //--------------------------------------------------
	 
	 static public XComponentLoader XComponentLoader( Object obj ) {
			 return cast( XComponentLoader.class, obj );
	 }
	 
	 static public XDispatchHelper XDispatchHelper( Object obj ) {
			 return cast( XDispatchHelper.class, obj );
	 }
	 
	 static public XDispatchProvider XDispatchProvider( Object obj ) {
			 return cast( XDispatchProvider.class, obj );
	 }
	 
	 static public XModel XModel( Object obj ) {
			 return cast( XModel.class, obj );
	 }
	 
	 static public XStorable XStorable( Object obj ) {
			 return (XStorable) UnoRuntime.queryInterface( XStorable.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Lang                    com.sun.star.lang.*
	 //--------------------------------------------------
	 
	 static public XMultiComponentFactory XMultiComponentFactory( Object obj ) {
			 return (XMultiComponentFactory) UnoRuntime.queryInterface( XMultiComponentFactory.class, obj );
	 }
	 
	 static public XMultiServiceFactory XMultiServiceFactory( Object obj ) {
			 return (XMultiServiceFactory) UnoRuntime.queryInterface( XMultiServiceFactory.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Sheet                   com.sun.star.sheet.*
	 //--------------------------------------------------
	 
	 static public XCellRangeAddressable XCellRangeAddressable( Object obj ) {
			 return (XCellRangeAddressable) UnoRuntime.queryInterface( XCellRangeAddressable.class, obj );
	 }
	 
	 static public XSpreadsheet XSpreadsheet( Object obj ) {
			 return (XSpreadsheet) UnoRuntime.queryInterface( XSpreadsheet.class, obj );
	 }
	 
	 static public XSpreadsheetDocument XSpreadsheetDocument( Object obj ) {
			 return (XSpreadsheetDocument) UnoRuntime.queryInterface( XSpreadsheetDocument.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Table                   com.sun.star.table.*
	 //--------------------------------------------------
	 
	 static public XTableChart XTableChart( Object obj ) {
			 return (XTableChart) UnoRuntime.queryInterface( XTableChart.class, obj );
	 }
	 
	 static public XTableCharts XTableCharts( Object obj ) {
			 return (XTableCharts) UnoRuntime.queryInterface( XTableCharts.class, obj );
	 }
	 
	 static public XTableChartsSupplier XTableChartsSupplier( Object obj ) {
			 return (XTableChartsSupplier) UnoRuntime.queryInterface( XTableChartsSupplier.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Text                    com.sun.star.text.*
	 //--------------------------------------------------
	 
	 static public XText XText( Object obj ) {
			 return (XText) UnoRuntime.queryInterface( XText.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  Util                    com.sun.star.util.*
	 //--------------------------------------------------
	 
	 static public XCloseable XCloseable( Object obj ) {
			 return (XCloseable) UnoRuntime.queryInterface( XCloseable.class, obj );
	 }
	 
	 static public XNumberFormatsSupplier XNumberFormatsSupplier( Object obj ) {
			 return (XNumberFormatsSupplier) UnoRuntime.queryInterface( XNumberFormatsSupplier.class, obj );
	 }
	 
	 static public XNumberFormatTypes XNumberFormatTypes( Object obj ) {
			 return (XNumberFormatTypes) UnoRuntime.queryInterface( XNumberFormatTypes.class, obj );
	 }
	 
	 //--------------------------------------------------
	 //  View                    com.sun.star.view.*
	 //--------------------------------------------------
	 
	 static public XPrintable XPrintable( Object obj ) {
			 return (XPrintable) UnoRuntime.queryInterface( XPrintable.class, obj );
	 }
	 static public XSelectionSupplier XSelectionSupplier( Object obj ) {
			 return (XSelectionSupplier) UnoRuntime.queryInterface( XSelectionSupplier.class, obj );
	 }
	 
}





//----------------------------------------------------------------------
//  OpenOffice.org imports
//----------------------------------------------------------------------


