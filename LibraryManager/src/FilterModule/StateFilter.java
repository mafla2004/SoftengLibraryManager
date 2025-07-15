package FilterModule;

import java.util.List;
import java.util.LinkedList;
import LibraryModule.ReadingState;
import LibraryModule.Volume;
import Utility.Package;

@Package class StateFilter implements Filter 
{
	@Package ReadingState filter;
	
	public StateFilter(ReadingState _filter)
	{
		filter = _filter;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof StateFilter))
		{
			return false;
		}
		
		StateFilter sf = (StateFilter)o;
		
		return filter.equals(sf.filter);
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
			if (v.GetState() == filter)
			{
				ret.add(v);
			}
		}
		
		return ret;
	}
}

class StateFilterFactory implements FilterFactory
{
	@Override
	public Filter CreateFilter(String rule)
	{
		String token = rule.toLowerCase();
		ReadingState state;
		
		switch (token)
		{
		case "read":
			state = ReadingState.READ;
			break;
		case "reading":
			state = ReadingState.READING;
			break;
		case "to_be_read": case "to be read":
			state = ReadingState.TO_BE_READ;
			break;
		default:
			return null;
		}
		
		return new StateFilter(state);
	}
}