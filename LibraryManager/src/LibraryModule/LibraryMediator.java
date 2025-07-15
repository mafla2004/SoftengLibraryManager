package LibraryModule;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import ComparatorModule.*;
import StorageModule.*;
import FilterModule.*;

public enum LibraryMediator 
{
	INSTANCE;
	
	private Library lib;
	private ComparatorMediator compInterface;
	private StorageHandler strgInterface;
	private FilterMediator fltrInterface;
	private Parser parser = new Parser();
	private List<Filter> filters = new LinkedList<Filter>();
	private boolean initialized = false;
	
	public boolean SortTitles(VolumeField logic)
	{
		if (lib == null)
		{
			return false;
		}
		
		VolumeComparator comp = compInterface.FetchComparator(logic);
		
		if (lib.GetComparator() == null || !comp.getClass().equals(lib.GetComparator().getClass()))
		{
			lib.SetSortState(SortState.UNSORTED);
		}
		
		lib.SetComparator(comp);
		return lib.Sort();
	}
	
	public boolean SaveLibrary()
	{
		if (lib == null)
		{
			return false;
		}
		
		String jsonRepr = parser.ParseLibrary(lib.GetLibrary());
		
		return strgInterface.SaveLibrary(jsonRepr);
	}
	
	public boolean LoadLibrary()
	{
		if (lib == null)
		{
			return false;
		}
		
		String jsonRepr = strgInterface.ReadLibrary();
		try 
		{
			lib.SetLibrary(parser.ParseJson(jsonRepr));
		} 
		catch (InvalidJsonSequenceException | InvalidJsonEntryException | JsonParsingError e) 
		{
			return false;
		}
		
		return true;
	}
	
	public boolean isInitialized()
	{
		return initialized;
	}
	
	public boolean AddVolume(String title, String author, String genre, String ISBN, byte rating, ReadingState state)
	{
		try
		{
			Volume v = new Volume(title, author, genre, ISBN, rating, state);
			return lib.AddVolume(v);
		}
		catch (IllegalArgumentException e)
		{
			return false;
		}
		
	}
	
	public boolean ModifyVolume(Volume v, String title, String author, String genre, String ISBN, byte rating, ReadingState state)
	{
		if (v == null)
		{
			return false;
		}
		
		Volume selectedVol = null;
		for (Volume vol : lib.GetLibrary())
		{
			if (vol.isEquivalent(v))
			{
				selectedVol = vol;
				break;
			}
		}
		
		if (selectedVol == null)
		{
			return false;
		}
		
		if (title != null && Parser.trim(title).length() != 0)
		{
			selectedVol.SetTitle(title);
		}
		
		if (author != null && Parser.trim(author).length() != 0)
		{
			selectedVol.SetAuthor(author);
		}
		
		if (genre != null && Parser.trim(genre).length() != 0)
		{
			selectedVol.SetGenre(genre);
		}
		
		if (ISBN != null && Parser.trim(ISBN).length() != 0)
		{
			selectedVol.SetISBN(ISBN);
		}
		
		if (rating != Volume.UNINITIALIZED_RATING)
		{
			selectedVol.SetRating(rating);
		}
		
		if (state != null)
		{
			selectedVol.SetState(state);
		}
		
		return true;
	}
	
	public boolean RemoveVolume(String title, String author, String genre, String ISBN)
	{
		return lib.Remove(title, author, genre, ISBN);
	}
	
	public void WipeLibrary()
	{
		lib.Wipe();
	}
	
	public List<Volume> RetrieveLibrary()
	{
		return ApplyFilters();
	}
	
	private List<Volume> ApplyFilters()
	{
		List<Volume> ret = (List<Volume>) ((LinkedList<Volume>)lib.GetLibrary()).clone();
		
		for (Filter f : filters)
		{
			ret = f.Filter(ret);
		}
		
		return ret;
	}
	
	public boolean AddFilter(VolumeField criteria, String rule)
	{
		if (filters == null)
		{
			filters = new LinkedList<Filter>();
		}
		
		if (rule == null || criteria == null)
		{
			return false;
		}
		
		Filter f = fltrInterface.FetchFilter(criteria, rule);
		
		if (f == null)
		{
			return false;
		}
		
		return filters.add(f);
	}
	
	public boolean RemoveFilter(VolumeField criteria, String rule)
	{
		if (filters == null || criteria == null || rule == null)
		{
			return false;
		}
		
		Filter f = fltrInterface.FetchFilter(criteria, rule);
		
		if (f == null)
		{
			return false;
		}
		
		return filters.remove(f);
	}
	
	public void ClearFilters()
	{
		filters.clear();
	}
	
	public boolean Initialize() throws FileNotFoundException, NoPathFoundException
	{
		if (initialized)
		{
			return false;
		}
		
		strgInterface	= new StorageHandler();		// TODO: handle path related logic
		lib 			= new Library();
		compInterface 	= new ComparatorMediator();
		fltrInterface	= new FilterMediator();
		initialized 	= true;
		
		return LoadLibrary();
	}
	
	public void ReinitializeStorage() throws FileNotFoundException, NoPathFoundException
	{
		strgInterface 	= new StorageHandler();
	}
}
