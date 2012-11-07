package ellematica.graphics;

import java.util.Comparator;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

public class SelectStringTableFormat implements
		AdvancedTableFormat<SelectString> {
	public int getColumnCount() {
		return 1;
	}

	public String getColumnName(int column) {
	
		if (column == 0)
			return "";

		throw new IllegalStateException();
	}

	public Object getColumnValue(SelectString issue, int column) {

		//if (column == 0)
		//	return issue.isSelected();
		//else 
		if (column == 0)
			return issue.getValue();

		throw new IllegalStateException();
	}

	@Override
	public Class getColumnClass(int column) {

		//if (column == 0)
		//	return Boolean.class;
		//else 
		if (column ==0)
			return String.class;

		throw new IllegalStateException();
	}

	@Override
	public Comparator getColumnComparator(int column) {
	
	if (column == 1) {
			return new Comparator<String>() {
				@Override
				public int compare(String arg0, String arg1) {
					// TODO Auto-generated method stub
					return arg0.compareTo(arg1);
				};
			};
		}
		throw new IllegalStateException();

	}

	@Override
	public boolean isEditable(SelectString arg0, int arg1) {
		return false;
	}

	@Override
	public SelectString setColumnValue(SelectString arg0, Object arg1, int arg2) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
