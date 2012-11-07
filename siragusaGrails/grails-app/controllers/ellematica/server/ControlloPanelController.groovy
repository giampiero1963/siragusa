package ellematica.server

import ellematica.graphics.*

class ControlloPanelController {
	ControlloPanel controlloPanel
	ClienteService clienteService
	JournalPanel journalPanel
	
	def calcCommissioni={
		def doc=controlloPanel.getDocument()
		def dataordine=doc.dataordine
		if(doc.type != "PI"){
			def pi= journalPanel.selected
			if(pi){
				dataordine=Proforma.get(pi[0].id).docDate
			}
		}
		
		def ret=clienteService.getCommissione(Customer.findByCode(doc.customer), Supplier.findByCode(doc.supplier),
			dataordine)
		
		controlloPanel.commissioni=ret	
		render "ok"
	}
}
