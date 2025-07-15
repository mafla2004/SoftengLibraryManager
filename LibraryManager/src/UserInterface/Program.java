package UserInterface;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import javax.swing.*;
import Utility.Package;
import LibraryModule.Volume;
import LibraryModule.VolumeField;
import LibraryModule.LibraryMediator;
import LibraryModule.ReadingState;
import StorageModule.NoPathFoundException;
import StorageModule.StorageHandler;

// Main class of the program, handling the GUI and user interactions
public class Program 
{
	public static final float 		LIB_HOR_FRACTION 	= .5f;
	public static final int			LIB_BTN_HEIGHT		= 35;
	public static final int 		MIN_BTN_Y 			= 30;
	public static final byte 		NUM_OF_FIELDS 		= 6;
	public static final byte		RATING_INDEX		= 4;
	public static final byte		STATE_INDEX			= 5;
	public static final Dimension 	P1080 				= new Dimension(1920, 1080);
	public static final Dimension 	P1440 				= new Dimension(2560, 1440);
	public static final Dimension	P720				= new Dimension(1280, 720);
	public static final Dimension 	DIM					= P720;
	public static final Dimension	LIB_BTN_DIM			= new Dimension((int)(DIM.width * LIB_HOR_FRACTION), LIB_BTN_HEIGHT);
	public static final Dimension	CTRL_PANE_DIM		= new Dimension((int)(DIM.width * (1 - LIB_HOR_FRACTION)), DIM.height / 4);
	public static final Dimension	CMD_BTN_DIM			= new Dimension(CTRL_PANE_DIM.width / 3, CTRL_PANE_DIM.height);
	public static final Dimension	STRG_BTN_DIM		= new Dimension(CTRL_PANE_DIM.width / 2, CTRL_PANE_DIM.height);
	public static final Color 		ELEGANT_BLUE 		= new Color(128, 128, 145);
	public static final Color 		MORE_ELEGANT_BLUE 	= new Color(172, 172, 220);
	public static final Color		BRIGHT_CYAN			= new Color(172, 200, 220);
	public static final String		PATH_REGEX 			= "^[A-Z]:\\\\.*$";
	public static final VolumeField	FIELDS[]			= { VolumeField.TITLE, VolumeField.AUTHOR, VolumeField.GENRE, VolumeField.ISBN, VolumeField.RATING, VolumeField.READING_STATE };
	
	public static record FilterInfo(VolumeField field, String value) 
	{
		public FilterInfo 
		{
			Objects.requireNonNull(field);
			Objects.requireNonNull(value);
		}
	}
	
	// Program variables
	private static Volume selectedVolume = null;
	private static SelectedVolumePanel selVolPanel = new SelectedVolumePanel(CMD_BTN_DIM);
	
	private static void RefreshLibraryButtons(LibraryMediator system, JPanel container)
	{
		if (system == null || container == null || !system.isInitialized())
		{
			return;
		}
		
		container.removeAll();
		
		for (Volume v : system.RetrieveLibrary())
		{
			LibButton btn = new LibButton(v, LIB_BTN_DIM);
			
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn.addActionListener(e -> {
				selectedVolume = btn.GetAssignedVolume();
				System.out.println("Selected volume: " + selectedVolume.GetTitle());
				selVolPanel.setVolume(selectedVolume);
			});
			
			container.add(btn);
			container.add(Box.createVerticalStrut(5));
		}
		container.revalidate();
		container.repaint();
	}
	
	private static String trim(String s) { return s.replaceAll("^\\s+|\\s+$", ""); }
	
