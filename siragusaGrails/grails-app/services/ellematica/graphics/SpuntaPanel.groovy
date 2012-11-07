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
import org.springframework.stereotype.Component

@Component
class SpuntaPanel{
	def stato
	def commissioni
	def periodo
	def swing
	def dateFormat="dd-MM-yyyy"
	def constraints

	public init(){
			
		swing.panel(constraints:constraints){
			// pannello superiore
			// pannello filtri di ricerca e bottoni
			swing.compoundBorder([
				swing.etchedBorder(),
			],parent:true)
			boxLayout(axis:BoxLayout.X_AXIS)
			checkBox(id="cb_shipped",text:'SHIPPED')
			checkBox(id="cb_paied",text:'PAYED')
			checkBox(id="cb_hk",text:'HK')
			checkBox(id="cb_dz",text:'DZ')

		
		}
	}
	
	
}

/*
 */