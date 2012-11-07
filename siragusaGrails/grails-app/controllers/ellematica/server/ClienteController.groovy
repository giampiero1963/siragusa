package ellematica.server
import com.jhlabs.image.WholeImageFilter;
import ellematica.graphics.AnagraficaPanel
import ellematica.graphics.ClientiFornitoriPanel
import ellematica.graphics.ControlloPanel

import grails.converters.*

class ClienteController {
	ClienteService clienteService
	AnagraficaPanel anagraficaPanel
	ControlloPanel controlloPanel
	ClientiFornitoriPanel clientiFornitoriPanel
	
	
	def commissioni={
		def who=anagraficaPanel.selectedClients
		Cliente c= who.cust?Customer.findByCode(who.cust):null
		Cliente s= who.supp?Supplier.findByCode(who.supp):null
		def comm =clienteService.getCommissione(c,s,anagraficaPanel.dataInizioCommissione)
		anagraficaPanel.fee=comm
		render "ok"
	}
	
	

  def save = {
		def a=anagraficaPanel.saveInfo
		def saved=clienteService.save(a)
		def customerList=clienteService.customers.collect{
			clienteInfo(it)
		}?:[]
		def supplierList=clienteService.suppliers.collect{
			clienteInfo(it)
		}?:[]
		def fee=[hk:saved?.hk,dz:saved?.dz]
		def ret=[esito:true,customers:customerList,suppliers:supplierList,fee:fee]
		anagraficaPanel.savedInfo=ret
		controlloPanel.setCommissioni(fee)
		
		render "ok"
		}
		
		private def clienteInfo(c){
			return [id:c.id, code:c.code, name:c.name, priority:c.priority]
		}
		
		
		def select_supp={			
			def ret=clienteService.selectCustomersOf(params.code)
			clientiFornitoriPanel.selectCustOf(ret)
			render "ok"
		}
		
		def select_cust={			
			def ret=clienteService.selectSuppliersOf(params.code)
			clientiFornitoriPanel.selectSuppOf(ret)
			render "ok"
		}
}