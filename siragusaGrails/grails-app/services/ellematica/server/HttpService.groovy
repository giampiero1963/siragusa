package ellematica.server

import grails.converters.JSON

 

import org.codehaus.groovy.grails.plugins.codecs.URLCodec
import org.springframework.stereotype.Component

@Component
class HttpService {

	def path = 'http://localhost:8080/siragusaGrails' 
	def initPhase=true;
	
	def post(String controller,String method,Object parameters=null ){
		if(initPhase) return "ok";
		
		def url = new URL ("$path/${controller}/${method}")
		def conn = url.openConnection()
		conn.requestMethod="POST"
		def data= parameters
		conn.doOutput=data?true:false
		conn.doInput=true
		if(data){
			Writer wr = new OutputStreamWriter(conn.outputStream)
			def st = [] 
			data.each{
				def val=URLCodec.encode(it.value)
				st.add( "${it.key}=${val}")
			}
			wr.write(st.join("&"))
			wr.flush()
			wr.close()
		}
		conn.connect()
		return conn.content.text
		
	}
	
	
}
