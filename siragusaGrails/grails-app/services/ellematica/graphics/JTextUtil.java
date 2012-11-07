package ellematica.graphics;

import javax.swing.JFormattedTextField;

public class JTextUtil {
	static public void firePropertyChange(JFormattedTextField object,String prop,Double old, Double val){
		object.firePropertyChange(prop,old.doubleValue(),val.doubleValue());
	}

}
