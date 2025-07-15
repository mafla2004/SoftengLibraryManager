package UserInterface;

import java.awt.*;
import Utility.Package;

@Package class OperationButton extends ProgramButton
{
	public OperationButton(Dimension dim)
	{
		super();
		
		setLayout(new BorderLayout(5, 5));
		setContentAreaFilled(true);
		setBorderPainted(true);
		
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
	
	public OperationButton(String title, Dimension dim)
	{
		super(title);
		
		setLayout(new BorderLayout(5, 5));
		setContentAreaFilled(true);
		setBorderPainted(true);
		
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
}
