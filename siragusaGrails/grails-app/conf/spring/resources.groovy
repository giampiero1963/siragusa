import ellematica.server.FileService;
import ellematica.server.JournalService;

import ellematica.graphics.*
import ellematica.server.*
import groovy.swing.SwingXBuilder

beans = {
	swing(SwingXBuilder)
	dispatcherService(DispatcherService)
	httpService(HttpService)
	numberFormatter(MyNumberFormatter)
	clienteService(ClienteService)
	reportTask(ReportTask)
	officeService(OfficeService){
		reportTask=ref(reportTask)
	}
	anagraficaPanel(AnagraficaPanel){
		clienteService=ref(clienteService)
		httpService=ref(httpService)
		swing=ref(swing)
		numberFormatter=ref(numberFormatter)
	}
	clientiFornitoriPanel(ClientiFornitoriPanel){
		swing=ref(swing)
		httpService=ref(httpService)
	}
	controlloPanel(ControlloPanel){
		swing=ref(swing)
		httpService=ref(httpService)
		numberFormatter=ref(numberFormatter)
	}
	fileService(FileService){
		officeService=ref(officeService)
	}
	mySqlService(MySqlService){
		fileService=ref(fileService)
	}

	filePanel(FilePanel){
		swing=ref(swing)
		fileService=ref(fileService)
		controlloPanel=ref(controlloPanel)
		httpService=ref(httpService)
		clientiFornitoriPanel=ref(clientiFornitoriPanel)
	}
	totaliPanel(TotaliPanel){
		swing=ref(swing)
		httpService=ref(httpService)
		numberFormatter=ref(numberFormatter)
	}
	journalPanel(JournalPanel){
		swing=ref(swing)
		filePanel=ref(filePanel)
		totaliPanel=ref(totaliPanel)
		httpService=ref(httpService)
		fileService=ref(fileService)
		controlloPanel=ref(controlloPanel)
		numberFormatter=ref(numberFormatter)
	}
	fileSearchPanel(FileSearchPanel){
		swing=ref(swing)
		fileService=ref(fileService)
		httpService=ref(httpService)
		filePanel=ref(filePanel)
		clientiFornitoriPanel=ref(clientiFornitoriPanel)
	}
	filtroPanel(FiltroPanel){
		swing=ref(swing)
		httpService=ref(httpService)
		dispatcherService=ref(dispatcherService)
	}

	spuntaPanel(SpuntaPanel){
		swing=ref(swing)
	}

	filePanelService(FilePanelService){
		filePanel=ref(filePanel)
		journalPanel=ref(journalPanel)
	}
	clientiFornitoriService(ClientiFornitoriService){
		clientiFornitoriPanel=ref(clientiFornitoriPanel)
		filePanel=ref(filePanel)
		anagraficaPanel=ref(anagraficaPanel)
		controlloPanel=ref(controlloPanel)
		journalPanel=ref(journalPanel)
		fileService=ref(fileService)
		officeService=ref(officeService)
		filtroPanel=ref(filtroPanel)
		
	}
	
	journalService(JournalService){
		clientiFornitoriPanel=ref(clientiFornitoriPanel)
		filtroPanel=ref(filtroPanel)
		journalPanel=ref(journalPanel)
		totaliPanel=ref(totaliPanel)
	}
	
	statisticsService(StatisticsService){
		journalService=ref(journalService)
		fileService=ref(fileService)
	}
	mainView(MainView){
		swing=ref(swing)
		controlloPanel = ref(controlloPanel)
		filtriPanel= ref(filtroPanel)
		spuntaPanel=ref(spuntaPanel)
		filePanel=ref(filePanel)
		journalPanel=ref(journalPanel)
		clientiFornitoriPanel=ref(clientiFornitoriPanel)
		totaliPanel=ref(totaliPanel)
		fileSearchPanel=ref(fileSearchPanel)
		anagraficaPanel=ref(anagraficaPanel)
		httpService=ref(httpService)
		
	}

}
