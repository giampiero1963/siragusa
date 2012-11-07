package ellematica.server;
import org.springframework.stereotype.Component;

@Component
class StatisticsService{

    JournalService journalService
		static transactional=true
		FileService fileService
		
		def statistics={
				def dataProp=journalService.ordingProp()
				def datas=journalService.search("asc")
			
			
				datas=datas.findAll {it instanceof Invoice }.sort({Invoice a, Invoice b -> a.shippingDate <=> b.shippingDate})
				
				
				def ret=[
					complessivi:doStat(datas,"shippingDate")
			
				]				
				fileService.statistics(ret)
	
		}
    
		
		private def doStat(datas,dataProp,breakCond={a,b->false},setOp={a,b,c->null}){
			def cdata
			def state=[:]
			def cdate
			def complessivi=[]
			def currentDate
			int i=0;
			datas.each{ Journal j ->
				cdate=j[dataProp]
				
				if(! (currentDate && (cdate[Calendar.MONTH] ==currentDate[Calendar.MONTH])) || breakCond(j,state)) {
					def d=j[dataProp]
					if(currentDate){
						def p=new GregorianCalendar(time:currentDate)
						def n=new GregorianCalendar(time:d)
						n.add(GregorianCalendar.MONTH,-1)
						def pm=p.get(GregorianCalendar.MONTH)
						def nm=n.get(GregorianCalendar.MONTH)
						for(int k=pm;k!=nm;){
							p.add(GregorianCalendar.MONTH,1)
							k=p.get(GregorianCalendar.MONTH)
							currentDate=Date.parse("dd-MM-yyyy","01-${k+1}-${d[Calendar.YEAR]}")
							if(cdata) complessivi+=cdata
							cdata=[date:currentDate,
								dati:[EUR:[importo:0.0 as double, importoHK:0.0 as double, importoDZ:0.0 as double],USD:[importo:0.0 as double, importoHK:0.0 as double, importoDZ:0.0 as double]]]							
						}
						
					}
					currentDate=Date.parse("dd-MM-yyyy","01-${d[Calendar.MONTH]+1}-${d[Calendar.YEAR]}")
					if(cdata) complessivi+=cdata
					cdata=[date:currentDate,
								dati:[EUR:[importo:0.0 as double, importoHK:0.0 as double, importoDZ:0.0 as double],USD:[importo:0.0 as double, importoHK:0.0 as double, importoDZ:0.0 as double]]]
					
				}
				def mult=(j instanceof NotaDiCredito)?-1:1
				["importo","importoHK","importoDZ"].each{prop->
					cdata.dati[j.valuta][prop]+=mult*j[prop]?:0.0 as double
				}
			}
			if(cdata) complessivi+=cdata
				
			complessivi
		
    }
}
