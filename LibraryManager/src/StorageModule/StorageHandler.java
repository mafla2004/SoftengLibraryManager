package StorageModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.Collection;
import java.lang.StringBuilder;
import LibraryModule.LibraryMediator;

public class StorageHandler 
{
	private String path;
	private File libraryFile;
	
	public static final String PATH_FILE_LOCATION = "../LibraryManager/src/Path.pth";
	public static final int FILE_PREDICTED_SIZE = 2048; // 2 kB
	public static final int PREDICTED_PATH_LENGTH = 128;
	
	public String ReadLibrary()
	{	
		try 
		{
			if (!libraryFile.exists())
			{
				return null;
			}
			
			FileReader reader = new FileReader(libraryFile);
			StringBuilder libBuilder = new StringBuilder(FILE_PREDICTED_SIZE);
			int i;
			try 
			{
				while ((i = reader.read()) != -1)
				{
					libBuilder.append((char)i);
				}
				
				reader.close();
				
				return libBuilder.toString();
			} 
			catch (IOException e) 
			{
				// TODO something
				e.printStackTrace();
				try 
				{
					reader.close();
				} 
				catch (IOException e1) 
				{
					// From IOException to IOInception in one function call
					e1.printStackTrace();
				}
				return null;
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean SaveLibrary(String json)	// Write all contents of library on Disk
	{
		
		try 
		{
			// Since we'll use this to save the whole library, we delete the entire contents of the file to rewrite everything.
			// I thought it was lazy at first, but then I found it's the norm, in which case don't mind if I do, saved time and headaches.
			if (libraryFile.exists())
			{
				libraryFile.delete();
			}
			libraryFile.createNewFile();
			
			FileWriter writer = new FileWriter(libraryFile);
			writer.write(json);
			writer.close();
			
			return true;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean hasFile()
	{
		if (libraryFile == null)
		{
			return false;
		}
		
		return libraryFile.exists();
	}

	public StorageHandler() throws FileNotFoundException, NoPathFoundException 
	{
		File pathFile = new File(PATH_FILE_LOCATION);
		
		if (!pathFile.exists())
		{
			throw new FileNotFoundException("Path File does not exist");
		}
		
		System.out.println("Path file found");
		FileReader pathReader = new FileReader(pathFile);
		StringBuilder pathBuilder = new StringBuilder();
		
		int i;
		try 
		{
			while ((i = pathReader.read()) != -1)
			{
				pathBuilder.append((char)i);
			}
			
			pathReader.close();
			
			String _path = pathBuilder.toString();
			if (_path.length() == 0)
			{
				throw new NoPathFoundException("No path was found in Path file");
			}
			path = _path;
			
			libraryFile = new File(path + "\\Library.json");
			if (!libraryFile.exists())
			{
				System.out.println("Path: " + path);
				libraryFile.createNewFile();
				System.out.println("File was created");
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
