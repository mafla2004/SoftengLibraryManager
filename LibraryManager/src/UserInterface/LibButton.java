package UserInterface;

import java.awt.*;
import javax.swing.*;
import LibraryModule.Volume;
import Utility.Package;

@SuppressWarnings("serial")
@Package class LibButton extends ProgramButton 
{	
	private Volume assignedVol;
	
	public Volume GetAssignedVolume()
	{
		return assignedVol;
	}
	
	public LibButton(Volume volume, Dimension dim)
	{
		super();
		
		assignedVol = volume;
		
		setLayout(new BorderLayout(5, 5));
		setContentAreaFilled(true);
		setBorderPainted(true);
		
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		String ratingStr = assignedVol.GetRating() == Volume.UNINITIALIZED_RATING ? 
				"To be rated" : assignedVol.GetRating() + " stars";
		String readingState = assignedVol.GetState().toString();
		
		JPanel eastPartition = new JPanel(new GridLayout(1, 3));
		JPanel westPartition = new JPanel(new GridLayout(1, 3));
		
		Dimension partitionDim = new Dimension((int)(dim.getWidth() / 2), getHeight());
		
		eastPartition.setPreferredSize(partitionDim);
		westPartition.setPreferredSize(partitionDim);
		
		// West Partition labels
		JLabel title 	= new JLabel(volume.GetTitle(),  SwingConstants.LEFT);
		JLabel author 	= new JLabel(volume.GetAuthor(), SwingConstants.LEFT);
		JLabel genre	= new JLabel(volume.GetGenre(),  SwingConstants.LEFT);
		
		// East Partition labels
		JLabel ISBN		= new JLabel(volume.GetISBN(), 	 SwingConstants.RIGHT);
		JLabel rating	= new JLabel(ratingStr, 		 SwingConstants.RIGHT);
		JLabel state	= new JLabel(readingState,		 SwingConstants.RIGHT);
		
		westPartition.add(title);
		westPartition.add(author);
		westPartition.add(genre);
		
		eastPartition.add(ISBN);
		eastPartition.add(rating);
		eastPartition.add(state);
		
		add(westPartition, BorderLayout.WEST);
		add(eastPartition, BorderLayout.EAST);
		
		assignedVol = volume;
	}
}
