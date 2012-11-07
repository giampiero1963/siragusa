package ellematica.server

class Cliente{
	String code
	String name
	String address
	String zipCode

	String bankDetails
	String factoryAddress
	String note

	
	int priority

	
	static constraints = {
		code nullable:false, maxSize:3
		name nullable:true, maxSize:500
		address maxSize:2048, nullable:true
		factoryAddress maxSize:2048, nullable:true
		zipCode     maxSize:10,nullable:true
		note maxSize:1024,nullable:true
		bankDetails maxSize:2048, nullable:true
		
	}
	static mapping = {
		version false
		code index:'code_idx'
		priority index:'code_idx'
		sort (priority:"desc", code:"asc")
	}

}

