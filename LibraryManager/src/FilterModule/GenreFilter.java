package FilterModule;

import java.util.List;
import java.util.LinkedList;
import LibraryModule.Volume;
import Utility.Package;

@Package class GenreFilter extends StringFilter 
{
	public GenreFilter(String _filter) 
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
		
		return o instanceof GenreFilter;
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
			if (v.GetGenre().toLowerCase().contains(filter.toLowerCase()))
			{
				ret.add(v);
			}
		}
		
		return ret;
	}
}

class GenreFilterFactory implements FilterFactory
{
	@Override
	public Filter CreateFilter(String rule)
	{
		return new GenreFilter(rule);
	}
}