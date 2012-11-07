package ellematica.server
import ellematica.graphics.DocumentValues
import groovy.io.FileType
import java.util.regex.Matcher
import org.springframework.stereotype .*

import java.util.regex.Pattern
import java.io.File;
import java.lang.annotation.Retention;

import org.apache.pdfbox.ExtractText
import org.artofsolving.jodconverter.OfficeDocumentConverter
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration
import org.artofsolving.jodconverter.office.OfficeConnectionProtocol
import org.artofsolving.jodconverter.office.OfficeManager
import org.artofsolving.jodconverter.util.PlatformUtils
import org.apache.commons.io.FileUtils
import ellematica.graphics.ClientiFornitoriPanel


import org.springframework.stereotype.Component

@Component
class FileService extends Observable{
	OfficeService officeService
	def convertPath=PlatformUtils.windows?System.env.CONVERT_PATH:"/opt/local/bin"
	def reportTemplate="reports.xls"
	def home=System.env['HOME']?:"C:"	

	File filePath(String path){
		def ff="${home}${File.separator}${path}"
		def dir=new File(ff)
		dir.mkdir()
		dir
		}
	def File dirRepository(){filePath("siragusa${File.separator}repository")}
	def File dirTxt(){filePath("siragusa${File.separator}txt")}
	def File dirUpload(){filePath("siragusa${File.separator}upload")}
	def File dirImages(){filePath("siragusa${File.separator}images")}
	def File dirTmp(){filePath("siragusa${File.separator}tmp")}
	def File dirTemplates(){filePath("siragusa${File.separator}templates")}
	def File dirReports(){filePath("siragusa${File.separator}reports")}
	def File dirDumps(){filePath("siragusa${File.separator}dumps")}
	def File dirBackups(){filePath("siragusa${File.separator}backups")}
	def File dirSend(){filePath("siragusa${File.separator}send")}

	
	
	def Process convertPdfToImage(dirfrom,src){
		if(convertToImage(dirfrom,"pdf",src)){
			copyToRepository(src)
			def txt=dest("upload","txt","pdf","txt",src)
			String[] a=[src,txt]
			ExtractText.main(a)
			def theTxt=dest("txt","txt","txt","pdf",txt)
			new File(txt).renameTo(theTxt)
		}
	}
	def Process convertTiffToImage(dirfrom,src){
		if(convertToImage(dirfrom,"tiff",src)){
			copyToRepository(src)
		}
	}
	
	def dest(dirfrom,dirto,ext,extto,src){
		def dest= (src  =~ /${ext}/).replaceFirst(extto)
		(dest =~ /${dirfrom}/).replaceFirst("${dirto}")
	}
	
	def executeCommand(cmd){
		if(PlatformUtils.windows){
			cmd=cmd.replace('/', '\\')
		}else{
			cmd=cmd.replace('"','')
		}
		println("executing $cmd")
		cmd.execute();
	}
	
	def  convertToImage(dirfrom,ext,src){
	
	
		def dest=dest(dirfrom,"images",ext,"png",src)
		if(test(src,dest)) return null;
		
		def cmd = """"$convertPath${File.separator}convert" "${src}" -append -background white "${dest}" """
		executeCommand(cmd);
	}
	
	
	
	def refresh(){
		
		def upload=dirUpload()
		def names=[]
		["tiff","pdf","xls","jpeg","jpg","xlsx"].each{ ext->
			upload.eachFileMatch(~/.*\.${ext}/){
				def newName=it.path.replaceAll(' ','_')
				it.renameTo(newName)
				names+=(new File(newName)).name
			}
		}
		refreshPdf()
		refreshExcel()
		refreshTiff()
		refreshJpg()
		
		names.sort{a,b -> b <=>a}	
		names
		
	}
	
	def imageFileName(String file){
		def src=dirUpload().path+File.separator+"$file"
		def ext= file.substring(file.lastIndexOf('.')+1) 
		if(ext=="jpeg" || ext=="jpg"){
			return src
		}
		dest("upload","images",ext,"png",src)
		
	}
	
	def refreshPdf(){	
		def pdf = ~/.*\.pdf/
		Process lastProcess
		dirUpload().eachFileMatch(pdf){
			def p=convertPdfToImage("upload",it.path)
			lastProcess= p?p:lastProcess;
		}
		lastProcess?.waitFor();		
	}
	
	def refreshJpg(){
		def jpg = ~/.*\.jpe?g/
		dirUpload().eachFileMatch(jpg){
			def dest=(it =~ /upload/).replaceFirst("repository")
			if(test(it.path,dest)){
				copyToRepository(it)
			}
		}
	}
	


	public FileService(){
		def home=filePath("siragusa")
		dirUpload()
		dirImages()
		dirTmp()
		dirTxt()
		dirRepository()
		dirTemplates()
		dirReports()
	
	}
	

	def getInfo(file){
		if(file =~/.*\.xls/)
			officeService.startExcelValuesTask(dirUpload().path+"${File.separator}$file")
		else{
			def val=new DocumentValues()
			val.filename=file
			setChanged()
			notifyObservers(val)
		}	
	}
	
	def removeFile(file){
		[new File(dirUpload().path+"${File.separator}$file"), new File(imageFileName(file))].each {
			it.delete()
		}
	}
	
	def fileUploaded(file){
		[new File(dirUpload().path+"${File.separator}$file")].each {
			it.delete()
		}
	}
	
	def test(src,dest){
		File s=new File(src)
		File d= new File(dest)
		return (d.exists() && (s.lastModified()< d.lastModified()) )
	
	}
	