	private static String[] askVolumeInformations(Component parent)
	{
		JTextField fields[] = new JTextField[NUM_OF_FIELDS];
		for (int i = 0; i < NUM_OF_FIELDS; i++)
		{
			fields[i] = new JTextField();
		}
		
		String[] labelNames = { "Title", "Author", "Genre", "ISBN code", "Rating", "Reading State" };
		
		JPanel panel = new JPanel(new GridLayout(NUM_OF_FIELDS, 2, 5, 5)); // 2 cols, 1 for labels, one for fields
		for (int i = 0; i < NUM_OF_FIELDS; i++)
		{
			panel.add(new JLabel(labelNames[i]));
			panel.add(fields[i]);
		}
		
		int result = JOptionPane.showConfirmDialog(
		            parent, 
		            panel, 
		            "Enter volume informations", 
		            JOptionPane.OK_CANCEL_OPTION, 
		            JOptionPane.PLAIN_MESSAGE
		        );
		
		if (result != JOptionPane.OK_OPTION)
		{
			return null;
		}
		
		String inputs[] = new String[NUM_OF_FIELDS];
		for (int i = 0; i < NUM_OF_FIELDS; i++)
		{
			inputs[i] = fields[i].getText();
		}
		
		if (inputs[RATING_INDEX] == null || trim(inputs[RATING_INDEX]).length() == 0)
		{
			inputs[RATING_INDEX] = "0";
		}
		
		if (inputs[STATE_INDEX] == null || trim(inputs[STATE_INDEX]).length() == 0)
		{
			inputs[STATE_INDEX] = "to be read";
		}
		
		return inputs;
	}
	
	public static FilterInfo askFilterInfo(Component parent)
	{
		JTextField txtValue = new JTextField();
		JComboBox<VolumeField> field = new JComboBox<VolumeField>(FIELDS);
		field.setSelectedIndex(0);
		
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.add(new JLabel("Filter:"));
		panel.add(txtValue);
		panel.add(new JLabel("By:"));
		panel.add(field);
		
		int option = JOptionPane.showConfirmDialog(
	            parent, panel, "Specify filter info",
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE
	        );
		
		if (option != JOptionPane.OK_OPTION)
		{
			return null;
		}
		
		return new FilterInfo((VolumeField)(field.getSelectedItem()), txtValue.getText());
	}
	
	public static boolean checkPathValidity(String path)
	{
		if (path == null || path.length() < 3)
		{
			return false;
		}
		
		return path.matches(PATH_REGEX);
	}
	
