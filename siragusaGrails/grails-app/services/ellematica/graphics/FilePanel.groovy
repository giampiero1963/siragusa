package ellematica.graphics
import javax.swing.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.TableModel
import javax.swing.tree.DefaultMutableTreeNode as TreeNode

import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*

import ellematica.graphics.event.*
import ellematica.server.*;
import groovy.model.DefaultTableModel
import static java.util.Calendar.YEAR
import static java.awt.FlowLayout.*
import static java.awt.ComponentOrientation.*
import org.springframework.stereotype.Component

@Component
class FilePanel extends Observable{

	def swing
	def dateFormat="dd-MM-yyyy"
	def constraints
	FileService fileService
	ControlloPanel controlloPanel;
	ClientiFornitoriPanel clientiFornitoriPanel
	HttpService httpService
	def journalPanel
	def fileList=[]
	
	def tableData=[
		[
			filename:"",
			date:""
		]
	]
	private def createTableModel(tableData) {
		swing.tableModel(list:tableData){
			propertyColumn( header:"FILE NAME", propertyName:"filename",editable:false )
		}
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

	def unselect(){
		swing.lst_files.selectionModel.clearSelection()
		
	}
	def upload(){
		def doc=controlloPanel.documentValues
		if(!doc) return
		
		def ret=httpService.post("journal","upload")
		if(!(ret =~ /^ok/)){
			alert(ret)
		}
		
	}
	
	def removeFile(file){
		fileService.removeFile(file)
		int pos=fileList.findIndexOf({it.filename==file})	
		if(pos>=0)
			fileList.remove(pos)
		viewData()		
	}
	def fileUploaded(file){
		fileService.fileUploaded(file)
		int pos=fileList.findIndexOf({it.filename==file})	
		if(pos>=0)
			fileList.remove(pos)
		viewData()
		
	}
	
	
	public init(){
		def tab

		def pane=swing.panel(constraints:constraints){
			swing.compoundBorder([
				swing.emptyBorder(2),
				swing.titledBorder("Upload")
			],parent:true)
			springLayout()
			//size:[300,150],preferredSize:[300,150]
			scrollPane(){
				tab=table(id:"lst_files",fillsViewportHeight:true) { createTableModel(tableData) }
			}
			panel(){
				borderLayout()
				button(id="bt_refresh",constraints:BorderLayout.LINE_END,
						action: action(name: 'REFRESH', closure: {refresh()}))
				
			}
		}
		SpringLayoutUtils.makeCompactGrid(pane, pane.componentCount, 1, 2,2, 2, 2)
		installListener()
		doubleClick(tab)
	}

	Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	Cursor defCursor  = new Cursor(Cursor.DEFAULT_CURSOR);
	
	def waitCursor(){
		swing.main.cursor=waitCursor
	}
	def resetCursor(){
		swing.main.cursor=defCursor
	}
	
	
	def refresh(){
		waitCursor()
		try{
			def files=fileService.refresh();
			fileList= files.collect{ 
				[filename:it]
			 }
			viewData()
		}catch(Exception e){
			e.printStackTrace()
		}
		resetCursor()
	}
	
	private Image getScaledImage(Image srcImg, Double scaleFactor){
		int w=(srcImg.width * scaleFactor) as int
		int h= (srcImg.height *scaleFactor) as int
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.background=Color.WHITE
		g2.clearRect(0, 0, w, h)
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
}

	def viewData(){
		def model=swing.lst_files.model
		model.rows.clear()
		model.rows.addAll(fileList?:[])
		model.fireTableDataChanged();
		swing.lbl_document.text=""
	}
	
	def currentShowingFile
	
	def openCurrentShowingFile(){
		if(currentShowingFile){
			selected(currentShowingFile)
		}
	}
	def showFile(value){
		currentShowingFile=value
		def file=fileService.imageFileName(value)
		ImageIcon theImage
		if(! (new File(file).exists())){
			 theImage=new ImageIcon()
		}else {
			ImageIcon ii=swing.imageIcon(file);
			theImage=new ImageIcon(getScaledImage(ii.image,1.3))
		}
		swing.lbl_document.icon=theImage
		swing.lbl_document.opaque=true
		swing.lbl_document.background=Color.WHITE
	}

	def selectedRow(int row){
		def table=swing.lst_files
		def value=table.getValueAt(row,0)
		if(!value) return
		waitCursor()
		showFile(value)
		fileService.getInfo(value)
		setChanged();
		notifyObservers(new FilePanelSelectionEvent(filename:value))
		
		if(value=~/^PI/ || value=~/^SH/){
			swing.st_pending.selected=true		
		}

		resetCursor()
	}
	
	
	def currentSelected=-1
	
	def installListener(){
		def table=swing.lst_files
	
		
		def ls=new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(table.selectedRow!=currentSelected){
					currentSelected=table.selectedRow
					selectedRow(currentSelected);
				}
			}
		}
		table.selectionModel.addListSelectionListener(ls);
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


/*
 */