package ellematica.graphics


import javax.swing.*
import javax.swing.JComboBox
import javax.swing.text.NumberFormatter
import javax.swing.tree.DefaultMutableTreeNode as TreeNode

import ellematica.graphics.event.AnagraficaChangedEvent
import ellematica.server.ClienteService;
import ellematica.server.HttpService
import groovy.swing.SwingBuilder
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
//import static java.awt.GridBagConstraints.*
import java.awt.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Observable;

import com.ellematica.siragusa.util.*
import ellematica.server.*
import org.springframework.stereotype.Component

@Component
class AnagraficaPanel  extends Observable {
	def swing
	def constraints
	static transactional = true

	ClienteService clienteService;
	HttpService httpService

	def dateFormat="dd-MM-yyyy"

	private def customers ;

	private def suppliers ;
	
	
	void setAll(value){
	
	}
	
	def numberFormatter

	public init(){
		customers=[]
		suppliers=[]
		def custPanel
		def suppPanel
		def buttonPanel
		def pane=swing.panel(constraints:constraints){
			swing.compoundBorder([
				swing.emptyBorder(2),
				swing.titledBorder('Customer/Supplier handling')
			],parent:true)
			gridLayout(cols: 2, rows:0,hgap:2)
			custPanel=commonPanel("Cust.","cust","dz",null,"customers")
			suppPanel=commonPanel("Supp.","supp","hk",null,"suppliers",true)
	
		}
		JUtils.fixFont(pane,10.0f)

	}
	def numFormatter(){
		numberFormatter.numberFormatter()
	}

	
	def commonPanel(lab,post,con,constr,data,add=null){
		def combo
		def pane=swing.panel(constraints:constr){
			springLayout()
			panel(){
				flowLayout(alignment:FlowLayout.LEFT)
				
				label(lab)
				combo=widget(
					wideComboBox([emptyItem] as Object[]),
						id:"anag_code_$post",
						editable:true
						,actionPerformed:{event->
							select(post,event,data)}
						)
				
				label("Pr.")
				widget(wideComboBox([1, 2, 3] as Object[]),id:"anag_pr_$post")
			}
		
			AutoCompleteDecorator.decorate(combo)
			
			panel(){
				boxLayout(axis:BoxLayout.Y_AXIS)
				panel(){
					flowLayout(alignment:FlowLayout.LEFT)
					label("Name",horizontalAlignment:JLabel.LEFT)
				}
				textField(id:"anag_name_$post",columns:20)
			}
			
			panel(){
				borderLayout()
				panel(constraints:BorderLayout.LINE_START){
					flowLayout(alignment:FlowLayout.LEFT)
					label(con.toUpperCase())
					widget(new JFormattedTextField(numFormatter()),id:"anag_$con",value:0.0)
				}
				if(add){
					button(id:"anag_save",constraints:BorderLayout.LINE_END,action: action(name: 'SAVE', closure: {save()}))
				}					
			}			
	
		}
		SpringLayoutUtils.makeCompactGrid(pane, pane.componentCount,1,
			2, 2, 2, 2);
		pane
	}
	
	
	long dz=0
	long hk=0

	private values(post,clienti){
		def combo=swing["anag_code_${post}"]
		String code=combo.selectedItem?.trim()
		if(! code) return
			def cliente=clienti.find({it.code==code})
		long id= cliente?cliente.id:0


		String name=swing["anag_name_${post}"].text
		int priority=swing["anag_pr_${post}"].selectedItem as int
		def dz=commissione(this.dz,"dz")
		def hk=commissione(this.hk,"hk")
		def ret=[code:code,id:id,name:name.trim(),priority:priority]
		if(dz)ret.dz=dz
		if(hk)ret.hk=hk
		ret
	}


	private commissione(id,tipo){

		def text=swing["anag_${tipo}"].text.trim()
		text= text?:"0"
		return [id:id,value:(text as double)]
	}

	private verifySelectedObject(combo){
		Object code=combo.selectedItem
		if(!code || code==emptyItem) return null
		def n=combo.itemCount
		for(int i=0;i<n ;i++){
			if(combo.getItemAt(i)==code) return code
		}
		return null
	}

	def setCommissione(cc,valore){
	  def obj=swing."anag_$cc";
		def old=obj.value
		obj.value=valore
		
		JTextUtil.firePropertyChange ((JFormattedTextField)obj,"value",old as double,valore as double)
	}

	def setStartingDate(startingDate=new Date()){
		//swing.anag_fee_start.date=startingDate
	}
	
