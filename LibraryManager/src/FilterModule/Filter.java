package FilterModule;

import java.util.List;
import LibraryModule.Volume;

public interface Filter 
{
	// English is a very funny language...
	public List<Volume> Filter(List<Volume> list);
}
