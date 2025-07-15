package ComparatorModule;

import LibraryModule.Volume;
import Utility.Package;

@Package class RatingComparatorFactory implements VolumeComparatorFactory 
{
	@Override
	public VolumeComparator CreateComparator() 
	{
		return new RatingComparator();
	}
}

class RatingComparator implements VolumeComparator
{
	@Override
	public int compare(Volume v1, Volume v2) 
	{
		VolumeComparator.checkNull(v1, v2);
		
		return v1.GetRating() - v2.GetRating();
	}
}