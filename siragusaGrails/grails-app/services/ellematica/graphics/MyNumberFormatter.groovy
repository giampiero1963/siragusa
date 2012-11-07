package ellematica.graphics

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import javax.swing.text.NumberFormatter

class MyNumberFormatter {
	def NumberFormatter numberFormatter(){
		def dec=new DecimalFormat()
		dec.minimumFractionDigits=2
		dec.maximumFractionDigits=2
		dec.roundingMode=java.math.RoundingMode.HALF_DOWN

		dec.decimalSeparatorAlwaysShown=true
		dec.decimalFormatSymbols=new DecimalFormatSymbols(decimalSeparator:'.',groupingSeparator:',')
		dec.groupingUsed=true
		dec.groupingSize=3
		Locale.setDefault(Locale.UK)
		NumberFormatter textFormatter = new NumberFormatter(dec);
		textFormatter.setOverwriteMode(true);
		textFormatter.setAllowsInvalid(true);
		textFormatter
	}
	

}
