package UserInterface;

import Utility.Package;
import LibraryModule.LibraryMediator;
import LibraryModule.ReadingState;
import LibraryModule.VolumeField;

public class SystemMediator 
{
	private LibraryMediator libInterface;
	
	@Package boolean SortLibrary(VolumeField logic)
	{
		if (libInterface == null)
		{
			return false;
		}
		
		return libInterface.SortTitles(logic);
	}
	
	@Package void WipeLibrary()
	{
		if (libInterface == null)
		{
			return;
		}
		
		libInterface.WipeLibrary();
	}
	
	@Package boolean SaveLibrary()
	{
		if (libInterface == null)
		{
			return false;
		}
		
		return libInterface.SaveLibrary();
	}
	
	@Package boolean LoadLibrary()
	{
		if (libInterface == null)
		{
			return false;
		}
		
		return libInterface.LoadLibrary();
	}
	
	@Package boolean AddVolume(String volume, String author, String genre, String ISBN, byte rating, ReadingState state)
	{
		if (libInterface == null)
		{
			return false;
		}
		
		return libInterface.AddVolume(volume, author, genre, ISBN, rating, state);
	}
	
	@Package boolean RemoveVolume(String volume, String author, String genre, String ISBN)
	{
		if (libInterface == null)
		{
			return false;
		}
		
		return libInterface.RemoveVolume(volume, author, genre, ISBN);
	}
	
	@Package SystemMediator() // Kicks off initialization of the system
	{
		
	}
}
