package ellematica.graphics
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import ellematica.server.ClienteService
import groovy.swing.SwingBuilder
import groovy.swing.factory.SwingBorderFactory;
import ca.odell.glazedlists.*;

import ca.odell.glazedlists.matchers.MatcherEditor
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
//import static java.awt.GridBagConstraints.*
import java.awt.*
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat
import java.util.Observable;

import com.ellematica.siragusa.util.*

import ellematica.graphics.event.AnagraficaSelectionEvent
import ellematica.graphics.events.*

import org.springframework.stereotype.Component

@Component
class ClientiFornitoriPanel extends Observable {
	
	
	def swing
	def constraints


	
	def tableCustId="lst_cust"
	def tableSuppId="lst_supp"
	
	def data=[cust:[],supp:[]]
	def sel=[cust:[], supp:[]]
	
	def httpService
	
	def getSelected(){
		def ret=[:]
		["cust","supp"].each{ p->
			
			if(swing."ck_${p}".selected){
				ret[p]=["*"]
				return
			}
			ret[p]=sel[p]
		}
		ret
	}

	Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	def defCursor
	
	def waitCursor(){
		defCursor=swing.main.cursor
		swing.main.cursor=waitCursor
	}
	def resetCursor(){
		swing.main.cursor=defCursor
	}
	
	void setAll(value){
		["cust","supp"].each{ p->
			def tableid="lst_${p}"
			swing."ck_${p}".selected=value
		}
	}

	public init(){

		swing.panel(constraints:constraints){
			boxLayout(axis:BoxLayout.X_AXIS)
			
			
			pannelloSelezione("Customer","cust")
			pannelloSelezione("Supplier","supp")
			
			}			
		}
	

	void seletClientiOf(post,code){
		httpService.post("cliente","select_${post}",[code:code])
	}
	
	def clientiUpdated(event){
		def sel=[:]
		["cust","supp"].each{ post->
		
			sel[post]=[swing."anag_code_$post".selectedItem]
			
			data[post]=event."$post".collect{
				new SelectString(value:it.code)
			}
			
			def table=swing."lst_$post"
			def txt=swing."txt_${post}"
			def m=table.model
			def chbox=swing."ck_${post}"
			
			def tl=tableListener(post,table,chbox)
			table.model=model(txt,data[post])
			table.selectionModel.addListSelectionListener(tl)
			m.dispose()
			
		}
		select(sel)
		
	}
	
	
	
	
	def private notifyEvent(table,value,select){
		waitCursor()
		setChanged();
		notifyObservers(new AnagraficaSelectionEvent(table:table,select:select,name:value));
		resetCursor()
	}
	
	def doubleClick( table,post){
		table.addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent e){
							if (e.component.enabled && e.button == MouseEvent.BUTTON1 && e.clickCount == 2){
									Point p = e.point;
									int row = table.rowAtPoint(p);
									int col = table.columnAtPoint(p);
									def val=table.model.getValueAt(row, col)
									if(val)
										seletClientiOf(post,val)
									
							}
					}
			})
	}

	
	def tableListener(Object postfisso,Object table,Object checkBoxAll){
		return  new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				
				if(e.isAdjusting) return
				
				
								
				def sm=table.selectionModel;

				if(sm.selectionEmpty){
					sel[postfisso]=[]
				}else{
					checkBoxAll.model.selected=false
					TableModel model = table.model;
					def selList= (0 .. (table.rowCount-1)).collect{
						if(sm.isSelectedIndex(it) ) {
							return model.getValueAt(it, 0)
							
						}
					}
					if(sel[postfisso]==selList)
						return
					sel[postfisso]=selList
				}
				if(!swing."ck_${postfisso}".selected )
					notifyEvent(table,data,sel[postfisso])			
			}
		}
	}
	

	
	def selectAll=[cust:false,supp:false]
	
	def clearSelection(tableid,postfisso){
		swing."${tableid}".selectionModel.clearSelection()
		swing."txt_${postfisso}".text=""
	}
	
	def selectAll(){
		["cust","supp"].each{ postfisso->
			selectAll("lst_${postfisso}",postfisso)
		}
	}
	
	def selectAll(tableid,postfisso){
		JTable table=swing."${tableid}"
		table.selectionModel.setSelectionInterval(0, table.rowCount-1)
		swing."txt_${postfisso}".text=""
	}
	def selectCustOf(value){
		swing.ck_cust.selected=false
		select([cust:value])
	}
	def selectSuppOf(value){
		swing.ck_supp.selected=false
		select([supp:value])
	}
	def select(value){
		["cust","supp"].each{ post->
			

			if(value[post]!=null){
				JTable table=swing."lst_${post}"
				//table.clearSelection()
				ListSelectionModel sm=table.selectionModel
				sm.clearSelection()
				
				if(swing."ck_${post}".selected){
					selectAll("lst_${post}",post)	
					return
				}
			
				def rc=table.model.rowCount
					value[post].each{
					int i=-1
					for(i= 0;i<rc;i++){
						if(table.model.getValueAt(i,0)==it) {
							def r=table.convertRowIndexToView(i)
							sm.addSelectionInterval(r,r)
						}
					}	
					
				}
				
			}
			
		}
	}
	
	def pannelloSelezione(titolo,postfisso){
		
		def tableid="lst_${postfisso}"
		JTable thetab
		def chkbox
		def pane=swing.panel(){
			swing.compoundBorder([
				swing.emptyBorder(2),
				swing.titledBorder(titolo)
			],parent:true)
			borderLayout()

			chkbox=checkBox(id:"ck_${postfisso}",
				text:'ALL',
				action: action(name: 'ALL', closure: { e->
					if(e.source.model.selected){
						selectAll(tableid,postfisso)
						e.source.model.selected=true
					}else{
						JTable table=swing[tableid]
						if(table.selectedRowCount==0){
							e.source.model.selected=true;
							return
						}
					}
					notifyEvent(tableid,"*",e.source.model.selected)
					}),
				constraints:BorderLayout.PAGE_START)
		
			panel(constraints:BorderLayout.CENTER){
				borderLayout()
			
				textField(id:"txt_${postfisso}",constraints:BorderLayout.PAGE_START)
		
				scrollPane(constraints:BorderLayout.CENTER) {
					thetab=table(fillsViewportHeight:true,id:tableid,model:model(swing["txt_${postfisso}"],data[postfisso]))
				}
			}
			
			header = swing[tableid].tableHeader;
			header.reorderingAllowed=false;
			def tbl=tableListener(postfisso,thetab,chkbox)
			thetab.selectionModel.addListSelectionListener(tbl)
			doubleClick(thetab,postfisso)
	
		}
		JUtils.fixFont(pane, 10.0f)
		
	}
	
	def model(filterEdit,data){

		BasicEventList evt=new BasicEventList(data)

		MatcherEditor<SelectString> textMatcherEditor = 
			new TextComponentMatcherEditor<SelectString>(filterEdit, 
				new StringSelectionFilterator());
		FilterList<SelectString> textFilteredIssues = 
			new FilterList<SelectString>(evt,textMatcherEditor);

		

		new EventTableModel(textFilteredIssues,new SelectStringTableFormat());
		
	}
	
	static notSelected="&&&"
	static all=["*"]
	
	def getFilters(){
		selected
	}
		

}