package ComparatorModule;

import java.util.Comparator;
import LibraryModule.Volume;

public interface VolumeComparator extends Comparator<Volume>
{
	// Peak laziness
	static void checkNull(Volume v1, Volume v2)
	{
		if (v1 == null)
		{
			throw new NullPointerException("Lvalue is null.");
		}
		if (v2 == null)
		{
			throw new NullPointerException("Rvalue is null.");
		}
	}
}