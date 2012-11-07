package ellematica.graphics
import javax.swing.*
import java.awt.*

import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
import static java.awt.FlowLayout.*
import static java.awt.ComponentOrientation.*
import ellematica.server.*
import org.springframework.stereotype.Component

@Component
class FiltroPanel{
	def stato
	def commissioni
	def periodo
	def swing
	def dateFormat="dd-MM-yyyy"
	def constraints

	def httpService
	def dispatcherService
	
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
		httpService.post("journal", "refresh")
		resetCursor()
	}
	
	def report(){
		
		httpService.post("journal", "report")
		
	}
	
	
	def statistics(){
		httpService.post("statistics", "statistics")
		
	}

	
		
	public init(){
		swing.panel(constraints:constraints){
			swing.compoundBorder([
				swing.emptyBorder(3)
				,
				swing.titledBorder('Filters')
			],parent:true)

			borderLayout()
			//gridLayout(cols: 1, rows:0)
			//boxLayout(axis:BoxLayout.Y_AXIS)				
			def topPane=panel(constraints:BorderLayout.PAGE_START){
				// pannello dei filtri di ricerca
				swing.compoundBorder([
					swing.loweredBevelBorder(),
				],parent:true)
				springLayout()
				pannelloFiltroRicerca()
				pannelloCommissioni()
			}
			JUtils.fixFont(topPane, 10.0f)
			pannelloSelezioneFiltriDate(BorderLayout.CENTER)
			SpringLayoutUtils.makeCompactGrid(topPane, 1,topPane.componentCount,
				2, 2, 2, 2);
		
			
			
		}
	}
	def getFiltroRicerca(){
		def tipoPeriodo=periodo.find({it.radio.selected})?.id.substring(3)
		def data=[:]
		if(tipoPeriodo && tipoPeriodo=="anno"){
			data.year=swing.per_data_year.value
		}else{
			data.from=swing.per_data_from.date
			data.to=swing.per_data_to.date
		}
		
		[ 			
			stato:stato.find({it.radio.selected})?.id?.substring(3),
			commissioni:commissioni.find({it.radio.selected})?.id?.substring(3),
			periodo:[tipo:tipoPeriodo,dati:data]
		]
	}
	def buttonGroup
	
	def pannelloFiltroRicerca(){
		
		swing.panel(){
		
			gridLayout(cols: 1, rows:0)
			buttonGroup=buttonGroup()
			stato=[
			[id:"st_all",radio: radioButton(id:"st_all",buttonGroup:buttonGroup,action: action(name:'ALL', closure: {refresh()}))],
			[id:"st_pending",radio:radioButton(id:"st_pending",buttonGroup:buttonGroup,selected:true,action: action(name:'PENDING SHIPMENT',closure: {refresh()}))],
			[id:"st_shipped",radio:radioButton(id:"st_shipped",buttonGroup:buttonGroup,action: action( name:'SHIPPED',closure: {refresh()}))],
			[id:"st_notpayed",radio:radioButton(id:"st_notpayed",buttonGroup:buttonGroup,action: action(name:'NOT PAYED', closure: {refresh()}))],
			[id:"st_payed",radio:radioButton(id:"st_payed",buttonGroup:buttonGroup,action: action(name:'PAYED', closure: {refresh()}))]
			]
		
		}//fine pannello dei bottoni
	}
	

	def pannelloCommissioni(){
		swing.panel(){
			gridLayout(cols:1,rows:0)
			commissioni=[
			[id:"st_hkb",radio:radioButton(id:"st_hkb",buttonGroup:buttonGroup,action: action(name:'HKB',closure: {refresh()}))],
			[id:"st_hkm",radio:radioButton(id:"st_hkm",buttonGroup:buttonGroup,action: action(name:'HKM',closure: {refresh()}))],
			[id:"st_hkl",radio:radioButton(id:"st_hkl",buttonGroup:buttonGroup,action: action(name:'HKL', closure: {refresh()}))],
			[id:"st_hk",radio:radioButton(id:"st_hk",buttonGroup:buttonGroup,action: action( name:'HKT',closure: {refresh()}))],
			[id:"st_dz",radio:radioButton(id:"st_dz",buttonGroup:buttonGroup,action: action( name:'DZ',closure: {refresh()}))]
			]
		}
	}

	def pannelloSelezioneFiltriDate(constr=null){
		def today=new Date()
		def pane=swing.panel(constraints:constr){
			swing.compoundBorder([
				swing.loweredBevelBorder(),
			],parent:true)
			//springLayout()
			//gridLayout(cols:1,rows:0)
			boxLayout(axis:BoxLayout.Y_AXIS)
			def pp=buttonGroup()
			def pr_periodo
			def pr_anno
			def pr_unanno
			def pr_history
		
			panel(){
				flowLayout(alignment:FlowLayout.LEFT)
				pr_periodo=radioButton(id="pr_periodo",text:"From/To",buttonGroup:pp)
				panel(){
					flowLayout(alignment:FlowLayout.LEFT)
					widget(id:"per_data_from",new JDateChooser(null,dateFormat))
					widget(id:"per_data_to",new JDateChooser(today,dateFormat))
				}
			}
		
			panel(){
				flowLayout(alignment:FlowLayout.LEFT)
				pr_anno=radioButton(id:"pr_anno",text:'Year:',buttonGroup:pp)
				widget( id:"per_data_year",new JYearChooser(),year:today[YEAR])
				//widget(Box.createRigidArea(new Dimension(100,0)))
				pr_unanno=radioButton(id:"pr_unanno",buttonGroup:pp,selected:true,action: action( name:'One Year',closure: {refresh()}))
				pr_history=radioButton(id:"pr_history",buttonGroup:pp,action: action( name:'History',closure: {refresh()}))
				button(id:"bt_update",action: action(name: 'REFRESH', closure: {refresh()}))
			
			}
			/*
			panel(){
				flowLayout(alignment:FlowLayout.LEFT)
				pr_unanno=radioButton(id:"pr_unanno",buttonGroup:pp,selected:true,action: action( name:'One Year',closure: {refresh()}))
				pr_history=radioButton(id:"pr_history",buttonGroup:pp,action: action( name:'History',closure: {refresh()}))
			}
			*/
			
			
	
			periodo=[
			[id:"pr_periodo",radio:pr_periodo],
			[id:"pr_anno",radio:pr_anno],
			[id:"pr_unanno",radio:pr_unanno],
			[id:"pr_history",radio:pr_history]
			]
		}
		//SpringLayoutUtils.makeCompactGrid(pane, pane.componentCount,1,
		//	2, 2, 2, 2);
		JUtils.fixFont(pane,10.0f)
		pane
	}

	def pannelloAquisizioneDate(){

		swing.panel(){
			//pannello delle date
			def today=new Date()

			boxLayout(axis:BoxLayout.Y_AXIS)
			panel(){
				boxLayout(axis:BoxLayout.X_AXIS)
				label("From",alignmentY:0.5,alignmentX:0.5)
				from=widget(id:"per_data_from",new JDateChooser(null,dateFormat))
				label("To",alignmentY:0.5,alignmentX:0.5)
				widget(Box.createRigidArea(new Dimension(15,0)))
				to=widget(id:"per_data_to",new JDateChooser(today,dateFormat))
			}
			panel(){
				boxLayout(axis:BoxLayout.X_AXIS)
				label("Year",alignmentY:0.5,alignmentX:0.5)
				widget(Box.createRigidArea(new Dimension(5,0)))
				year=widget(id:"per_data_year",new JYearChooser(),year:today[YEAR])
				widget(Box.createRigidArea(new Dimension(90,0)))
			}
			button(id="bt_update",action: action(name: 'REFRESH', closure: {refresh()}))
		}

	}
	def pannelloFiltriDate(){
		swing.panel(){
			// filtri date e acquisizione
			boxLayout(axis:BoxLayout.X_AXIS)
			pannelloSelezioneFiltriDate()
			pannelloAquisizioneDate()
		}
	}

	def pannelloCommissioniDate(){
		swing.panel(){
			boxLayout(axis:BoxLayout.X_AXIS)
			pannelloCommissioni()
			pannelloFiltriDate()

		}
	}
}



/*
 */