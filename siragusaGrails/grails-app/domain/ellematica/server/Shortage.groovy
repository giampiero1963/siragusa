package ellematica.server

class Shortage extends Proforma{


  static constraints = {
  }
	static mapping = {
		discriminator "SH"
	}
}
