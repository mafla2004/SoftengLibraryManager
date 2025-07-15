package LibraryModule;

import Utility.Package;

public class Volume 
{
	private String 			title;
	private String 			author;
	private String 			genre;
	private String 			ISBN;
	private byte 			rating;
	private ReadingState 	state;
	
	public final String 		GetTitle() 			{ return title; }
	public final String 		GetAuthor() 		{ return author; }
	public final String 		GetGenre() 			{ return genre; }
	public final String 		GetISBN() 			{ return ISBN; }
	public final byte			GetRating()			{ return rating; }
	public final ReadingState	GetState()			{ return state;}
	
	@Package final void		SetTitle(String _title) 	{ title = _title; }
	@Package final void 	SetAuthor(String _author) 	{ author = _author; }
	@Package final void		SetGenre(String _genre) 	{ genre = _genre; }
	@Package final void 	SetISBN(String _ISBN) 		{ if (isValidISBN(_ISBN)) ISBN = _ISBN; }
	@Package final void 	SetRating(byte _rating)		{ if (isValidRating(_rating)) rating = _rating; }
	@Package final void		SetState(ReadingState stt)	{ state = stt;}
	
	static final byte 	SHORT_ISBN 	= 10;
	static final byte 	LONG_ISBN 	= 13;
	public static final byte UNINITIALIZED_RATING = -1;	// JAVA BYTES ARE SIGNED?!
	
	static boolean isValidRating(byte rating)
	{
		return rating >= 0 && rating <= 5;
	}
	
	static boolean isValidISBN(String ISBN)
	{
		if (ISBN.length() != SHORT_ISBN && ISBN.length() != LONG_ISBN)
		{
			return false;
		}
		
		for (char c : ISBN.toCharArray())
		{
			if (!Character.isDigit(c))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public Volume(
				String 	_title, 
				String 	_author, 
				String 	_genre,
				String 	_ISBN)
	{
		if (!isValidISBN(_ISBN))
		{
			throw new IllegalArgumentException("Invalid ISBN code entered");
		}
		
		title 	= _title;
		author 	= _author;
		genre 	= _genre;
		ISBN 	= _ISBN;
		rating 	= 0;
		state	= ReadingState.TO_BE_READ;
	}
	
	public Volume(
			String 			_title, 
			String 			_author, 
			String 			_genre,
			String 			_ISBN,
			byte 			_rating,
			ReadingState 	_state)
	{
		if (!isValidISBN(_ISBN))
		{
			throw new IllegalArgumentException("Invalid ISBN code entered");
		}
	
		title 	= _title;
		author 	= _author;
		genre 	= _genre;
		ISBN 	= _ISBN;
		rating	= _rating;
		state	= _state;
	}
	
	public Volume() 
	{
		title	= null;
		author	= null;
		genre	= null;
		ISBN	= null;
		rating	= UNINITIALIZED_RATING;
		state	= null;
	}
	
	// Maybe add copy constructor?
	
	@Override
	public String toString()
	{
		String ret = 	"TITLE: " + title + 
						"; AUTHOR: " + author +
						"; GENRE: " + genre +
						"; ISBN CODE: " + ISBN;
		return ret;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Volume))
		{
			return false;
		}
		
		Volume v = (Volume)o;
		
		if (!title.equals(v.title) || !author.equals(v.author) ||	// I wish there was operator overloading...
			!genre.equals(v.genre) || !ISBN.equals(v.ISBN) ||
			!state.equals(v.state) || rating != v.rating)
		{
			return false;
		}
		return true;
	}
	
	public boolean isEquivalent(Volume v)
	{
		/* Useful to check if two volumes can be considered equivalent despite not being equal.
		 * E.G. if we have two volumes named 1984 of dystopian genre, written by George Orwell and with
		 * ISBN code 0000002048, but they have different reading states and/or rating, 
		 * the computer shouldn't consider them equal, even though they are equivalent volumes */
		if (!title.equals(v.title) || !author.equals(v.author) ||
			!genre.equals(v.genre) || !ISBN.equals(v.ISBN))
			{
				return false;
			}
			return true;
	}
	
	@Override
	public int hashCode()
	{
		return title.hashCode() + 3 * author.hashCode() - 5 * genre.hashCode() + 7 * ISBN.hashCode();
	}
	
	
}
