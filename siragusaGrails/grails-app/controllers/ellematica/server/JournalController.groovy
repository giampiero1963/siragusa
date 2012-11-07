package ellematica.server

import ellematica.graphics.*


class JournalController {
		ControlloPanel controlloPanel
		JournalPanel journalPanel
		JournalService journalService
		MainView  mainView
		FilePanel filePanel
		ClienteService clienteService
		ClientiFornitoriPanel clientiFornitoriPanel
		FiltroPanel filtroPanel
		TotaliPanel totaliPanel
		FileService fileService
		FileSearchPanel fileSearchPanel
		
    def index = { }
		def upload={
			def documento=controlloPanel.document
			if(Customer.findByCode(documento.customer) == null){
				render "Customer ${documento.customer} non salvato" 
				return
			}
			if(Supplier.findByCode(documento.supplier) == null){
				render "Supplier ${documento.supplier} non salvato"
				return
			}
			 
			
			
			def id=this."upload${documento.type}"((Object) documento)
			if(id){
				filePanel.fileUploaded(documento.filename)				
				refresh()
				journalPanel.select(id)
			}
			
			render "ok"
			return
		}
		
		def init(Journal p, DocumentValues doc){
			p.shippingDate=doc.data_imbarco
			p.valuta=doc.importo.valuta
			p.importo=doc.importo.valore
			p.docNo=doc.numero_doc
			p.customer=Customer.findByCode(doc.customer)
			p.supplier =Supplier.findByCode(doc.supplier)
			p.creationDate=new Date()
			p.fileName=doc.filename
			p.docDate=doc.dataordine
			p.importoDZ=doc.importoDZ
			p.importoHK=doc.importoHK			
			p.dz=clienteService.getDz(p.customer,p.supplier,p.docDate)
			p.hk=clienteService.getHk(p.customer,p.supplier,p.docDate)
			
		}
		
		
		def getSelectedProforma(){
			journalPanel.selected.find({it.type in ["PI","SH"]})
		}
		
		def remove={
			def pi = journalPanel.selected
			pi.each{
				def p=Journal.get(it.id)
				if(p){
					filePanel.removeFile(p.fileName)
					journalPanel.removeSelected()
					
					p.delete(flush:true)
				}
			}
			refresh()
			render "ok"
		}
		
		def uploadPI={ doc->
			def p=Proforma.findByFileName(doc.filename)		
			p= p?:new Proforma()
			init(p,doc)

			if(!p.save(flush:true)){
				p.errors.each{
					println it
				}
			}			
			return p.id
		}
		
		def uploadIN={doc->
		
			
			def inv=Invoice.findByFileName(doc.filename)?:new Invoice()
			init(inv,doc)
			

			if(! inv.importoDZ){
				inv.payedDZ=true
			}
			if(! inv.importoHK){
				inv.payedHK=true
			}
			
			inv.save(flush:true)
			
			return inv.id
		}
		
		def uploadSH={doc->
			def p=Shortage.findByFileName(doc.filename)		
			p= p?:new Shortage()
			init(p,doc)
			p.save(flush:true,failOnError:true)			
			return p.id

		}
		def uploadCN={doc->
			def p=NotaDiCredito.findByFileName(doc.filename)			
			p=p?:new NotaDiCredito()
				
			init(p,doc)
			
			p.save(flush:true)

			return p.id
		}
		
		def all=["*"]
		
		def makeData(data){
			def cust=data.collect{
				"'"+it+"'"
			}.join(',')
		}
		
	
		
		
		def refresh={
			
			journalService.refresh()
			
			
			render "ok"
		}
		
		
		
		static propMap=[
			shipment:"shippingDate",
			dz: "importoDZ",
			hk: "importoHK",
			doc: "docNo",
			date: "docDate"
			]
		
		def cellUpdated={
			def dataChanged=journalPanel.dataChanged
			if(dataChanged?.data ){
				def j=Journal.get(dataChanged.id as long)
				dataChanged.data.each{
					j[propMap[it.name]?:it.name]=it.value
				}
				if(!j.save(flush:true)){
						j.errors.each{
							println it
						}
				}
			}
			render "ok"			
		}
		
		
		
		def fee(f){
			 
			[id:f.id,creation:f.creation,customer:f.customer?.code,supplier:f.supplier?.code,value:f.value]
	
		}
		
		private reportData(){
			def data=journalService.search("asc")
			def datas
			if(data){
				datas=[journal:[],hk:[],dz:[]]
				data.each{ d->
					def j=new JournalTableData(d)
					datas.journal+=j
					["hk","dz"].each{ f->
						if(! datas[f].find({d[f].id==it.id})){
							datas[f]+=fee(d[f])
						}
					}
				}
			}
			datas
		}
		def report={
			def data=reportData()
			if(data) fileService.report(data)
			render "ok"
		}
		def filterFile={
			if(fileSearchPanel.selectedFile){
				def data=Journal.findByFileName(fileSearchPanel.selectedFile)
				if(data)	
					journalPanel.setTableData([new JournalTableData(data)])
				else
					journalPanel.setTableData([])
			}
			render "ok"
		}
}