	def copyToRepository(String src){
		dirRepository()
		def dest=(src =~ /upload/).replaceFirst("repository")
		FileUtils.copyFile(new File(src), new File(dest))
	}
	
	def excelExt=['xls','xlsx']
	
	def convertExcelToImage(String src){
		def ext= fileExt(src)
		def thedest=dest("upload","images",ext,"png",src)
		if(test(src,thedest)) return null;
		copyToRepository(src)
		
		def tmpdest=dest("upload","tmp",ext,"pdf",src)
		officeService.convert(src,tmpdest)
		def txt=dest("upload","txt",ext,"csv",src)
		officeService.convert(src,txt)
		def theTxt=dest("txt","txt","csv",ext,txt)
		new File(txt).renameTo(theTxt)

		convertToImage("tmp","pdf",tmpdest)
	}
	
	def refreshExcel(){
		def xls = ~/.*\.xlsx?/
		Process lastProcess
		dirUpload().eachFileMatch(xls){
			def p=convertExcelToImage(it.path)
			lastProcess=p?p:lastProcess
		}
		lastProcess?.waitFor()
	}
	
	def refreshTiff(){
		def xls = ~/.*\.tiff/
		Process lastProcess
		dirUpload().eachFileMatch(xls){
			def p=convertTiffToImage("upload",it.path)
			lastProcess=p?p:lastProcess
		}
		lastProcess?.waitFor()
	}
	
	
	

	
	
	def filteredFileList(filters,fileList){
		def ff=filters
		def all=ClientiFornitoriPanel.all
		if(ff.cust==all && ff.supp == all) return fileList
		if(ff.cust==[] || ff.supp == []) return []
		
		if(ff.cust==all) return fileList.findAll { file->
				ff.supp.find{ c->
					file =~ /${c}/
				}
			}
		if(ff.supp==all) return fileList.findAll { file->
			ff.cust.find{ c->
				file =~ /${c}/
			}
		}
		fileList.findAll { file->
			ff.cust.find{ c->
				if(file =~ /${c}/){
					ff.supp.find{ cc->
						file =~ /${cc}/
					}
				}
			}
		}
		
	}
	
	def isFileOk(file,filters){
		def ff=filters
		def all=ClientiFornitoriPanel.all
		if(ff.cust==all && ff.supp == all) return true
		if(ff.cust==[] || ff.supp == []) return false
		
		if(ff.cust==all) return ff.supp.find{ c-> file =~ /${c}/}
			
		if(ff.supp==all) return ff.cust.find{ c->file =~ /${c}/}
		
		ff.cust.find{ c->
			if(file =~ /${c}/){
				ff.supp.find{ cc->
					file =~ /${cc}/
				}
			}
		}
		

		
	}
	
	
	def search(String text,filters){
		def dir=dirTxt() 
		def ret=[]
		def p= ~/(?i)${text}/		
		dir.eachFile(FileType.FILES){ File file ->
			if(isFileOk(file.name,filters) && (file.text =~ p)){ 
				ret+=[filename:file.name,date:new Date(file.lastModified())]
			}
		}
		return (ret.sort{ a,b-> b.date <=> a.date})
	}
	
	def open(file,dir=null){
		dir=dir?:dirRepository()
		def cmd="$dir${File.separator}$file"
		
		if(PlatformUtils.windows){
			["cmd","/c",cmd].execute()
		}else{
			println cmd 
		}
	}
	
	
	def report(data){
		String templateFile=dirTemplates().path+"${File.separator}reports.xls"
		Date now=new Date()
		String outputFile=dirReports().path+"${File.separator}report.xls"
		officeService.report(templateFile,outputFile, data)
		open("report.xls",dirReports())
	}
	
	def statistics(data){
		String templateFile=dirTemplates().path+"${File.separator}statistics.xls"
		Date now=new Date()
		String outputFile=dirReports().path+"${File.separator}stat.xls"
		officeService.statistics(templateFile,outputFile, data)
		open("stat.xls",dirReports())
	}
	
	def zip(){
		def ant = new AntBuilder()
		new File("${home}${File.separator}backup").mkdir()
		ant.zip(
				destfile: "${home}${File.separator}backup${File.separator}dump.zip",
				basedir: "${home}${File.separator}siragusa",
				level: 9,
				excludes: "**/tmp/**, **/reports/**"
		)
	}
	def unzip(){
		File f=new File("${home}${File.separator}backup${File.separator}dump.zip")
		if(! f.exists()) return false
		def ant = new AntBuilder()
		ant.unzip(  src:f.path,
            dest:"${home}${File.separator}siragusa",
            overwrite:"true" )
		return true
	}
	
	def fileExt(name){
		def ret=name.find(~/\.[^\.]+$/)
		ret?ret[1..-1]:name		
	}
	
	public static void main(String[] args){
		FileService f=new FileService();
		def ret=f.open("PI_ITS_ART.xls")
		println "PI_ITS_ART.xls".find(~/\.[^\.]+$/)
		
	}
	
	def test(){
		FileService f=new FileService();
		def ret=f.open("PI_ITS_ART.xls")
		
	}
	
	def sendTo(files){
		File p=dirSend()
		p.eachFile{ File f->
			f.delete()
		}
		File r=dirRepository()
		files.each { 
			def src=r.path+File.separator+it
			def dest=p.path+File.separator+it
			FileUtils.copyFile(new File(src), new File(dest))
		}
	}


}
