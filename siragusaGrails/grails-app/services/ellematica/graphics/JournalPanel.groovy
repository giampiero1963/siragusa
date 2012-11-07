package ellematica.graphics
import javax.swing.*
import javax.swing.JTable.NumberRenderer
import javax.swing.border.LineBorder
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableColumn
import javax.swing.table.TableColumnModel
import javax.swing.text.NumberFormatter
import javax.swing.tree.DefaultMutableTreeNode as TreeNode

import ellematica.server.Customer;
import ellematica.server.Dz;
import ellematica.server.FileService;
import ellematica.server.Hk;
import ellematica.server.Journal;
import ellematica.server.Supplier;
import groovy.swing.SwingBuilder
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.renderer.DefaultTableRenderer
import org.jdesktop.swingx.renderer.FormatStringValue
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
//import static java.awt.GridBagConstraints.*
import java.awt.*
import java.math.RoundingMode
import java.text.*
import java.util.Comparator;
import java.util.Date;

import com.ellematica.siragusa.util.*



@org.springframework.stereotype.Component
class JournalPanel extends Observable{
	def swing
	def constraints
	
	def filePanel
	def httpService
	def totaliPanel
	ControlloPanel controlloPanel
	
	FileService fileService
	
	def numberFormatter
	
	def numFormatter(){
		numberFormatter.numberFormatter()
	}


	public init(){
		def pane
		swing.panel(constraints: constraints){
			borderLayout()
			pane=panel(constraints:BorderLayout.PAGE_START){
				springLayout()
				button(
					action:action(name: 'DELETE', closure: {del()})
					)
				button(
					action:action(name: 'SEND', closure: {sendTo()})
					)
				totaliPanel.init()
			
			}
			swing.scrollPane(constraints:BorderLayout.CENTER){
				table()
			}
		}	

		SpringLayoutUtils.makeCompactGrid(pane, 1,
			pane.componentCount,
			2, 2, 2, 2);
	}
	
	private def fireLoadingData(){
		loadingData=true
		journalModel.fireTableDataChanged()
	}
	
	def refreshRow(inv){
		def ind = journalModel.data.findIndexOf({it.id==inv.id})
		journalModel.data[i]=new JournalTableData(inv)
		fireLoadingData()		
	}
	
	def alert(msg){
		swing.optionPane().showMessageDialog(null, msg,"Attenzione!",JOptionPane.WARNING_MESSAGE)
		
	}
	
	def message(msg){
		swing.optionPane().showMessageDialog(null, msg,"Attenzione!",JOptionPane.INFORMATION_MESSAGE)
	}
		
