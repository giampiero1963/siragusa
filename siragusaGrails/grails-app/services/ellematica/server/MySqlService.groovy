package ellematica.server
import groovy.io.FileType
import java.text.SimpleDateFormat;

import org.artofsolving.jodconverter.util.PlatformUtils

class MySqlService {

	FileService fileService
	
	String mySqlHome=PlatformUtils.windows? System.env.MYSQL_HOME:"/usr/local/mysql/bin"
	String dumpCmd="mysqldump"
	String mysqlCmd="mysql "
	
	def theDump={
		String dumpFile=fileService.dirDumps().path+"${File.separator}dump.sql"
		export(dumpFile)
	}
	
	def backup={
		Date date=new Date()
		String ext=new SimpleDateFormat("yyyyMMdd-hhmmss").format(date)
		String dumpFile=fileService.dirBackups().path+"${File.separator}backup-${ext}.sql"
		export(dumpFile)
	}
	
	def restore={
		File f=fileService.dirBackups()
		def tmp= new TreeSet()
		f.eachFile(FileType.FILES){
			tmp+=it.path
		}
		dorestore(tmp.last())		
	}
	
	def loaddump={
		String dumpFile=fileService.dirDumps().path+"${File.separator}dump.sql"
		dorestore(dumpFile)
	}
	
	
	def dorestore(String file){
		def p=""""${mySqlHome}"${File.separator}${mysqlCmd} -e 'source $file' siragusa"""
		def f= PlatformUtils.windows?file.replaceAll('/','\\'):file
		def cmd=["${mySqlHome}${File.separator}mysql","--user=root", "--password=root","-e", "source $f","siragusa"]
		println cmd
		Process proc=cmd.execute()
		proc.waitFor()
		if (proc.exitValue())
			println proc.err.text
	}
	
	def export(String filename){
		//def p=fileService.executeCommand(""""${mySqlHome}${File.separator}${dumpCmd}""")
		def cmd=["${mySqlHome}${File.separator}${dumpCmd}","--user=root", "--password=root","siragusa" ]
		def proc=cmd.execute()
		new File(filename).write(proc.text);
	}

}
