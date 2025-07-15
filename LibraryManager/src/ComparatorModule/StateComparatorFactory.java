package ComparatorModule;

import LibraryModule.Volume;
import Utility.Package;

@Package class StateComparatorFactory implements VolumeComparatorFactory 
{
	@Override
	public VolumeComparator CreateComparator() 
	{
		return new StateComparator();
	}
}

class StateComparator implements VolumeComparator
{
	@Override
	public int compare(Volume v1, Volume v2) 
	{
		VolumeComparator.checkNull(v1, v2);
		
		return v1.GetState().compareTo(v2.GetState());
	}
}