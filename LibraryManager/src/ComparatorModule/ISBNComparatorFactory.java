package ComparatorModule;

import LibraryModule.Volume;
import Utility.Package;

@Package class ISBNComparatorFactory implements VolumeComparatorFactory 
{
	@Override
	public VolumeComparator CreateComparator() 
	{
		// TODO Auto-generated method stub
		return null;
	}
}

class ISBNComparator implements VolumeComparator
{

	@Override
	public int compare(Volume v1, Volume v2) 
	{
		VolumeComparator.checkNull(v1, v2);
		
		return v1.GetISBN().compareTo(v2.GetISBN());
	}
	
}