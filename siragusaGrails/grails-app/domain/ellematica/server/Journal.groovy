package ellematica.server

class Journal {
	String docNo
	Date creationDate
	Customer customer
	Supplier supplier
	String fileName
	String valuta
	
	Hk hk
	Dz dz
	String note
	Date docDate
	Date shippingDate
	
	Double importo
	Double importoHK
	Double importoDZ
	Journal padre
	

	
	static constraints = {
		docNo nullable:true, maxSize:50
		note  nullable:true, maxSize:2048
		fileName nullable:true, maxSize:250
		padre nullable:true
		importo nullable:true
		valuta  nullable:true
		importoHK nullable:true
		importoDZ nullable:true
		fileName nullable:true
		hk nullable:true
		dz nullable:true
		shippingDate nullable:true
		docDate nullable:true
		
	}
	static mapping = {
		version false
		customer index:'jou_customer_idx'
		supplier index:'jou_supplier_idx'
		docDate index:'jou_doc_idx'
		shippingDate index:'jou_ship_idx'
		valuta index:'jou_valuta_idx'
	}

	static belongsTo = [padre:Journal]
}
