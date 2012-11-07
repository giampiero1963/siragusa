package ellematica.graphics

class DocumentValues {
	Date dataordine;
	String numero_doc;
	String numord;
	Date data_imbarco
	ValoreConValuta importo
	String currency
	String filename
	String type
	String customer
	String supplier
	Double importoDZ
	Double importoHK
	
	
	
	void setFilename(String name){
		filename=name
		type=name[0..1]
		customer=name[3..5]
		supplier=name[7..9]
	}
}
