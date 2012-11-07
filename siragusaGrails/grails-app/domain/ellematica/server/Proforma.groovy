package ellematica.server

class Proforma extends Journal{
	Double deposit
    static constraints = {
			deposit nullable:true
    }
		
	static mapping = {
			discriminator "PI"
	}
}
