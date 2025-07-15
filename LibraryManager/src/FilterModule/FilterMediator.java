package FilterModule;

import LibraryModule.VolumeField;

public class FilterMediator 
{
	public Filter FetchFilter(VolumeField criteria, String rule)
	{
		FilterFactory factory;
		
		switch (criteria)
		{
		case TITLE:
			factory = new TitleFilterFactory();
			break;
		case AUTHOR:
			factory = new AuthorFilterFactory();
			break;
		case GENRE:
			factory = new GenreFilterFactory();
			break;
		case ISBN:
			factory = new ISBNFilterFactory();
			break;
		case RATING:
			factory = new RatingFilterFactory();
			break;
		case READING_STATE:
			factory = new StateFilterFactory();
			break;
		default:
			return null;
		}
		
		return factory.CreateFilter(rule);
	}
}
