package ellematica.graphics

import ellematica.server.*

import org.springframework.stereotype.Component



public class JournalTableData {
	
	
		boolean selected=false;
		long id=0;
		String type="";
		String doc="";
		Date date;
		String customer="";
		String supplier="";
		Date shipment
		Double importo=0.0;
		Double hk=0.0;
		Double dz=0.0;
		Double deposit=0.0
		String note;
		String valuta
		String filename=""
		Boolean payed
		Boolean shipped
		Boolean payedHK
		Boolean payedDZ
		Double  hkPerc=0.0
		Double  dzPerc=0.0
		
		
		public JournalTableData(){
			
		} 
		
		public JournalTableData(Journal data){
			id=data.id
			switch(data){
				case Shortage:
					type="SH"
					break
				case Proforma:
					type="PI"
					deposit=data.deposit?:0.0
					break
				case NotaDiCredito:
					type="CN"
					payed=data.payed
					payedHK=data.payedHK
					payedDZ=data.payedDZ
					shipped=data.shipped
					break
				case Invoice:
					type="IN"
					payed=data.payed
					payedHK=data.payedHK
					payedDZ=data.payedDZ
					shipped=data.shipped
					
					break
				
				
				}
			
			doc=data.docNo
			date=data.docDate //.format("dd-MM-yyyy")
			shipment=data.shippingDate//.format("dd-MM-yyyy")
			filename=data.fileName
			customer=data.customer.code
			supplier=data.supplier.code
			valuta=data.valuta
			note=data.note
			importo=data.importo?:0.0
			hk=data.importoHK?:0.0
			dz=data.importoDZ?:0.0
			hkPerc=data.hk?.value?:0.0
			dzPerc=data.dz?.value?:0.0
			
		
		}
	
	
}
