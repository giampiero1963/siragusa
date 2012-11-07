package ellematica.graphics
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode as TreeNode


import ellematica.server.HttpService
import grails.converters.JSON
import groovy.swing.*
import ca.odell.glazedlists.*
import ca.odell.glazedlists.swing.*
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator
import org.springframework.beans.factory.InitializingBean
import javax.swing.WindowConstants as WC
import com.toedter.calendar.*
import static java.util.Calendar.YEAR
import static java.awt.FlowLayout.*
import static java.awt.GridBagConstraints.*
import static java.awt.ComponentOrientation.*
import java.awt.*
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.springframework.stereotype.Component

@Component
class MainView implements InitializingBean{
	HttpService httpService

	@Override
	public void afterPropertiesSet() {
		view()
		Thread.start{
			waitCursor()
			filePanel.refresh()
			resetCursor()
		}
	}


	def swing 

	def documentLabel
	def controlloPanel
	FiltroPanel filtriPanel
	def spuntaPanel
	def filePanel
	def journalPanel
	ClientiFornitoriPanel clientiFornitoriPanel
	def totaliPanel
	def fileSearchPanel
	AnagraficaPanel anagraficaPanel
	
	
	def filePath={System.env['HOME']?:System.env['HOMEPATH']}
	def fileName="graphConf.conf" 

	def file(){
		def path=filePath()+"/.siragusa"
		def dir=new File(path)
		dir.mkdir();
		new File(path+"/${fileName}")
	}
	
	def tables
	
	def saveGraph(){
		
		
		def tosave=[:];
		tosave.main=[size:[width:swing.main.size.width,height:swing.main.size.height],
			location:[x:swing.main.location.x,y:swing.main.location.y]]
		tosave.split_main=[dividerLocation:swing.split_main.dividerLocation]
		tosave.split_in=[dividerLocation:swing.split_in.dividerLocation]
		
		

		tables.each{table-> 
			def columns= table.table.columnModel.columns
			def widths=columns*.width
			tosave[table.name]=[widths:widths]
		}
		
		def json=new JSON(tosave) 
		File file=file();
		def writer=new FileWriter(file)
		writer.write(json.toString())
		writer.flush()
		
	}
	
	def loadGraph(){
		File f=file();
		try{
			if(f.exists()){
				def slurper = new JSON()
				def saved = slurper.parse(new FileReader(f))
				swing.main.size=new Dimension(saved.main.size.width as int,saved.main.size.height as int)
				swing.main.location=[saved.main.location.x as int,saved.main.location.y as int]
				swing.split_main.dividerLocation=saved.split_main.dividerLocation
				swing.split_in.dividerLocation=saved.split_in.dividerLocation
			
				tables.each{table->
					def columns= table.table.columnModel.columns
					def i=0
					def widths=saved[table.name].widths
					
					columns.each{column->
						column.preferredWidth=widths[i++]
					}
				}
			}
		}
		catch(e){
			e.printStackTrace()
		}
	
	}

