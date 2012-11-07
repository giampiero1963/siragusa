package ellematica.server

class NotaDiCredito extends Invoice{

    static constraints = {
    }
	static mapping = {
			discriminator "CN"
	}
}