	private select(post,event,clienti){
		Object code=verifySelectedObject(event.source)
		def dati=this[clienti]
		def cliente
		def other=(post=="cust")?"supp":"cust"
		if(!code) {
			cliente=[code:emptyItem,name:"",priority:1]
			def cc=(post=="cust")?"dz":"hk"
			setCommissione(cc,0.0)	
			setStartingDate()
		}else{
			if(! dati) return;
			cliente=dati.find({it.code==code})
		}
		swing."anag_name_${post}".text=cliente.name
		setPr(cliente,post)
		if(code || verifySelectedObject(swing."anag_code_${other}"))
			loadCommissioni(event)
	}

	private setPr(cliente,post){
		def pr=cliente.priority?cliente.priority-1:0
		swing."anag_pr_${post}".selectedIndex=pr;
		println "$post $pr"
	}
	
	private loadCommissioni(event)
	{
		def customer=values("cust",customers)
		def supplier=values("supp",suppliers)
		if(customer || supplier){
			def par=[:]
			if(customer) par.cust=customer
			if(supplier) par.supp=supplier
			if(! commissioniCorrenti){
				httpService.post("cliente","commissioni")
			}
			

		}

	}
	
	def setFee(ret){
		def hk=ret.hk?ret.hk.value:0.0
		def dz=ret.dz?ret.dz.value:0.0
		setCommissione("hk",hk as Double)
		setCommissione("dz",dz as Double)
		startingDate=hk?ret.hk.creation:dz?ret.dz.creation:new Date()
		this.hk=hk?hk:0
		this.dz=dz?dz:0
	}


	def getSaveInfo(){
		def customer=values("cust",customers)
		def supplier=values("supp",suppliers)

		def par=[:]
		
		if(customer || supplier){
			if(customer) par.cust=customer
			if(supplier) par.supp=supplier
			par.startingDate=new Date()
		}
		
		par
	}
	
	def commissioniCorrenti 
	
	def Date getDataInizioCommissione(){
		def date=new Date()
		date
	}
	
	def getSelectedClients(){
		String cust=swing.anag_code_cust.selectedItem?.trim()
		String supp=swing.anag_code_supp.selectedItem?.trim()
		return [cust:cust,supp:supp]
	}
	
	
	def setSavedInfo(ret){
		customers=ret.customers
		suppliers=ret.suppliers
		String cust=swing.anag_code_cust.selectedItem?.trim()
		String supp=swing.anag_code_supp.selectedItem?.trim()
		addObjects(swing.anag_code_cust,customers)
		addObjects(swing.anag_code_supp,suppliers)

		notifyEvent()
	}
	
	private save(){
		def customer=values("cust",customers)
		def supplier=values("supp",suppliers)
		if(customer || supplier)httpService.post("cliente","save")
	}
	static emptyItem="   "

	private addObjects(combo,clienteList){

		def theList=clienteList*.code?:[emptyItem]

		def s=combo.selectedItem
		
		while(combo.itemCount>1)
			combo.removeItemAt(1)
		
		theList.each{
			if(it!=emptyItem)combo.addItem(it)
		}
		
		combo.selectedItem=s

	}

	
	
		
	
	
	public initData(){
		customers=clienteService.customers
		suppliers=clienteService.suppliers
		addObjects(swing.anag_code_cust,customers)
		addObjects(swing.anag_code_supp,suppliers)
		notifyEvent()


	}

	def private notifyEvent(){
		setChanged();
		notifyObservers(new AnagraficaChangedEvent(cust:customers, supp:suppliers));
	}
	
	 def wideComboBox(Object[] i){
		  new  JComboBox(i) {
		
				private boolean layingOut = false;
		
				public void doLayout(){
						try{
								layingOut = true;
								super.doLayout();
						}finally{
								layingOut = false;
						}
				}
		
				public Dimension getPreferredSize(){
					def dim = super.preferredSize;
					dim.width+=5
					return dim;
				}
				
				public void setPreferredSize(Dimension pd){
					def dim = super.preferredSize;
					["width","height"].each{
						println it + dim[it]
						dim[it] = pd[it]==-1?dim[it]:Math.min(pd[it],dim[it])
					}
			
					super.preferredSize=dim
					
				}
				
				public void setSize(Dimension pd){
					def dim = super.maximumSize;
					["width","height"].each{
						println it + dim[it]
						dim[it] = pd[it]==-1?dim[it]:Math.min(pd[it],dim[it])
					}
			
					super.maximumSize=dim
					
				}
				public Dimension getSize(){
						Dimension dim = super.size;
						if(!layingOut)
								dim.width = Math.max(dim.width, preferredSize.width);
						return dim;
				}
				public init(final Object[] items){
					super.init(items);
				}
	
				public init(Vector items) {
					super.init(items);
				}
	
				public init(ComboBoxModel aModel) {
						super.init(aModel);
			
				}
		  }
	 }
	

}

