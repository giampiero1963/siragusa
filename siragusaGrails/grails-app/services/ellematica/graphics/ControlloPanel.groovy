package ellematica.graphics
import javax.swing.*

import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.NumberFormatter
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import ellematica.server.HttpService
import groovy.swing.SwingBuilder
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.springframework.beans.factory.InitializingBean;

import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
//import static java.awt.GridBagConstraints.*
import java.awt.*
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.TextEvent
import java.awt.event.TextListener
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import com.ellematica.siragusa.util.*
import org.springframework.stereotype.Component

@Component
class ControlloPanel {

	
	def swing
	def constraints
	def dataOrdine
	def numeroOrdine
	def customer
	def supplier
	def fattura
	def valuta
	def hk
	def cz
	def shipment
	def dateFormat="dd-MM-yyyy"
	
	static emptyItem="   "
	
	HttpService httpService

	def clientiUpdated(event){
		
		["cust","supp"].each{ post->
			def selected=swing."ctrl_${post}".selectedItem
			def list=event."$post"
			def cbox=swing."ctrl_${post}"
			addObjects(cbox,list)
			swing."ctrl_${post}".selectedItem=selected
		}	
	}

	def propertyChange(destination,test){
		new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent e) {
				if(test()){
					Object source = e.getSource();
					destination.value=source.value * documentValues.importo.valore
				}
			}
		}
	}
	@Override
	public void afterView() {
		
		def testHk={ 
			def custAnagCode=swing.anag_code_cust.selectedItem
			def suppAnagCode=swing.anag_code_supp.selectedItem
			def custCtrlCode=swing.ctrl_cust.selectedItem
			def suppCtrlCode=swing.ctrl_supp.selectedItem
			
			(custAnagCode == custCtrlCode) && 
			( (suppAnagCode == suppCtrlCode) || (suppAnagCode == emptyItem) )  
		}
		
		
		def testDz={
			def custAnagCode=swing.anag_code_cust.selectedItem
			def suppAnagCode=swing.anag_code_supp.selectedItem
			def custCtrlCode=swing.ctrl_cust.selectedItem
			def suppCtrlCode=swing.ctrl_supp.selectedItem
	
			
			(suppAnagCode == suppCtrlCode) &&
			( (custAnagCode == custCtrlCode) || (custAnagCode == emptyItem) )
		}
	
		//calcCommissioni()
			//swing.anag_hk.addPropertyChangeListener("value", propertyChange(swing.ctrl_hk,testHk))
			//swing.anag_dz.addPropertyChangeListener("value", propertyChange(swing.ctrl_dz,testDz))
	}
	
	def calcCommissioni={
		httpService.post("controlloPanel", "calcCommissioni")
	}
	
	
	def setCommissioni(c){
		double importo=swing.ctrl_importo?.value?:0.0 as Double
		["hk","dz"].each{
			def val=importo * (c[it]?.value ?:0)
			swing."ctrl_${it}".value=val
			documentValues["importo${it.toUpperCase()}"]=val
		}
		
	}
	
	private addObjects(combo,clienteList){
		
		def theList=clienteList*.code?:[emptyItem]
		def num = combo.itemCount
		def toremove=[]
		for(int i=0;i<num;i++){
			def obj=combo.getItemAt(i)
			if(obj!=emptyItem)
				toremove+=obj
		}
		toremove.each{ combo.removeItem(it) }
		theList.each{
			if(it!=emptyItem)combo.addItem(it)
		}
	}

	def numFormatter(){
		numberFormatter.numberFormatter()
	}
	
	
	def textModified=false
	

	
	public init(){
	
		JPanel pane=swing.panel(constraints:constraints){
			swing.compoundBorder([
				swing.emptyBorder(3),
				swing.titledBorder('Control')
			],parent:true)
			
			// gridLayout(cols: 2, rows:0,hgap:1)
			springLayout()
			label("Date")
			label("Doc. No.")
			dataOrdine=	widget(new JDateChooser(null,dateFormat),id:"ctrl_dataordine")
			numeroOrdine=textField(id:"ctrl_numord")
			label("Customer")
			label("Supplier")
			customer=comboBox(
					id:"ctrl_cust",
					items:[emptyItem],
					editable:true
					// actionPerformed:{event-> lb.text = event.source.selectedItem}
					)
			suppliers=comboBox(
					id:"ctrl_supp",
					items:[emptyItem],
					editable:true
					// actionPerformed:{event-> lb.text = event.source.selectedItem}
					)
			AutoCompleteDecorator.decorate(swing.ctrl_cust)
			AutoCompleteDecorator.decorate(swing.ctrl_supp)
			panel(){
				gridLayout(cols: 2, rows:0,hgap:2)
				valuta=buttonGroup()
				radioButton(id:"ctrl_usd",text:'USD',buttonGroup:valuta,selected:true)
				radioButton(id:"ctrl_eur",text:'EUR',buttonGroup:valuta)
			}
			panel{
				borderLayout()
				label("HK",constraints:BorderLayout.LINE_START)
				button(constraints:BorderLayout.CENTER,action:action(name:"UPLOAD",closure: { e->upload()}))
			}
			fattura=widget(new JFormattedTextField(numFormatter()),id:"ctrl_importo",columns:10)
			hk=widget(new JFormattedTextField(numFormatter()),id:"ctrl_hk",formatter:numFormatter(),columns:10)
			label("Shipment")
			label("DZ")
			shipment=widget(new JDateChooser(null,dateFormat),id:"ctrl_shipment")
			dz=widget(new JFormattedTextField(numFormatter()), id:"ctrl_dz",columns:10)
		}
		fattura.addFocusListener(new MyFocusListener(httpService))
		JUtils.fixFont(pane,10.0f)
		SpringLayoutUtils.makeCompactGrid(pane,pane.componentCount/2 as int,2,1,1,1,1)
	}
	

	def DocumentValues documentValues 
	
	def getDocument(){
		documentValues['importoDZ']=swing.ctrl_dz.value
		documentValues['importoHK']=swing.ctrl_hk.value		
		documentValues['importo']= new ValoreConValuta(valore: swing.ctrl_importo.value, valuta: (swing.ctrl_usd.selected?"USD":"EUR"))
		documentValues['dataordine']=swing.ctrl_dataordine.date
		documentValues['numord']=swing.ctrl_numord.text
		documentValues['numero_doc']=swing.ctrl_numord.text
		documentValues['customer']=swing.ctrl_cust.selectedItem
		documentValues['supplier']=swing.ctrl_supp.selectedItem
		documentValues['data_imbarco']=swing.ctrl_shipment.date
		
		
		
		
		return documentValues	
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
	
	def upload(){
		DocumentValues doc=documentValues
		if(!doc) return
		def test= [ "customer",
			"supplier",
			"data_imbarco",			
			"dataordine",
			"importoDZ",
			"importoHK"
			].findAll{ !doc[it]}
		if(test && ! confirm("I seguenti campi sono assenti o pari a 0:\n${test.join('\n')}\n\nProcedere comunque?")){
			return
		}
		if(! (doc.importo && doc.importo.valuta && doc.importo.valore  )){
			if(!confirm("La valuta o l'importo sono nulli o pari a 0\n\nProcedere comunque?"))
				return			
		}
			
		def ret=httpService.post("journal","upload")
		if(!(ret =~ /^ok/)){
			alert(ret)
		}
		
	}
	
	def setDocumentValues (DocumentValues values){
		documentValues=values
	
		swing.ctrl_importo.value=values.importo? values.importo.valore:0.0 as Double
		String valuta=values.importo?.valuta?.toLowerCase()
		swing.ctrl_usd.selected=false
		swing.ctrl_eur.selected=false
		
		if(valuta)
			swing."ctrl_$valuta".selected=true
		
		swing.ctrl_dataordine.date=values.dataordine
		swing.ctrl_numord.text=values.numero_doc
		swing.ctrl_cust.selectedItem=values.customer
		swing.ctrl_supp.selectedItem=values.supplier
		swing.anag_code_cust.selectedItem=values.customer
		swing.anag_code_supp.selectedItem=values.supplier
		swing.ctrl_shipment.date=values.data_imbarco 
		if(valuta && (values.importoHK==null || values.importoDZ==null )){
			calcCommissioni()
		}
	}
	
	def setDocumentValues (JournalTableData values){
		if(!documentValues) documentValues=[:]
		
		swing.ctrl_importo.value=values.importo
		String valuta=values.valuta
		swing.ctrl_usd.selected=false
		swing.ctrl_eur.selected=false
		swing."ctrl_${valuta.toLowerCase()}".selected=true
		
		swing.ctrl_dataordine.date=values.date
		swing.ctrl_numord.text=values.doc
		swing.ctrl_cust.selectedItem=values.customer
		swing.ctrl_supp.selectedItem=values.supplier
		swing.anag_code_cust.selectedItem=values.customer
		swing.anag_code_supp.selectedItem=values.supplier
		swing.ctrl_shipment.date=values.shipment
		swing.ctrl_hk.value=values.hk
		swing.ctrl_dz.value=values.dz
		documentValues.filename=values.filename
		documentValues.type=values.type
		
		
	}
	def clear(){
			documentValues=new DocumentValues()
		
			swing.ctrl_importo.value=0.0 as double
			swing.ctrl_usd.selected=false
			swing.ctrl_eur.selected=false
			
		
			swing.ctrl_dataordine.date=null
			swing.ctrl_numord.text=""
			swing.ctrl_cust.selectedItem=""
			swing.ctrl_supp.selectedItem=""
			swing.ctrl_shipment.date=null
			swing.ctrl_hk.value=0.0 as double
			swing.ctrl_dz.value=0.0 as double
			
		
	}
	
	 def numberFormatter
	
	private verifySelectedObject(combo){
		Object code=combo.selectedItem
		if(!code || code==emptyItem) return null
		def n=combo.itemCount
		for(int i=0;i<n ;i++){
			if(combo.getItemAt(i)==code) return code
		}
		return null
	}
}

public class MyFocusListener implements FocusListener{
	def currentValue
	def  httpService
	
	MyFocusListener(httpService){
		this.httpService=httpService
	}
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		currentValue = arg0.source.value
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub 
		if(currentValue != arg0.source.value){
			httpService.post("controlloPanel", "calcCommissioni")
		}
		
		
	}

}
