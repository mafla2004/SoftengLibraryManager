package FilterModule;

import java.util.List;
import java.util.LinkedList;
import LibraryModule.Volume;
import Utility.Package;

@Package class RatingFilter implements Filter 
{
	@Package byte filter;
	
	public RatingFilter(byte _filter)
	{
		filter = _filter;
	}
	
	@Override
	public List<Volume> Filter(List<Volume> list) 
	{
		if (list == null)
		{
			return null;
		}
		
		List<Volume> ret = new LinkedList<Volume>();
		
		for (Volume v : list)
		{
			if (v.GetRating() == filter)
			{
				ret.add(v);
			}
		}
		
		return ret;
	}

}

class RatingFilterFactory implements FilterFactory
{
	private static String trim(String s) { return s.replaceAll("^\\s+|\\s+$", ""); }
	
	@Override
	public Filter CreateFilter(String rule) 
	{
		String trimmed = trim(rule);
		
		if (trimmed.length() != 1)
		{
			return null;
		}
		
		if (!Character.isDigit(trimmed.charAt(0)))
		{
			return null;
		}
		
		byte val = (byte) Character.getNumericValue(trimmed.charAt(0));
		if (val < 0 || val > 5)
		{
			return null;
		}
		
		return new RatingFilter(val);
	}
	
}