package ComparatorModule;

import LibraryModule.VolumeField;

//import LibraryModule.LibraryMediator;

public class ComparatorMediator 
{
	//private LibraryMediator libInterface;
	
	public VolumeComparator FetchComparator(VolumeField logic)
	{
		VolumeComparatorFactory factory;
		switch (logic)
		{
		case TITLE:
			factory = new TitleComparatorFactory();
			break;
		case AUTHOR:
			factory = new AuthorComparatorFactory();
			break;
		case GENRE:
			factory = new GenreComparatorFactory();
			break;
		case ISBN:
			// Who sorts by ISBN?
			factory = new ISBNComparatorFactory();
			break;
		case RATING:
			factory = new RatingComparatorFactory();
			break;
		case READING_STATE:
			factory = new StateComparatorFactory();
			break;
		default:
			return null;
		}
		
		return factory.CreateComparator();
	}
}
