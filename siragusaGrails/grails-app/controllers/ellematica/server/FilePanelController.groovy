package ellematica.server

import ellematica.graphics.*

class FilePanelController {
		ControlloPanel controlloPanel
		FilePanel filePanel
		
    def index = { }
		
		def upload={
			def documento=controlloPanel.documentValues
			println documento.type
			render "ok"
		}
}
