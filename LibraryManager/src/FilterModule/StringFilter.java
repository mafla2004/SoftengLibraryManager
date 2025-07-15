package FilterModule;

import java.util.List;
import LibraryModule.Volume;
import Utility.Package;

@Package abstract class StringFilter implements Filter 
{
	@Package String filter;
	
	private static String trim(String s) { return s.replaceAll("^\\s+|\\s+$", ""); }
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof StringFilter))
		{
			return false;
		}
		
		StringFilter sf = (StringFilter)o;
		
		return trim(filter.toLowerCase()).equals(trim(sf.filter.toLowerCase()));
	}
	
	public StringFilter(String _filter)
	{
		filter = _filter;
	}
}
