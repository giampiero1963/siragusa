package ellematica.server

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype .*

import ellematica.graphics.event.*
import ellematica.graphics.*
import java.util.Observable;

import org.springframework.stereotype.Component

@Component
class ClientiFornitoriService implements Observer, InitializingBean{

	def clientiFornitoriPanel
	def filePanel
	def anagraficaPanel
	def controlloPanel
	def journalPanel
	def officeService
	def fileService
	def filtroPanel
 
	
		@Override
		public void afterPropertiesSet() {
			[clientiFornitoriPanel,anagraficaPanel,filePanel,officeService,fileService].each{
				//it.removeObserver(this)
	      it.addObserver(this)
				}
		}
		

    
    public void update(Observable o, Object value) {
			switch(value){
				case AnagraficaSelectionEvent:
					filtroPanel.refresh()
					break
				case AnagraficaChangedEvent:
					clientiFornitoriPanel.clientiUpdated(value)
					controlloPanel.clientiUpdated(value)
					break
				case FilePanelSelectionEvent:
					journalPanel.unselect()
					break
				case DocumentValues:
					controlloPanel.clear()
					controlloPanel.setDocumentValues(value)
					clientiFornitoriPanel.select([cust:[value.customer],supp:[value.supplier]])
					break
			}	 
			
    }
}
