package ellematica.server;
class Customer extends Cliente {
	String tel
	String fax
	String eori
	String expressAirCourier
	String shippingForwarder
	String shippingMark	
	String note2
	static constraints = {
		tel maxSize:25,nullable:true
		fax maxSize:25,nullable:true
		note2 maxSize:1024, nullable:true
		expressAirCourier maxSize:2048, nullable:true
		shippingForwarder maxSize:2048, nullable:true
		eori     maxSize:20,nullable:true
		shippingMark maxSize:2048,nullable:true
		
	}

}
