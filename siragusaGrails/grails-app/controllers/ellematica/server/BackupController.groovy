package ellematica.server

import ellematica.graphics.AnagraficaPanel

class BackupController {
	 MySqlService mySqlService
	 FileService fileService
	 AnagraficaPanel anagraficaPanel
	 
    def backup={
			mySqlService.theDump()
			fileService.zip()
			render "ok"
		}
		def restore={
			if(fileService.unzip()){
				mySqlService.backup()
				mySqlService.loaddump()
			}
			anagraficaPanel.initData()
			redirect controller:"journal", action:"refresh"	
		}
}