	def confirm(msg){
		def response = swing.optionPane().showConfirmDialog(null, msg, "Confirm",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return response == JOptionPane.YES_OPTION
		
	}
	
	private def del(){
		def sel=selected
		if(!sel){
			alert("E' necessario selezionare almeno una riga")
			return
		}
		if(!confirm("Conferma la cancellazione delle righe selezionate?"))
			return
		waitCursor()
		httpService.post("journal","remove")
		resetCursor()
	}
	
	Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	Cursor defCursor  = new Cursor(Cursor.DEFAULT_CURSOR);
	
	def waitCursor(){
		swing.main.cursor=waitCursor
	}
	def resetCursor(){
		swing.main.cursor=defCursor
	}
	
	
	private def sendTo(){
		def sel=selected
		if(!sel){
			alert("E' necessario selezionare almeno una riga")
			return
		}
		waitCursor()
		fileService.sendTo(sel*.filename)
		resetCursor()
	}
	def unselect(){
		swing.lst_journal.clearSelection()
	}
	
	private modelIndex(idx){
		swing.lst_journal.convertRowIndexToModel(idx)
	}
	
	private viewIndex(idx){
		swing.lst_journal.convertRowIndexToView(idx)
	}
	
	def select(id){
		def table=swing.lst_journal
		def idx=viewIndex(journalModel.data.findIndexOf{it.id==id})
		table.selectionModel.setSelectionInterval(idx, idx);
	}
	
	def removeSelected(){
		def tmp=selected
		journalModel.data-=tmp
		journalModel.fireTableDataChanged()
	}
	
	def loadingData
	
	

	
	def setTableData(data){
		journalModel.data=data;
		fireLoadingData()
	}

	def getSelected(){
		journalModel.data.findAll({it.selected})
	}
	
	JournalTableModel journalModel

	private tableModel(columns){
		journalModel =new JournalTableModel(numFormatter())
		journalModel.data=[]
		journalModel.columns=columns
		journalModel
	}
	
	private def createTableModel() {
		def model=tableModel([
			new PropertyColumn( header:" ", propertyName:"selected",type:Boolean.class,editable:true), //0
			new PropertyColumn( header:"TYPE", propertyName:"type",editable:false ),    //1
			new PropertyColumn( header:"DOC", propertyName:"doc",editable:true ),  //2
			new PropertyColumn( header:"DATE", propertyName:"date",type:Date.class,editable:true ),//3
			new PropertyColumn( header:"CUST", propertyName:"customer",editable:false ),//4
			new PropertyColumn( header:"SUPP", propertyName:"supplier",editable:false ),//5
			new PropertyColumn( header:"SHIPMENT", propertyName:"shipment" ,type:Date.class,editable:true),//6
			new PropertyColumn( header:"CURR", propertyName:"valuta", editable:false),
			new PropertyColumn( header:"DEP", propertyName:"deposit" ,type:Double.class,editable:true),
			new PropertyColumn( header:"AMOUNT", propertyName:"importo",type:Double.class,editable:true ),
			new PropertyColumn( header:"HK", propertyName:"hk",type:Double.class ,editable:true),
			new PropertyColumn( header:"DZ", propertyName:"dz",type:Double.class,editable:true ),
			new PropertyColumn( header:"S", propertyName:"shipped",type:Boolean.class,editable:true ),
			new PropertyColumn( header:"P", propertyName:"payed",type:Boolean.class,editable:true ),
			new PropertyColumn( header:"H", propertyName:"payedHK",type:Boolean.class,editable:true ),
			new PropertyColumn( header:"D", propertyName:"payedDZ",type:Boolean.class,editable:true ),
			new PropertyColumn( header:"NOTE", propertyName:"note",editable:true )
		])
	}


	

	private def table(){
		def ret=swing.table(id:"lst_journal",fillsViewportHeight:true,autoResizeMode:JTable.AUTO_RESIZE_OFF)
		ret.model=	createTableModel() 
		addSelectionListener(ret)
		setTableCellUpdated()
		setTableRenderer(ret)
		setTableCellEditor(ret)
		ret
	}
	
	def setTableCellEditor(table){
		def cellEditor=new MyDateEditor();
	
		def idx=journalModel.columns.findIndexValues { it.propertyName in ["shipment","date"] }
		idx.each {
			
			TableColumn col = table.columnModel.getColumn(it as int);
			col.cellEditor= cellEditor
		}
		cellEditor=new MyNumberEditor(numFormatter())
		idx=journalModel.columns.findIndexValues { it.propertyName in ["importo","hk","dz","deposit"] }
		idx.each {
			TableColumn col = table.columnModel.getColumn(it as int);
			col.cellEditor= cellEditor
		}
		
	}

	def setTableRenderer(table){
			
		def stringValue = new FormatStringValue(new SimpleDateFormat("dd-MM-yyyy"));
		
		table.setDefaultRenderer(Date.class, new DefaultTableRenderer(stringValue))
		table.setDefaultRenderer(Double.class, new NumRenderer(numFormatter()))
	
	}
	

	
	
	
	private def addSelectionListener(table){
		table.selectionMode=ListSelectionModel.SINGLE_SELECTION
		def rowSM = table.selectionModel
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (! lsm.isSelectionEmpty()) {
						
						int selectedRow = modelIndex(lsm.getMinSelectionIndex());
						rowSelected(journalModel.data[selectedRow])
				}
		}
		});
	
	}
	
	private def rowSelected(JournalTableData row){
		waitCursor()
		if(row.filename)
			filePanel.showFile(row.filename)
		resetCursor()
		controlloPanel.setDocumentValues(row)
		filePanel.unselect()
	}
	
	DataChanged dataChanged
		
	
	
	def setTableCellUpdated(){
		journalModel.addTableModelListener(new TableModelListener(){
			void tableChanged(TableModelEvent e){
				if(e.type==TableModelEvent.UPDATE){
					if(loadingData ){
						loadingData=false
						return
					}
					def name=journalModel.columns[e.column].propertyName
					if(name=="selected") return
					def data=journalModel.data[modelIndex(e.firstRow)]
					if(!data) return;
					dataChanged=new DataChanged(data:[[name:name,value:data[name]]],id:data.id)
					def fire
					if(name == "importo"){
						data.hk=(data.importo?:0.0)* (data.hkPerc?:0.0)
						data.dz=(data.importo?:0.0)* (data.dzPerc?:0.0)
						dataChanged.data+=[[name:'hk',value:data.hk],[name:'dz',value:data.dz]]
						fire=true
					}
					
					if(data.type=='IN' && name == "importo" ){
						if(! data.importo && !( data.payed && data.payedHK && data.payedDZ)){
							data.payedHK=true
							data.payedDz=true
							dataChanged.data+=[name:'payedHK',value:true]
							dataChanged.data+=[name:'payedDZ',value:true]
							
							fire=true				
						}
					}
					if(data.type=='IN' && name in ["dz","hk"] ){
						def nn=name.toUpperCase()
						if(! data[name] && ! data["payed$nn"]){
							
							data["payed$nn"]=true
							dataChanged.data+=[name:"payed$nn",value:true]
							fire=true
						}
					}
					
					waitCursor()
					httpService.post("journal","cellUpdated")
					if(fire) journalModel.fireTableDataChanged()
					resetCursor()
				}
			}
			
		})
	}

}

