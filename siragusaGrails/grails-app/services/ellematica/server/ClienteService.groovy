package ellematica.server;
import org.springframework.stereotype.Component;

@Component
class ClienteService {

    static transactional = true

		def getCustomers(){
			Customer.findAll("from Customer order by priority asc, code asc")
    }
		def getSuppliers(){
			Supplier.findAll("from Supplier order by priority asc, code asc")
		}
		
		def getCliente(int i){
			Cliente.get(i)
		}
		
		
	
		
		def save(c){
			def customer
			def supplier
			def dz
			def hk
			def ret=[:]
			if(c.cust){
				
				c.cust.code=c.cust.code.toUpperCase();
				customer= Customer.findByCode(c.cust.code)?:new Customer()
				customer.code=c.cust.code
				customer.priority=c.cust.priority
				customer.name=c.cust.name	
				if(!customer.save(flush:true)){
					
						customer.errors.each {
								 println it
						}
				 }
				ret.cust=customer
				
				
			}
			if(c.supp){
				c.supp.code=c.supp.code.toUpperCase();
				supplier= Supplier.findByCode(c.supp.code)?:new Supplier()
				supplier.code=c.supp.code
				supplier.priority=c.supp.priority
				supplier.name=c.supp.name
				if(!supplier.save(flush:true)){
					
					supplier.errors.each {
							 println it
					}
				}
				ret.supp=supplier
			}
			
			if(c.supp?.hk ) ret.hk=newHk(c.startingDate, c.supp.hk,customer,supplier)
			if(c.cust?.dz) 	ret.dz=newDz(c.startingDate,c.cust.dz,customer,supplier)
			
			ret
		
		 
		}
		
		
		def sort=[sort:"id",order: "desc"]
		
		def getDz(c,s,d=null){		
			if(c==null) return 0
			d=d?:new Date()
			def dz=s?Dz.findByCustomerAndSupplier(c,s,sort):null
			if(!dz) dz=Dz.findByCustomerAndSupplierIsNull(c,sort)
			dz
		}
		 def getHk(c,s,d=null){
			 if(s==null ) return 0;
			 d=d?:new Date()
			 def hk=c?Hk.findByCustomerAndSupplier(c,s,sort):null
			 if(!hk) hk=Hk.findBySupplierAndCustomerIsNull(s,sort)
			 hk
		 }
		
		 private def newHk(date,com,cust,supp){
			 def hk=getHk(cust,supp,date)
			 
			 def obj=hk?:new Hk()
			 obj.creation=date
			 obj.value=com.value
			 obj.customer=cust
			 obj.supplier=supp
			 obj.save(flush:true)
			 obj
		 }
 
		private def newDz(date,com,cust,supp){
			def dz=getDz(cust,supp,date)
			
			def obj=dz?:new Dz()
			obj.creation=date
			obj.value=com.value
			obj.customer=cust
			obj.supplier=supp
			obj.save(flush:true)
			obj
		}
		
		def getCommissione(Customer c, Supplier s,Date d=null){
			
			if(!(c||s) ) return [:]
			
			return [dz:getDz(c,s,d),hk:getHk(c,s,d)]
		}

		
		def selectCustomersOf(code){
			return Journal.executeQuery("select distinct(j.customer.code) from Journal j where j.supplier.code=:code",[code:code]);
		}
	
		def selectSuppliersOf(code){
			return Journal.executeQuery("select distinct(j.supplier.code) from  Journal j where j.customer.code=:code",[code:code]);	
		}

}
