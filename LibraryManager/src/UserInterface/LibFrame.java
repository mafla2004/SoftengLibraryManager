package UserInterface;

import java.awt.*;
import javax.swing.*;
import Utility.Package;

@Package class LibFrame extends JFrame
{
	public LibFrame(Dimension dim, String title, boolean resizable, Color color)
	{
		super();
		
		setSize(dim);
		setVisible(true);
		setTitle(title);
		setResizable(resizable);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(color);
	}
}
