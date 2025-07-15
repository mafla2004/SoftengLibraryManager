package ComparatorModule;

import LibraryModule.Volume;
import Utility.Package;

@Package class AuthorComparatorFactory implements VolumeComparatorFactory 
{
	@Override
	public VolumeComparator CreateComparator() 
	{
		return new AuthorComparator();
	}
}

class AuthorComparator implements VolumeComparator
{
	@Override
	public int compare(Volume v1, Volume v2) 
	{
		VolumeComparator.checkNull(v1, v2);
		
		return v1.GetAuthor().compareTo(v2.GetAuthor());
	}
}