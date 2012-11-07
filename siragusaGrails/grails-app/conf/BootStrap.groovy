import ellematica.graphics.MainView
import ellematica.server.HttpService

class BootStrap {
		MainView mainView
		def anagraficaPanel
		def fileService
		def journalService
		def filePanel
		HttpService httpService
		
    def init = { servletContext ->
			println "ok"
			httpService.initPhase=true
			mainView.loadGraph()
			mainView.initData()
			filePanel.viewData()
			journalService.refresh()
			httpService.initPhase=false
    }
    def destroy = {
    }
}
