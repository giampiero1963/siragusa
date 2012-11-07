package ellematica.graphics
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import groovy.swing.SwingBuilder
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
//import static java.awt.GridBagConstraints.*
import java.awt.*

import com.ellematica.siragusa.util.*

import org.springframework.stereotype.Component

@Component
class TotaliPanel{
	def swing
	def constraints
	def httpService
	def numberFormatter

	def totali(){
		waitCursor()
		httpService.post("journal","totali")
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
	
	def setTotali(data){
		def formatter=numberFormatter.numberFormatter()
		["USD","EUR"].each{ valuta->
			def dati=data[valuta]
			dati.each{ tot->
				swing."tot_${valuta}${tot.key}".text=formatter.valueToString(tot.value)
			}
			swing."tot_${valuta}comm".text=formatter.valueToString(dati.importoHK+dati.importoDZ)
		}
	}

	public init(){

		def paneUSD,paneEUR
		def pane=swing.panel(constraints:constraints){
			flowLayout(alignment:FlowLayout.LEFT)
			
			paneUSD=swing.panel(){
				swing.compoundBorder([
//					swing.emptyBorder(1),
					swing.titledBorder('Totals USD')
				],parent:true)
					flowLayout(alignment:FlowLayout.LEFT)
			}
			paneEUR=swing.panel(constraints:constraints){
				swing.compoundBorder([
//					swing.emptyBorder(1),
					swing.titledBorder('Totals EUR')
				],parent:true)
					flowLayout(alignment:FlowLayout.LEFT)
			}
			tot(paneUSD,"tot_USDimporto",  "Imp:",0.0)
			tot(paneUSD,"tot_USDimportoHK","HK:",0.0)
			tot(paneUSD,"tot_USDimportoDZ","DZ:",0.0)
			tot(paneUSD,"tot_USDcomm","HK+DZ:",0.0)
			tot(paneEUR,"tot_EURimporto",  "Imp:",0.0)
			tot(paneEUR,"tot_EURimportoHK","HK:",0.0)
			tot(paneEUR,"tot_EURimportoDZ","DZ:",0.0)
			tot(paneEUR,"tot_EURcomm","HK+DZ:",0.0)
		/*
		SpringLayoutUtils.makeCompactGrid(pane, 1,
			pane.componentCount,
			10, 2, 10, 2);
		*/
		}
		pane
	
	}
	
	def tot(panel,postfix,title,value){
		panel.add(swing.label(text:"${title}"))
		panel.add(swing.label(id:"${postfix}",text:value,opaque:true,background:Color.WHITE))
	} 	

}