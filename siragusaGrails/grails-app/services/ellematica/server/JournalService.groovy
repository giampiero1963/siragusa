package ellematica.server

import ellematica.graphics.JournalTableData

class JournalService {

    static transactional = true
		def clientiFornitoriPanel
		def filtroPanel
		

    def serviceMethod() {

    }
		
		def types=[
			hkb:Journal,
			hkl:Journal,
			hkm:Journal,
			hk:Journal,
			dz:Journal,
			pending:Journal,
			shipped:Invoice,
			payed:Invoice,
			notpayed:Invoice,
			all:Journal,
			]
		
		def stato=[
			pending:[prop:"shipped",value:false,order:"shippingDate",type: Journal],
			shipped:[prop:"shipped",value:true,order:"shippingDate",type:Invoice],
			payed:[prop:"payed",value:true,order:"shippingDate",type:Invoice],
			notpayed:[prop:"payed",value:false,order:"shippingDate",type:Invoice],
			all:[order: "shippingDate",type:Journal]

			]
		
		
		def commissioni=[
			hkb:[[prop:"importoHK",notequal:true,value:0.0 as Double],[prop:"payedHK",value:false],[prop:"payed",value:true],[prop:"shipped",value:true]],
			hkm:[[prop:"importoHK",notequal:true,value:0.0 as Double],[prop:"payedHK",value:false],[prop:"payed",value:false],[prop:"shipped",value:true]],
			hkl:[[prop:"importoHK",notequal:true,value:0.0 as Double],[prop:"payedHK",value:false],[prop:"payed",value:false],[prop:"shipped",value:false]],
			hk:[[prop:"importoHK",notequal:true,value:0.0 as Double],[prop:"payedHK",value:false]],
			dz:[[prop:"importoDZ",notequal:true,value:0.0 as Double],[prop:"payedDZ",value:false],[prop:"payed",value:true],[prop:"shipped",value:true]]
			]
		
		
		static dateFormat="dd-MM-yyyy"
		def periodo=[
			history:{[]},
			periodo:{par-> [from:(par.from?:Date.parse(dateFormat,"01-01-1960")),to:(par.to?:Date.parse(dateFormat,"31-12-2200"))] },
			anno:{par-> [from:Date.parse(dateFormat,"01-01-${par.year}"),to:Date.parse(dateFormat,"31-12-${par.year}")] },
			unanno:{par->
				def from=new Date()-365
				 [from:from,to:Date.parse(dateFormat,"31-12-2100")]
				 }
			]
		
		def ordingProp(){
			def fr=filtroPanel.filtroRicerca
			return (fr.stato && fr.stato!='all')? stato[fr.stato].order:"docDate"			
			
		}
		static all=["*"]
		
		def search(theOrd="asc",cls=null){
			def se=clientiFornitoriPanel.selected
			def fr=filtroPanel.filtroRicerca
			def ord ="shippingDate"
			def data
			def theClass=cls?:types[fr.stato]?:types[fr.commissioni]
			if(se.cust && se.supp){
				data=theClass.withCriteria{
					order(ord,theOrd)
					and{
						if(se.cust!=all){
							customer{
								or{
										
									se.cust.each{
										eq("code",it)
									}
								}
							}
						}
						if(se.supp!=all){
							
							supplier{
									or{
									se.supp.each{
										eq("code",it)
									}
								}
							}
						}
						if(fr.stato && fr.stato!='all'){
							ord=stato[fr.stato].order
							def prop=stato[fr.stato].prop
							def val=stato[fr.stato].value
							eq(prop,val)
						}
		
						if(fr.commissioni){
							commissioni[fr.commissioni]?.each{ comm->
								if(comm.notequal){
									not{
										eq(comm.prop,comm.value)
									}
								}
								else
									eq(comm.prop,comm.value)
							}
						}
						if(fr.periodo){
							def p=periodo[fr.periodo.tipo](fr.periodo.dati)
							if(p){
								between("shippingDate",p.from,p.to)
							}
						}
					}
					
				}
			}
			data
		}
		
		
		def journalPanel
		def totaliPanel
		
		def refresh={
			
			def data=search()
			if(data){
				journalPanel.setTableData(data.collect { new JournalTableData(it)})
				
			}
			else
				journalPanel.setTableData([])
			
			totali(data)
			
		}
		
		private totali(data){
			def res=[USD:[importo:0 as Double,importoHK:0 as Double,importoDZ:0 as Double],EUR:[importo:0 as Double,importoHK:0 as Double,importoDZ:0 as Double]]
			data.each{ j ->
				def mul=(j instanceof NotaDiCredito)?-1:1
				["importo","importoHK","importoDZ"].each{
					if(j[it]) res[j.valuta][it]+=mul*j[it]
				}
				
			}
			totaliPanel.setTotali(res)
			
		}
		
	
				
			
		
}
