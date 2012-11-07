package ellematica.graphics;

import javax.swing.*;
import java.awt.*;

public class JUtils {
	
	static void alertBox(JOptionPane opt,String msg){
		opt.showMessageDialog(null, msg,msg, JOptionPane.WARNING_MESSAGE);
	
	}
	
	public static void fixFont( c,dim) {
		c.font=c.font.deriveFont(dim);
		def  components = c.components;
		components.each{ comp->
				if (comp instanceof Container) {
						fixFont(comp,dim);
				} else {
						comp.font=comp.font.deriveFont(dim);
				}
		}
}

}
