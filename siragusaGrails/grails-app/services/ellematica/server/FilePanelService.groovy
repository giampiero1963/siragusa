package ellematica.server;

import ellematica.graphics.*
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component


public class FilePanelService implements InitializingBean{

	@Override
	public void afterPropertiesSet() {
		filePanel.journalPanel=journalPanel
	}

	FilePanel filePanel
	def journalPanel

}
