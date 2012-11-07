package ellematica.graphics
import javax.swing.*
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import ellematica.server.FileService
import groovy.swing.SwingBuilder
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
//import static java.awt.GridBagConstraints.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import com.ellematica.siragusa.util.*

import org.springframework.stereotype.Component

@Component
class FileSearchPanel{
	def swing
	def constraints
	
	FileService fileService
	def httpService
	
	def tableData=[]
	def tableModel
	FilePanel filePanel
	ClientiFornitoriPanel clientiFornitoriPanel
	
	
	private def createTableModel(tableData) {
		tableModel=swing.tableModel(list:tableData){
			propertyColumn( header:"X", propertyName:"selected",type:Boolean.class,editable:true )
			propertyColumn( header:"FILE NAME", propertyName:"filename",editable:false )
			propertyColumn( header:"DATE", propertyName:"date",type:Date.class,editable:false )
		
		}
	}

	Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	Cursor defCursor  = new Cursor(Cursor.DEFAULT_CURSOR);
	
	def waitCursor(){
		swing.main.cursor=waitCursor
	}
	def resetCursor(){
		swing.main.cursor=defCursor
	}
	
	
	def getSelected(){
		def model =swing.src_table.model
		model.rowsModel.value.findAll{it.selected}
		
	}
	private def sendTo(){
		def sel=selected
		if(!sel){
			alert("E' necessario selezionare almeno una riga")
			return
		}
		waitCursor()
		try{
			fileService.sendTo(sel*.filename)
		}catch(Exception e){
			println e
		}
		resetCursor()
	}
	
	def search(String src){
		waitCursor()
		def ff=clientiFornitoriPanel.filters
		tableData= fileService.search(src,ff).collect{[filename:it.filename,date:it.date,selected:false]}
		def model =swing.src_table.model
		model.rowsModel.value=tableData
		model.fireTableDataChanged()
		resetCursor()
	}
	
	public init(){
		def intpan
		swing.panel(constraints:constraints){
			swing.compoundBorder([
				swing.emptyBorder(2),
				swing.titledBorder('Search')
			],parent:true)
			borderLayout()
			intpan=panel(constraints:BorderLayout.PAGE_START){
				springLayout()
				textField(columns:30,id:"search_file")
				button(id:"bt_search",action: action(name: 'SEARCH', closure: {search(swing.search_file.text)}))
				button(id:"bt_search",action: action(name: 'SEND', closure: {sendTo()}))
			}
			
			swing.scrollPane(constraints:BorderLayout.CENTER){
				table(id:"src_table",fillsViewportHeight:true) {
					createTableModel(tableData)
				}
				
			}
		}
		SpringLayoutUtils.makeCompactGrid(intpan,1, intpan.componentCount,
			2, 2, 2, 2);
		doubleClick(swing.src_table)
		selectionListener(swing.src_table)
	}
	
	private def selectionListener(table){
		table.selectionMode=ListSelectionModel.SINGLE_SELECTION 
		def rowSM = table.selectionModel
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (! lsm.isSelectionEmpty()) {
						
						int selectedRow = lsm.getMinSelectionIndex();
						rowSelected(tableData[selectedRow])
				}
		}
		});
	
	}
	def selectedFile
	private def rowSelected( row){
		selectedFile=null
		if(row.filename){
			selectedFile=row.filename
			filePanel.showFile(selectedFile)
			httpService.post("journal","filterFile")
		}
	}
	
	
	
	def selected(val){
		fileService.open(val)
	}
	
	def doubleClick( table){
		table.addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent e){
							if (e.component.enabled && e.button == MouseEvent.BUTTON1 && e.clickCount == 2){
									Point p = e.point;
									int row = table.rowAtPoint(p);
									int col = table.columnAtPoint(p);
									def val=table.model.getValueAt(row, col)
									if(val)
										selected(val)
									
							}
					}
			})
	}
	
}