class PropertyColumn{
	String header
	String propertyName
	Class type
	boolean editable
}



class JournalTableModel extends AbstractTableModel{
	
	def formatter
	public JournalTableModel(formatter){
		this.formatter=formatter
	}
	
	
	def data=[]
	def columns=[]
	def addColumn(PropertyColumn c){
		columns+= c
	}

	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columns.size;
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size;
	}
	@Override
	public Object getValueAt(int r, int c) {
		
		return data[r]."${columns[c].propertyName}";
	}

	
	private def doubleFormat(value){
		return formatter.stringToValue(value as String)
	}

	void setValueAt(Object par,int r,int c){
		if(columns[c].type==Double.class) par=doubleFormat(par)
		data[r]."${columns[c].propertyName}"=par
		fireTableCellUpdated(r, c);
	}
	
	public String getColumnName(int column) {
		return columns[column].header?:"";
	}

	
	public boolean isCellEditable(int row, int column) {
		def ret=columns[column].editable?:false
		if(!ret) return false
		def name=columns[column].propertyName
		switch (name){

			case "deposit":
				return data[row].type in ["PI","SH"]
			 case "shipped":
			 case "payed":
			 case "payedHK":
			 case "payedDZ":
					return data[row].type in ["IN","CN"]
			
			default:
				return true
		}
	
		
	}

	public Class getColumnClass(int column) {
		columns[column].type?:Object.class
	}
}

class MyNumberEditor extends DefaultCellEditor{
	
		NumberFormatter format
 
		Double cellEditorValue;
 
		public MyNumberEditor(formatter)
		{
			super(new JFormattedTextField(formatter));
				format=formatter
				component.name="Table.editor";

		}
 
		public boolean stopCellEditing()
		{
				cellEditorValue = format.stringToValue(component.value as String);
				
				return super.stopCellEditing();
		}
 
		public Component getTableCellEditorComponent(JTable table, Object value,
																								 boolean isSelected,
																								 int row, int column)
		{
				cellEditorValue = null;
				if (value instanceof Double)
				{
					cellEditorValue = format.stringToValue(value as String) ;
				}
				component.border=new LineBorder(Color.black);
				return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}
 
	
}

class MyDateEditor extends DefaultCellEditor
{
		SimpleDateFormat format 
 
		Date value;
 
		public MyDateEditor()
		{
			super(new JFormattedTextField(new SimpleDateFormat("dd-MM-yyyy")));
			
				format=new SimpleDateFormat("dd-MM-yyyy");
				component.name="Table.editor";
				// you should actually check for null
		}
 
		public boolean stopCellEditing()
		{
				value = component.value;
				
				return super.stopCellEditing();
		}
 
		public Component getTableCellEditorComponent(JTable table, Object value,
																								 boolean isSelected,
																								 int row, int column)
		{
				this.value = null;
				if (value instanceof Date)
				{
						value = format.format((Date)value);
				}
				component.border=new LineBorder(Color.black);
				return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}
 
		public Object getCellEditorValue()
		{
				return value;
		}
}

class NumRenderer extends DefaultTableCellRenderer {
	
	def formatter
	public NumRenderer(format) {
		setHorizontalAlignment(SwingConstants.RIGHT);
		this.formatter=format
	}
	
	public void setValue(Object aValue) {
		Object result = aValue;
		if (( aValue != null) && (aValue instanceof Number)) {			
			result = formatter.valueToString(aValue.doubleValue());
		}
		super.setValue(result);
	}
}
class DataChanged{
	def data
	def id
}


