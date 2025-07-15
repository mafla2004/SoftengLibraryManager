package UserInterface;

import java.awt.*;
import javax.swing.*;
import Utility.Package;
import LibraryModule.Volume;

@Package class SelectedVolumePanel extends JPanel
{
	private JLabel lbTitle 	= new JLabel();
	private JLabel lbAuthor = new JLabel();
	private JLabel lbGenre 	= new JLabel();
	private JLabel lbISBN 	= new JLabel();
	private JLabel lbRating = new JLabel();
	private JLabel lbState 	= new JLabel();
	
	public SelectedVolumePanel(Dimension dim)
	{
		setLayout(new GridLayout(6, 1, 5, 5));
		
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		
		add(lbTitle);
		add(lbAuthor);
		add(lbGenre);
		add(lbISBN);
		add(lbRating);
		add(lbState);
		
		setBorder(BorderFactory.createTitledBorder("Selected Volume"));
		
		setVolume(null);
	}
	
	public void setVolume(Volume v) 
	{
        if (v == null) 
        {
            lbTitle.setText("TITLE: ");
            lbAuthor.setText("AUTHOR: ");
            lbGenre.setText("GENRE: ");
            lbISBN.setText("ISBN: ");
            lbRating.setText("RATING: ");
            lbState.setText("STATE: ");
        } 
        else
        {
        	lbTitle.setText("TITLE: " 	+ v.GetTitle());
            lbAuthor.setText("AUTHOR: " + v.GetAuthor());
            lbGenre.setText("GENRE: " 	+ v.GetGenre());
            lbISBN.setText("ISBN: " 	+ v.GetISBN());
            lbRating.setText("RATING: " + v.GetRating() + "star(s)");
            lbState.setText("STATE: " 	+ v.GetState());
        }
	}
}
