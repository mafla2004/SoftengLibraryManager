package FilterModule;

import java.util.LinkedList;
import java.util.List;
import Utility.Package;
import LibraryModule.Volume;

@Package class ISBNFilter extends StringFilter 
{
	public ISBNFilter(String _filter) 
	{
		super(_filter);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!super.equals(o))
		{
			return false;
		}
		
		return o instanceof ISBNFilter;
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
			if (v.GetISBN().toLowerCase().contains(filter.toLowerCase()))
			{
				ret.add(v);
			}
		}
		
		return ret;
	}
}

class ISBNFilterFactory implements FilterFactory
{
	@Override
	public Filter CreateFilter(String rule)
	{
		return new ISBNFilter(rule);
	}
}