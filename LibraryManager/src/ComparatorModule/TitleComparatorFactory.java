package ComparatorModule;

import LibraryModule.Volume;
import Utility.Package;

@Package class TitleComparatorFactory implements VolumeComparatorFactory
{
	@Override
	public VolumeComparator CreateComparator() 
	{
		return new TitleComparator();
	}
}

class TitleComparator implements VolumeComparator
{
	@Override
	public int compare(Volume v1, Volume v2) 
	{
		VolumeComparator.checkNull(v1, v2);
		
		return v1.GetTitle().compareTo(v2.GetTitle());
	}
}