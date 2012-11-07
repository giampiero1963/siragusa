package ellematica.graphics;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;

public class StringSelectionFilterator implements TextFilterator<SelectString> {

	@Override
	public void getFilterStrings(List<String> baseList, SelectString string) {
		baseList.add(string.value);
	}

}
