package startup;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import main.*;
import miscellaneous.*;

/**
 * Creates a frame that prompts the user what sized grid he/she wants to play on
 * @author Robert Dallara
 *
 */
public class SizeFrame extends JFrame {

	static final int FRAME_WIDTH=350;
	static final int FRAME_HEIGHT=140;
	
	private JButton okButton;
	
	private JMenu fileMenu;
	private JMenuBar menuBar;
	
	private JRadioButton smallButton;
	private JRadioButton mediumButton;
	private JRadioButton largeButton;
	
	/**
	 * Creates a frame that prompts the user about the grid size
	 */
	public SizeFrame()
	{
		//Creates a menu bar with an option that allows the user to exit the game
		menuBar=new JMenuBar();
		this.setJMenuBar(menuBar);
		
		menuBar.add(this.createFileMenu());
		
		//Lays out the selection buttons that will allow the user to select the grid size
		JPanel buttonPanel=new JPanel();
		
		smallButton=new JRadioButton("Small (2x3)");
		mediumButton=new JRadioButton("Medium (3x4)");
		largeButton=new JRadioButton("Large (4x4)");
		
		ButtonGroup sizeGroup=new ButtonGroup();
		
		sizeGroup.add(smallButton);
		sizeGroup.add(mediumButton);
		sizeGroup.add(largeButton);
		
		buttonPanel.add(smallButton);
		buttonPanel.add(mediumButton);
		buttonPanel.add(largeButton);
		
		//Adds the "OK" button to this frame and makes it respond once the user has selected a grid size
		okButton=new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(smallButton.isSelected())
				{
					GameData.setGridRows(2);
					GameData.setGridColumns(3);
				}
				
				else if(mediumButton.isSelected())
				{
					GameData.setGridRows(3);
					GameData.setGridColumns(4);
				}
				
				else if(largeButton.isSelected())
				{
					GameData.setGridRows(4);
					GameData.setGridColumns(4);
				}
				
				if(GameData.getGridRows()>0 && GameData.getGridColumns()>0)
				{
					SizeFrame.this.setVisible(false);
					SizeFrame.this.dispose();
					
					new GameFrame();
				}
			}
		});
		
		//Adds a border to this frame telling the user to select a grid size
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Select Size"));
		buttonPanel.add(okButton);
		
		this.add(buttonPanel);
		this.addWindowListener(new SizeFrameWindowListener());
		
		//Sets the properties of this frame and makes it visible
		this.setTitle("Select Board Size");
		this.setSize(SizeFrame.FRAME_WIDTH, SizeFrame.FRAME_HEIGHT);
		
		Dimension screenDimensions=Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenDimensions.getWidth()/2.5), (int)(screenDimensions.getHeight()/2.5));
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/**
	 * Creates the file menu containing the "exit" option
	 * @return the menu after it has been created
	 */
	private JMenu createFileMenu()
	{
		fileMenu=new JMenu("File");
		fileMenu.add(createExitItem());
		
		return fileMenu;
	}
	
	/**
	 * Creates the "exit" option in the file menu (which the user can use to exit the game)
	 * @return the menu item (that is the "exit" option) after it is created
	 */
	private JMenuItem createExitItem()
	{
		JMenuItem exitItem=new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		
		return exitItem;
	}
	
	/**
	 * Used to listen in on the window and respond appropriately to its changes and circumstances
	 * @author Robert Dallara
	 *
	 */
	class SizeFrameWindowListener implements WindowListener
	{
		public void windowOpened(WindowEvent event){}
		
		//Exits the program if the user tries to exit the window and he/she hasn't selected a grid size
		public void windowClosing(WindowEvent event)
		{
			if(GameData.getGridRows()==0 || GameData.getGridColumns()==0)
				System.exit(0);
		}
		
		public void windowDeactivated(WindowEvent event){}
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0){}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
	}
}
