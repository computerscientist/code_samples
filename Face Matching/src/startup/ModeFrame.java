package startup;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import miscellaneous.*;

/**
 * Creates a frame that prompts the user for what game mode they want to play in
 * @author Robert Dallara
 *
 */
public class ModeFrame extends JFrame {

	static final int FRAME_WIDTH=280;
	static final int FRAME_HEIGHT=145;
	
	private JButton okButton;
	
	private JMenu fileMenu;
	private JMenuBar menuBar;
	
	private JRadioButton facesToFacesButton;
	private JRadioButton facesToWordsButton;
	
	/**
	 * Creates a frame that prompts the user what mode they want to play in
	 */
	public ModeFrame()
	{
		//Creates a menu bar with an option that allows the user to exit the game
		menuBar=new JMenuBar();
		this.setJMenuBar(menuBar);
		
		menuBar.add(this.createFileMenu());
		
		//Lays out the selection buttons that will allow the user to select the game mode
		JPanel modeSelectionPanel=new JPanel();
		JPanel modeButtonPanel=new JPanel();
		
		facesToFacesButton=new JRadioButton("Faces to Faces");
		facesToWordsButton=new JRadioButton("Faces to Words");
		
		ButtonGroup modeGroup=new ButtonGroup();
		
		modeGroup.add(facesToFacesButton);
		modeGroup.add(facesToWordsButton);
		
		modeButtonPanel.add(facesToFacesButton);
		modeButtonPanel.add(facesToWordsButton);
		
		modeSelectionPanel.add(modeButtonPanel);
		
		//Adds the "OK" button to this frame and makes it respond once the user has selected a mode
		okButton=new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(facesToFacesButton.isSelected())
					GameData.setGameMode(Mode.FACES_TO_FACES);
				
				else if(facesToWordsButton.isSelected())
					GameData.setGameMode(Mode.FACES_TO_WORDS);
				
				//Close this frame and prompt the user for the grid size once he/she has selected a mode
				if(GameData.getGameMode()!=null)
				{
					ModeFrame.this.setVisible(false);
					ModeFrame.this.dispose();
					
					new SizeFrame();
				}
			}
		});
		
		//Adds a border to this frame telling the user to select a mode
		modeSelectionPanel.setBorder(BorderFactory.createTitledBorder("Select Mode"));
		modeSelectionPanel.add(okButton);
		
		this.add(modeSelectionPanel);
		this.addWindowListener(new ModeFrameWindowListener());
		
		//Sets the properties of this frame and makes it visible
		this.setTitle("Select Mode");
		this.setSize(ModeFrame.FRAME_WIDTH, ModeFrame.FRAME_HEIGHT);
		
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
	class ModeFrameWindowListener implements WindowListener
	{
		public void windowOpened(WindowEvent event){}
		
		//Exits the program if the user tries to exit the window and he/she hasn't selected a game mode
		public void windowClosing(WindowEvent event)
		{
			if(GameData.getGameMode()==null)
				System.exit(0);
		}
		
		public void windowDeactivated(WindowEvent event){}
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0) {}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
	}
}