	public static void main(String... args)
	{	
		LibFrame frame = new LibFrame(DIM, "Library Manager", false, ELEGANT_BLUE);
		Container frameContentPane = frame.getContentPane();
		
		// LIBRARY DISPLAYER
		
		// Create a container for the volume buttons
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBackground(MORE_ELEGANT_BLUE);
		container.setPreferredSize(new Dimension(DIM.width /2, DIM.height));
		
		/*
		// CONTAINER TEST WITH A FEW VOLUMES
		Volume literally1984 = new Volume("1984", "George Orwell", "Dystopian", "1234567890123", (byte)5, ReadingState.READING);
		Volume unreal = new Volume("Unreal", "Epic", "Shooter", "1234567890", (byte)5, ReadingState.READ);
		Volume hl2 = new Volume("Half-Life 2", "Valve", "Shooter", "0987654321", (byte)5, ReadingState.READ);
		List<Volume> testList = List.of(literally1984, unreal, hl2);
		for (Volume v : testList)
		{
			LibButton btn = new LibButton(v, LIB_BTN_DIM);
			
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			container.add(btn);
			container.add(Box.createVerticalStrut(5));
		}*/
		
		LibraryMediator system = LibraryMediator.INSTANCE;
		while (!system.isInitialized())
		{
			try
			{
				system.Initialize();
			}
			catch (NoPathFoundException e)
			{
				System.out.println("NO PATH WAS FOUND");
			
				// Ask the user to input a path
				boolean validPath = false;
				while (!validPath)
				{
					String path = JOptionPane.showInputDialog(
								frame,
								"Please enter the path to save your library in: ",
								"Path needed",
								JOptionPane.PLAIN_MESSAGE
							);
					if (path == null)
					{
						System.out.println("Null path");
						frame.dispose();
						return;
					}
					
					validPath = checkPathValidity(path);

					try 
					{
						File pathFile = new File(StorageHandler.PATH_FILE_LOCATION);
						if (!pathFile.exists())
						{
							System.out.println("Path file not found");
							return;
						}
					
						FileWriter pathWriter = new FileWriter(pathFile);
					
						pathWriter.write(path);
					
						pathWriter.close();
					} 
					catch (IOException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		/*
		// Test volume addition, refreshing and saving
		system.AddVolume("Unreal", "Epic", "Shooter", "1234567890", (byte)5, ReadingState.READ);
		system.AddVolume("Quake", "Id", "Shooter", "1234567890", (byte)5, ReadingState.READ);
		RefreshLibraryButtons(system, container);
		system.SaveLibrary();*/
		
		// Test Removal
		/*
		system.RemoveVolume("Quake", "Id", "Shooter", "1234567890");
		RefreshLibraryButtons(system, container);*/
		
		RefreshLibraryButtons(system, container);
		
		JScrollPane scrollPane = new JScrollPane(container);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    frame.add(scrollPane, BorderLayout.WEST);
	    
	    // LIBRARY AND OBJECT MANAGER
	    
	    JPanel commandBox = new JPanel();
	    
	    commandBox.setLayout(new BoxLayout(commandBox, BoxLayout.Y_AXIS));
	    commandBox.setBackground(ELEGANT_BLUE);
	    
	    // Volume operations panel
	    
	    JPanel volOpPanel = new JPanel();
	    
	    volOpPanel.setLayout(new BoxLayout(volOpPanel, BoxLayout.X_AXIS));
	    volOpPanel.setBackground(BRIGHT_CYAN);
	    
	    volOpPanel.setPreferredSize(CTRL_PANE_DIM);
	    
	    OperationButton addVolume 		= new OperationButton("ADD VOLUME", 	CMD_BTN_DIM);
	    OperationButton removeVolume 	= new OperationButton("REMOVE VOLUME", 	CMD_BTN_DIM);
	    OperationButton wipeLibrary 	= new OperationButton("WIPE LIBRARY", 	CMD_BTN_DIM);
	    
	    volOpPanel.add(addVolume);
	    volOpPanel.add(removeVolume);
	    volOpPanel.add(wipeLibrary);
	    
	    addVolume.addActionListener(e -> {
	    	while (true)
	    	{
	    		String info[] = askVolumeInformations(volOpPanel);
	    		
	    		String ratingInput = trim(info[RATING_INDEX]);
	    		if (ratingInput.length() != 1 || !Character.isDigit(ratingInput.charAt(0)))
	    		{
	    			JOptionPane.showMessageDialog(addVolume, "Invalid Rating value", "ERROR", JOptionPane.OK_OPTION);
	    			continue;
	    		}
	    		byte rating = (byte) Character.getNumericValue(ratingInput.charAt(0));
	    		
	    		String stateInput = trim(info[STATE_INDEX].toLowerCase());
	    		ReadingState state;
	    		switch (stateInput)
	    		{
	    		case "read":
	    			state = ReadingState.READ;
	    			break;
	    		case "reading":
	    			state = ReadingState.READING;
	    			break;
	    		default:
	    			state = ReadingState.TO_BE_READ;
	    			break;
	    		}
	    		
	    		system.AddVolume(info[0], info[1], info[2], info[3], rating, state);
	    		RefreshLibraryButtons(system, container);
	    		
	    		break;
	    	}
	    });
	    
	    removeVolume.addActionListener(e -> {
	    	/*while (true)
	    	{
	    		String info[] = askVolumeInformations(volOpPanel);
	    		
	    		/*
	    		String ratingInput = trim(info[RATING_INDEX]);
	    		if (ratingInput.length() != 1 || !Character.isDigit(ratingInput.charAt(0)))
	    		{
	    			JOptionPane.showMessageDialog(addVolume, "ERROR", "Invalid Rating value", JOptionPane.OK_OPTION);
	    		}
	    		byte rating = (byte) Character.getNumericValue(ratingInput.charAt(0));
	    		
	    		String stateInput = trim(info[STATE_INDEX].toLowerCase());
	    		ReadingState state;
	    		switch (stateInput)
	    		{
	    		case "read":
	    			state = ReadingState.READ;
	    		case "reading":
	    			state = ReadingState.READING;
	    		default:
	    			state = ReadingState.TO_BE_READ;
	    		}/
	    		
	    		system.RemoveVolume(info[0], info[1], info[2], info[3]);
	    		
	    		RefreshLibraryButtons(system, container);
	    		
	    		break;
	    	}*/
	    	
	    	if (selectedVolume == null)
	    	{
	    		JOptionPane.showMessageDialog(removeVolume, "You must first select a volume from the library", "ERROR", JOptionPane.OK_OPTION);
	    		return;
	    	}
	    	
	    	system.RemoveVolume(selectedVolume.GetTitle(), selectedVolume.GetAuthor(), selectedVolume.GetGenre(), selectedVolume.GetISBN());
	    	selVolPanel.setVolume(null);
	    	RefreshLibraryButtons(system, container);
	    });
	    
	    wipeLibrary.addActionListener(e -> {
	    	int result = JOptionPane.showConfirmDialog(
	    			wipeLibrary, 
	    			"This will delete the entire library, you'll still have to save manually for this to translate on disk. Are you sure?",
	    			"Notice",
	    			JOptionPane.OK_CANCEL_OPTION
	    			);
	    	if (result == JOptionPane.OK_OPTION)
	    	{
	    		system.WipeLibrary();
	    		RefreshLibraryButtons(system, container);
	    	}
	    });
	    
	    commandBox.add(volOpPanel, BorderLayout.NORTH);
	    
	    // Volume control panel
	    
	    JPanel volCtrlPanel = new JPanel();
	    
	    volCtrlPanel.setLayout(new BoxLayout(volCtrlPanel, BoxLayout.X_AXIS));
	    volCtrlPanel.setBackground(BRIGHT_CYAN);
	    
	    OperationButton modify 	= new OperationButton("MODIFY VOLUME", CMD_BTN_DIM);
	    OperationButton sort 	= new OperationButton("SORT VOLUMES", CMD_BTN_DIM);
	    
	    volCtrlPanel.add(selVolPanel);
	    volCtrlPanel.add(modify);
	    volCtrlPanel.add(sort);
	    
	    modify.addActionListener(e -> {
	    	if (selectedVolume == null)
	    	{
	    		JOptionPane.showMessageDialog(modify, "You must first select a volume from the library", "ERROR", JOptionPane.OK_OPTION);
	    		return;
	    	}
	    	
	    	while (true)
	    	{
	    		String info[] = askVolumeInformations(volOpPanel);
	    		
	    		String ratingInput = trim(info[RATING_INDEX]);
	    		if (ratingInput.length() != 1 || !Character.isDigit(ratingInput.charAt(0)))
	    		{
	    			JOptionPane.showMessageDialog(modify, "Invalid Rating value", "ERROR", JOptionPane.OK_OPTION);
	    			continue;
	    		}
	    		byte rating = (byte) Character.getNumericValue(ratingInput.charAt(0));
	    		
	    		String stateInput = trim(info[STATE_INDEX].toLowerCase());
	    		ReadingState state;
	    		switch (stateInput)
	    		{
	    		case "read":
	    			state = ReadingState.READ;
	    			break;
	    		case "reading":
	    			state = ReadingState.READING;
	    			break;
	    		default:
	    			state = ReadingState.TO_BE_READ;
	    			break;
	    		}
	    		
	    		system.ModifyVolume(selectedVolume, info[0], info[1], info[2], info[3], rating, state);
	    		selVolPanel.setVolume(selectedVolume);
	    		RefreshLibraryButtons(system, container);
	    		
	    		break;
	    	}
	    });
	    
	    sort.addActionListener(e -> {
	    	JComboBox<VolumeField> field = new JComboBox<VolumeField>(FIELDS);
	    	
	    	JPanel sortPanel = new JPanel(new GridLayout(1, 2, 5, 5));
	    	sortPanel.add(new JLabel("Sort by: "));
	    	sortPanel.add(field);
	    	
	    	int option = JOptionPane.showConfirmDialog(
	    				volCtrlPanel,
	    				sortPanel,
	    				"Sort",
	    				JOptionPane.OK_CANCEL_OPTION
	    			);
	    	
	    	if (option != JOptionPane.OK_OPTION)
	    	{
	    		return;
	    	}
	    	
	    	system.SortTitles((VolumeField)field.getSelectedItem());
	    	RefreshLibraryButtons(system, container);
	    });
	    
	    commandBox.add(volCtrlPanel);
	    
	    // Filter operations panel
	    
	    JPanel fltOpPanel = new JPanel();
	    
	    fltOpPanel.setLayout(new BoxLayout(fltOpPanel, BoxLayout.X_AXIS));
	    fltOpPanel.setBackground(BRIGHT_CYAN);
	    
	    fltOpPanel.setPreferredSize(CTRL_PANE_DIM);
	    
	    OperationButton addFilter 		= new OperationButton("ADD FILTER", 	CMD_BTN_DIM);
	    OperationButton removeFilter 	= new OperationButton("REMOVE FILTER", 	CMD_BTN_DIM);
	    OperationButton wipeFilters 	= new OperationButton("WIPE FILTERS", 	CMD_BTN_DIM);
	    
	    fltOpPanel.add(addFilter);
	    fltOpPanel.add(removeFilter);
	    fltOpPanel.add(wipeFilters);
	    
	    addFilter.addActionListener(e -> {
	    	while (true)
	    	{
	    		FilterInfo info = askFilterInfo(fltOpPanel);
	    		
	    		if (info == null)
	    		{
	    			break; // User cancelled action
	    		}
	    		
	    		system.AddFilter(info.field(), info.value());
	    		RefreshLibraryButtons(system, container);
	    		
	    		break;
	    	}
	    });
	    
	    removeFilter.addActionListener(e -> {
	    	while (true)
	    	{
	    		FilterInfo info = askFilterInfo(fltOpPanel);
	    		
	    		if (info == null)
	    		{
	    			break; // User cancelled action
	    		}
	    		
	    		system.RemoveFilter(info.field(), info.value());
	    		RefreshLibraryButtons(system, container);
	    		
	    		break;
	    	}
	    });
	    
	    wipeFilters.addActionListener(e -> {
	    	system.ClearFilters();
	    	RefreshLibraryButtons(system, container);
	    });
	    
	    commandBox.add(fltOpPanel, BorderLayout.NORTH);
	    
	    // Storage operations panel
	    
	    JPanel strgOpPanel = new JPanel();
	    
	    strgOpPanel.setLayout(new BoxLayout(strgOpPanel, BoxLayout.X_AXIS));
	    strgOpPanel.setBackground(BRIGHT_CYAN);
	    
	    strgOpPanel.setPreferredSize(CTRL_PANE_DIM);
	    
	    OperationButton save 	= new OperationButton("SAVE", CMD_BTN_DIM);
	    OperationButton load 	= new OperationButton("LOAD", CMD_BTN_DIM);
	    OperationButton chDir 	= new OperationButton("CHANGE DIRECTORY", CMD_BTN_DIM);
	    
	    strgOpPanel.add(save);
	    strgOpPanel.add(load);
	    strgOpPanel.add(chDir);
	    
	    save.addActionListener(e -> {
	    	if (!system.SaveLibrary())
	    	{
	    		JOptionPane.showMessageDialog(addVolume, "ERROR", "Something went wrong while saving", JOptionPane.OK_OPTION);
	    	}
	    	
	    	RefreshLibraryButtons(system, container);
	    });
	    
	    load.addActionListener(e -> {
	    	if (!system.LoadLibrary())
	    	{
	    		JOptionPane.showMessageDialog(addVolume, "ERROR", "Something went wrong while loading", JOptionPane.OK_OPTION);
	    	}
	    	
	    	RefreshLibraryButtons(system, container);
	    });
	    
	    chDir.addActionListener(e -> {
	    	boolean validPath = false;
			while (!validPath)
			{
				String path = JOptionPane.showInputDialog(
							frame,
							"Please enter the path to save your library in: ",
							"Path needed",
							JOptionPane.PLAIN_MESSAGE
						);
			
				validPath = checkPathValidity(path);

				try 
				{
					File pathFile = new File(StorageHandler.PATH_FILE_LOCATION);
					if (pathFile.exists())
					{
						pathFile.delete();
					}
					pathFile.createNewFile();
				
					FileWriter pathWriter = new FileWriter(pathFile);
				
					pathWriter.write(path);
				
					pathWriter.close();
					
					try 
					{
						system.ReinitializeStorage();
						
						RefreshLibraryButtons(system, container);
					} 
					catch (NoPathFoundException pnf) 
					{
						JOptionPane.showMessageDialog(modify, "Invalid path, please reinsery", "ERROR", JOptionPane.OK_OPTION);
						pnf.printStackTrace();
					}
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    });
	    
	    commandBox.add(strgOpPanel);
	    
	    frame.add(commandBox);
	    
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
}
