package ComparatorModule;

import LibraryModule.Volume;
import Utility.Package;

@Package class GenreComparatorFactory implements VolumeComparatorFactory
{
	@Override
	public VolumeComparator CreateComparator() 
	{
		return new GenreComparator();
	}	
}

class GenreComparator implements VolumeComparator
{
	@Override
	public int compare(Volume v1, Volume v2) 
	{
		VolumeComparator.checkNull(v1, v2);
		
		return v1.GetGenre().compareTo(v2.GetGenre());
	}
}