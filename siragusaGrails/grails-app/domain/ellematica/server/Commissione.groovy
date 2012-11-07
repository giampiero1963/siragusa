package ellematica.server


class Commissione{
	
	Date creation
	double value
	
	Customer customer
	Supplier supplier
	
	
	static mapping = {
		version false
		customer index:'comm_supplier_idx'
		supplier index:'comm_customer_idx'
		creation index:'com_creation_idx'
		sort creation:"desc"
	}
	static constraints = {
		supplier nullable:true
		customer nullable:true
		value nullable:true
	}


}