	def confirm(msg){
		def response = swing.optionPane().showConfirmDialog(null, msg, "Confirm",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			return response == JOptionPane.YES_OPTION
		
	}

	Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	
	Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	def waitCursor(){
		swing.main.cursor=waitCursor
	}
	def resetCursor(){
		swing.main.cursor=defCursor
	}
	
	def backup(){
		waitCursor()
		httpService.post("backup","backup")
		resetCursor()
	}
	def restore(){
		if(confirm("Conferma il caricamento?")) {
			waitCursor()			
			httpService.post("backup","restore")
			resetCursor()
			
		}
	}
	def reports(){
		waitCursor()
		filtriPanel.report()
		resetCursor()
	}
	
	def statistics(){
		waitCursor()
		filtriPanel.statistics()
		resetCursor()
	}

	def exit(){
		if(confirm("Conferma l'uscita?"))System.exit(0)
	}
	
	def admin(){
		waitCursor()
		["explorer","http://localhost:8080/siragusaGrails/"].execute()
		resetCursor()
	}
	
	def view={

		swing.frame(id:"main",title: 'Main', defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
				size: [1024, 768], show: true, locationRelativeTo: null) {
					lookAndFeel("system")
					menuBar() {
						menu(text: "File", mnemonic: 'F') {
							menuItem(text:"Admin",mnemonic:'A',actionPerformed:{admin()})
							menuItem(text: "Backup", mnemonic: 'B', actionPerformed: { backup() })
							menuItem(text: "Restore", mnemonic: 'R', actionPerformed: { restore() })
							menuItem(text: "Exit", mnemonic: 'X', actionPerformed: { exit() })
						}
						menu(text: "Graphix", mnemonic: 'G') {
							menuItem(text: "Save", mnemonic: 'S', actionPerformed: { saveGraph() })
							menuItem(text: "Load", mnemonic: 'L', actionPerformed: { loadGraph() })
						}
						menu(text: "Statistics/Reports", mnemonic: 'G') {
							menuItem(text: "Reports", mnemonic: 'R', actionPerformed: { reports() })
							menuItem(text: "Statistics", mnemonic: 'S', actionPerformed: {statistics()})
						}
					}

					splitPane(id:"split_main",dividerLocation:300) {
						def pan=scrollPane(constraints: "left",background:Color.WHITE){  
							documentLabel=label(id:"lbl_document")
						}
						doubleClick(pan)
						splitPane(id:"split_in",orientation:JSplitPane.VERTICAL_SPLIT, dividerLocation:280) {
							splitPane(constraints: "top") {
								boxLayout(axis:BoxLayout.X_AXIS)
								leftPanel()
								rightPanel()								
							}
							
							init(journalPanel,"bottom")
							
						}
					}
				}
				
				afterView()

	}
	
	
	def afterView(){
		tables=[
			[name:"lst_cust",table:swing.lst_cust],
			[name:"lst_supp",table:swing.lst_supp],
			[name:"lst_journal",table:swing.lst_journal],
			[name:"search_file",table:swing.src_table]
		]
		controlloPanel.afterView() 
		clientiFornitoriPanel.all=true
		

	}
	

	
	
	def init(obj,constr=null){
		if(constr)obj.constraints=constr
		obj.init()
	}
	def rightPanel(){
		def pane=swing.panel(){
			springLayout()
			//init(spuntaPanel,BorderLayout.PAGE_START)
			init(anagraficaPanel)
			init(filePanel)
			
		}
		SpringLayoutUtils.makeCompactGrid(pane, pane.componentCount, 1, 2,2, 2, 2)
	}
	def leftPanel(){
		swing.panel(){
			boxLayout(axis:BoxLayout.X_AXIS)
			controlAndCustomersPanel()
			filterAndSearchPanel()
		}
	}
	def controlAndCustomersPanel(){
		def pan=swing.panel(){
			borderLayout()
			init(controlloPanel,BorderLayout.PAGE_START)
			init(clientiFornitoriPanel,BorderLayout.CENTER)		
		}
	
		
	}
	def filterAndSearchPanel(){
		def pan=swing.panel(){
			borderLayout()
			init(filtriPanel,BorderLayout.PAGE_START)
			init(fileSearchPanel,BorderLayout.CENTER)
		}
		
	}
	
	def optionPane
	def initData(){
		anagraficaPanel.initData()
		optionPane=swing.optionPane()
		clientiFornitoriPanel.all=true
	}
	
	
	def alert(msg){
		optionPane.showMessageDialog(null, msg,"Attenzione!",JOptionPane.WARNING_MESSAGE)
		
	}
	
	def message(msg){
		optionPane.showMessageDialog(null, msg,"Attenzione!",JOptionPane.INFORMATION_MESSAGE)
	}
	
	
	def doubleClick( panel){
		panel.addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent e){
							if (e.component.enabled && e.button == MouseEvent.BUTTON1 && e.clickCount == 2){
								filePanel.openCurrentShowingFile()
									
							}
					}
			})
	}
}
