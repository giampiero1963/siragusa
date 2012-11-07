package ellematica.server

import java.util.Date;

class Invoice extends Journal{
	
	
	boolean shipped=false
	boolean payed=false
	boolean payedHK=false
	boolean payedDZ=false
	Date shippedDate
	Date payedDate
	Date payedDateHK
	Date payedDateDZ
	

  static constraints = {
		payedDate nullable:true
		payedDateHK nullable:true
		payedDateDZ nullable:true
		shippedDate nullable:true
		payed nullable:true, default:false
		payedHK nullable:true
		payedDZ nullable:true
		shipped nullable:true
  }
	static mapping = {
			discriminator "IN"
			shipped index:'jou_flship_idx'
			payed index:'jou_flpayed_idx'
			payedHK index:'jou_flpayedhk_idx'
			payedDZ index:'jou_flpayeddz_idx'
	}
}
