package LibraryModule;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import ComparatorModule.VolumeComparator;
import FilterModule.Filter;
import Utility.Package;

public class Library 
{
	private List<Volume> 		library;
	private VolumeComparator 	comparator;
	private SortState 			sortState;
	
	public Library()
	{
		sortState 	= SortState.UNSORTED;
		comparator 	= null;
		library 	= new LinkedList<Volume>();
	}
	
	@Package void SetSortState(SortState _sortState)
	{
		sortState = _sortState;
	}
	
	/*
	 * Spent something like 10 mins writing a quicksort algorithm only to find out Java collections have a faster, better
	 * sort function built in...
	 * 
	 * Look up "Shinji sitting meme" to understand how I feel right now
	 * Don't even feel like deleting this
	 * 
	private List<Volume> quickSort(List<Volume> list)
	{
		// ASSUMES THAT THE COMPARATOR AND THE LIST ARE NOT NULL AND THAT THE LIST'S SIZE IS > 0
		
		if (list.size() <= 1)
		{
			return list;
		}
		else if (list.size() == 2)
		{
			if (comparator.compare(list.getFirst(), list.getLast()) > 0)
			{
				Volume v1 = list.getFirst();
				list.set(0, list.getLast());
				list.set(1, v1);
				return list;
			}
		}
		
		// Quicksort recursion
		
		int midpoint = list.size() / 2;
		List<Volume> lowerSubList = new LinkedList<Volume>();	// Could change constructor if I find a better data structure, unlikely though
		List<Volume> upperSubList = new LinkedList<Volume>();
		Volume midvolume = list.get(midpoint); // Again, I wish there was operator overloading (list[midpoint])
		
		for (Volume v : list)
		{
			if (comparator.compare(v, midvolume) <= 0)
			{
				lowerSubList.add(v);
				continue;
			}
			upperSubList.add(v);
		}
		
		lowerSubList = quickSort(lowerSubList);
		upperSubList = quickSort(upperSubList);
		
		lowerSubList.addAll(upperSubList);
		return lowerSubList;
	}*/
	
	public boolean Sort()
	{
		if (comparator == null || library == null)
		{
			return false;
		}
		
		library.sort(comparator);
		
		if (sortState == SortState.CRESCENT)
		{
			Collections.reverse(library);
			sortState = SortState.DECRESCENT;
		}
		else
		{
			sortState = SortState.CRESCENT;
		}
		
		return true;
	}
	
	public boolean AddVolume(Volume v)
	{
		if (library == null)
		{
			library = new LinkedList<Volume>();
			library.add(v);
			return true;
		}
		
		if (Contains(v))
		{
			return false;
		}
		
		library.add(v);
		return true;
	}
	
	public boolean Contains(Volume v)
	{
		if (library == null)
		{
			return false;
		}
		
		for (Volume vol : library)
		{
			if (v.isEquivalent(vol))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void Wipe()
	{
		if (library != null) library.clear();
	}
	
	public boolean Remove(
									String title,
									String author,
									String genre,
									String ISBN)
	{
		if (library == null)
		{
			return false;
		}
		
		Volume v = new Volume(title, author, genre, ISBN);
		
		int i = 0;
		for (Volume vol : library)
		{
			if (vol.isEquivalent(v))
			{
				library.remove(i);
			}
			i++;
		}
		
		return false;
	}
	
	@Package List<Volume> GetLibrary()
	{
		return library;
	}
	
	@Package void SetLibrary(List<Volume> newLib)
	{
		library = newLib;
	}
	
	/* Another missing feature from Java: friend classes...
	 * Let's just use C++ for everything... */
	@Package void SetComparator(VolumeComparator _comparator)
	{
		comparator = _comparator;
	}
	
	@Package VolumeComparator GetComparator()
	{
		return comparator;
	}
}

enum SortState
{
	UNSORTED,
	CRESCENT,
	DECRESCENT
}
