package ellematica.server

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.text.XText

public class OOoCalcUtils {
	 private OOoCalcUtils() { }
		 
	 
	 
	 //----------------------------------------------------------------------
	 //  Sugar coated access to a cell or cells.
	 //----------------------------------------------------------------------

	 /**
		* Get a single cell by its position.
		*/
	 public static XCell getCellByPosition( Object spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellByPosition( spreadsheet_XSpreadsheet, col, row );
	 }
	 /**
		* Get a single cell by its position.
		*/
	 public static XCell getCellByPosition( XSpreadsheet spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 return oneCell;
	 }
	 
	 /**
		* Get a single cell by its name.
		* You should pass in a single cell name such as "A1".
		* If you pass in a range, such as "B15:E37", a single cell will be returned,
		*  which is the top left cell in the range, such as B15.
		*/
	 public static XCell getCellByName( Object spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellByName( spreadsheet_XSpreadsheet, cellName );
	 }
	 /**
		* Get a single cell by its name.
		* You should pass in a single cell name such as "A1".
		* If you pass in a range, such as "B15:E37", a single cell will be returned,
		*  which is the top left cell in the range, such as B15.
		*/
	 public static XCell getCellByName( XSpreadsheet spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 return oneCell;
	 }

	 /**
		* Get range of cells by their position.
		*/
	 public static XCellRange getCellRangeByPosition( Object spreadsheet, int col1, int row1, int col2, int row2 )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellRangeByPosition( spreadsheet_XSpreadsheet, col1, row1, col2, row2 );
	 }
	 /**
		* Get range of cells by their position.
		*/
	 public static XCellRange getCellRangeByPosition( XSpreadsheet spreadsheet, int col1, int row1, int col2, int row2 )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByPosition( col1, row1, col2, row2 );
			 return oneOrMoreCells;
	 }
	 
	 /**
		* Get range of cells by their name.
		* You should pass in a single cell range name such as "A1" or "B15:E37", or "MyCells".
		*/
	 public static XCellRange getCellRangeByName( Object spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellRangeByName( spreadsheet_XSpreadsheet, cellName );
	 }
	 /**
		* Get range of cells by their name.
		* You should pass in a single cell range name such as "A1" or "B15:E37", or "MyCells".
		*/
	 public static XCellRange getCellRangeByName( XSpreadsheet spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 return oneOrMoreCells;
	 }
	 
	 
	 //----------------------------------------------------------------------
	 //  Sugar coated way to access or manipulate a cell
	 //   Value, Formula or String.
	 //----------------------------------------------------------------------

	 public static double getCellValue( Object spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellValue( spreadsheet_XSpreadsheet, col, row );
	 }
	 public static double getCellValue( XSpreadsheet spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 return oneCell.getValue();
	 }
	 public static double getCellValue( Object spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellValue( spreadsheet_XSpreadsheet, cellName );
	 }
	 public static double getCellValue( XSpreadsheet spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 return oneCell.getValue();
	 }
	 public static void setCellValue( Object spreadsheet, int col, int row, double value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 setCellValue( spreadsheet_XSpreadsheet, col, row, value );
	 }
	 public static void setCellValue( XSpreadsheet spreadsheet, int col, int row, double value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 oneCell.setValue( value );
	 }
	 public static void setCellValue( Object spreadsheet, String cellName, double value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 setCellValue( spreadsheet_XSpreadsheet, cellName, value );
	 }
	 public static void setCellValue( XSpreadsheet spreadsheet, String cellName, double value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 oneCell.setValue( value );
	 }
	 
	 public static String getCellFormula( Object spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellFormula( spreadsheet_XSpreadsheet, col, row );
	 }
	 public static String getCellFormula( XSpreadsheet spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 return oneCell.getFormula();
	 }
	 public static String getCellFormula( Object spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellFormula( spreadsheet_XSpreadsheet, cellName );
	 }
	 public static String getCellFormula( XSpreadsheet spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 return oneCell.getFormula();
	 }
	 public static void setCellFormula( Object spreadsheet, int col, int row, String formula )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 setCellFormula( spreadsheet_XSpreadsheet, col, row, formula );
	 }
	 public static void setCellFormula( XSpreadsheet spreadsheet, int col, int row, String formula )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 oneCell.setFormula( formula );
	 }
	 public static void setCellFormula( Object spreadsheet, String cellName, String formula )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 setCellFormula( spreadsheet_XSpreadsheet, cellName, formula );
	 }
	 public static void setCellFormula( XSpreadsheet spreadsheet, String cellName, String formula )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 oneCell.setFormula( formula );
	 }
	 
	 public static String getCellString( Object spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellString( spreadsheet_XSpreadsheet, col, row );
	 }
	 public static String getCellString( XSpreadsheet spreadsheet, int col, int row )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 // Get the XText interface of the cell.
			 XText oneCell_XText = QI.XText( oneCell );
			 return oneCell_XText.getString();
	 }
	 public static String getCellString( Object spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 return getCellString( spreadsheet_XSpreadsheet, cellName );
	 }
	 public static String getCellString( XSpreadsheet spreadsheet, String cellName )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 // Get the XText interface of the cell.
			 XText cell_XText = QI.XText( oneCell );
			 return cell_XText.getString();
	 }
	 public static void setCellString( Object spreadsheet, int col, int row, String value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 setCellString( spreadsheet_XSpreadsheet, col, row, value );
	 }
	 public static void setCellString( XSpreadsheet spreadsheet, int col, int row, String value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a single cell from the spreadsheet.
			 XCell oneCell = spreadsheet.getCellByPosition( col, row );
			 // Get the XText interface of the cell.
			 XText oneCell_XText = QI.XText( oneCell );
			 oneCell_XText.setString( value );
	 }
	 public static void setCellString( Object spreadsheet, String cellName, String value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 XSpreadsheet spreadsheet_XSpreadsheet = QI.XSpreadsheet( spreadsheet );
			 setCellString( spreadsheet_XSpreadsheet, cellName, value );
	 }
	 public static void setCellString( XSpreadsheet spreadsheet, String cellName, String value )
					 throws com.sun.star.lang.IndexOutOfBoundsException {
			 // Get a range of cells from the spreadsheet.  Since the name passed in is supposed
			 //  to be the name of a single cell, this should be a cell range of only one cell.
			 XCellRange oneOrMoreCells = spreadsheet.getCellRangeByName( cellName );
			 // Get the top left cell of the cell range.  If the cellName was actually a range
			 //  of cells, such as B15:E37, then we end up with just a single cell B15.
			 XCell oneCell = oneOrMoreCells.getCellByPosition( 0, 0 );
			 // Get the XText interface of the cell.
			 XText oneCell_XText = QI.XText( oneCell );
			 oneCell_XText.setString( value );
	 }
	 
}

