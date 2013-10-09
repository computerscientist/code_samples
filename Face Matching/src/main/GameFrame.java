package main;

import java.awt.event.*;
import java.awt.*;

import java.util.Random;

import javax.swing.*;

import buttonpanel.*;
import emotionimages.*;

import memory.*;
import miscellaneous.*;
import sprites.*;
import startup.*;

/**
 * Creates the frame that actually displays and runs the face-matching game
 * @author Robert Dallara
 *
 */
public class GameFrame extends JFrame {
	
	static final int FRAME_WIDTH=1000;
	static final int FRAME_HEIGHT=700;
	
	static final int PAUSE_TIME=500;
	
	static final int HORIZONTAL_GAP=5;
	static final int VERTICAL_GAP=10;
	
	static int NUMBER_OF_ROWS;
	static int NUMBER_OF_COLUMNS;
	
	private boolean isPaused=false;
	
	private int[] firstFlippedCardCoordinates;
	private int numberOfCardsFlippedOver;
	
	private Card[][] cards;
	private ButtonPanel[][] buttonGrid;
	
	private JMenu fileMenu;
	private JMenuBar menuBar;
	
	private MemoryLayout layout;
	
	public GameFrame()
	{
		numberOfCardsFlippedOver=0;
		
		NUMBER_OF_ROWS=GameData.getGridRows();
		NUMBER_OF_COLUMNS=GameData.getGridColumns();
		
		//Creates the grid of cards as well as its corresponding grid of button panels
		cards=new Card[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
		buttonGrid=new ButtonPanel[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
		
		layout=new MemoryLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS);
		
		menuBar=new JMenuBar();
		this.setJMenuBar(menuBar);
		
		//Randomly insert matches of different emotions into the card grid
		this.arrangeMatches();
		
		JPanel buttonPanel=new JPanel(new GridLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS, HORIZONTAL_GAP, VERTICAL_GAP));
		
		/**
		 * After the matches have been arranged, initialize each button along with
		 * its button panel properties
		 */
		for(int i=0;i<buttonGrid.length;i++)
		{
			for(int j=0;j<buttonGrid[i].length;j++)
			{
				//Creates panel based on whether a card is a face picture or word picture
				if(layout.getPictureType(i, j).equals(PictureType.FACE_PICTURE))
					buttonGrid[i][j]=new LabeledButtonPanel(new JButton(cards[i][j].getVisibleSideIcon()), layout.getEmotionType(i, j));
				
				else
					buttonGrid[i][j]=new ButtonPanel(new JButton(cards[i][j].getVisibleSideIcon()));
				
				buttonGrid[i][j].getJButton().addActionListener(new ButtonListener(i, j));
				buttonPanel.add(buttonGrid[i][j]);
			}
		}
		
		//Display game mode at border of frame
		buttonPanel.setBorder(BorderFactory.createTitledBorder(GameData.getGameMode().equals(Mode.FACES_TO_FACES)? "Faces to Faces" : "Faces to Words"));
		
		//Initializes the properties of this frame and makes it visible
		this.add(buttonPanel);
		menuBar.add(this.createFileMenu());
		
		this.addWindowListener(new GameWindowListener());
		
		this.setTitle("Face Matching");
		this.setSize(GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	 * Sets the size of each button based on the dimensions of its panel
	 * as well as on the grid dimensions
	 */
	private void sizeButtons()
	{
		for(int i=0;i<buttonGrid.length;i++)
		{
			for(int j=0;j<buttonGrid[i].length;j++)
				buttonGrid[i][j].getJButton().setPreferredSize(new Dimension((int)(buttonGrid[i][j].getSize().getWidth()*9)/10, (int)(buttonGrid[i][j].getSize().getHeight()*75)/100));
		}
	}
	
	/**
	 * Makes a button take up most of its panel (meant to be used for "word pictures"
	 * when they are revealed, when their "question marks" beneath them will go away)
	 * 
	 * @param row the row of the button
	 * @param column the column of the button
	 */
	private void enlargeButton(int row, int column)
	{
		buttonGrid[row][column].getJButton().setPreferredSize(new Dimension((int)(buttonGrid[row][column].getSize().getWidth()*9)/10, (int)(buttonGrid[row][column].getSize().getHeight()*95)/100));
	}
	
	/**
	 * Makes a button smaller so that its label can be shown (meant to be used for "word pictures"
	 * when they are hidden again, when their "question marks" beneath them will reappear)
	 * 
	 * @param row the row of the button
	 * @param column the column of the button
	 */
	private void shrinkButton(int row, int column)
	{
		buttonGrid[row][column].getJButton().setPreferredSize(new Dimension((int)(buttonGrid[row][column].getSize().getWidth()*9)/10, (int)(buttonGrid[row][column].getSize().getHeight()*75)/100));
	}
	
	/**
	 * Flips a certain card on the grid
	 * @param rowNumber the row of the card
	 * @param columnNumber the column of the card
	 */
	private void flipCard(int rowNumber, int columnNumber)
	{
		cards[rowNumber][columnNumber].flip();
		
		//Show that the card is selected and show its hidden icon
		buttonGrid[rowNumber][columnNumber].switchBackgroundColor();
		buttonGrid[rowNumber][columnNumber].getJButton().setIcon(cards[rowNumber][columnNumber].getVisibleSideIcon());
		
		//Fit the image so that it fills the button
		this.resizeButtonImage(rowNumber, columnNumber);
		buttonGrid[rowNumber][columnNumber].repaint();
	}
	
	/**
	 * Checks to see if a certain card's emotion matches that of 
	 * another card that has already been flipped over just earlier
	 * 
	 * @param rowNumber the row of the card
	 * @param columnNumber the column of the card
	 */
	private void checkForMatches(int rowNumber, int columnNumber)
	{
		this.flipCard(rowNumber, columnNumber);
		
		if(layout.getPictureType(rowNumber, columnNumber).equals(PictureType.WORD_PICTURE))
			this.enlargeButton(rowNumber, columnNumber);
		
		/**
		 * Does the card we have just flipped over match the card that was flipped
		 * over earlier? (In faces-to-faces mode, two cards match if they have faces
		 * of the same emotion on them. In faces-to-words mode, two cards match if
		 * one card is a word picture with a certain emotion on it and the other card
		 * has a picture of that same emotion. Two cards won't match in this mode if
		 * they are of the same type (i.e both word/face pictures), even if they depict
		 * the same emotion).
		 */
		if((layout.getEmotionType(rowNumber, columnNumber).equals(layout.getEmotionType(firstFlippedCardCoordinates[0], firstFlippedCardCoordinates[1]))) &&
		   !(GameData.getGameMode().equals(Mode.FACES_TO_WORDS) && layout.getPictureType(rowNumber, columnNumber).equals(layout.getPictureType(firstFlippedCardCoordinates[0], firstFlippedCardCoordinates[1]))))
		{
			buttonGrid[rowNumber][columnNumber].setBackgroundToCompleted();
			buttonGrid[firstFlippedCardCoordinates[0]][firstFlippedCardCoordinates[1]].setBackgroundToCompleted();
			
			this.cards[rowNumber][columnNumber].reverseFlippability();
			GameFrame.this.firstFlippedCardCoordinates=null;
			
			numberOfCardsFlippedOver+=2;
			
			//Have we won yet?
			if(numberOfCardsFlippedOver>=NUMBER_OF_ROWS*NUMBER_OF_COLUMNS)
				JOptionPane.showMessageDialog(this, "You win!", "Round Completed", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else
		{
			//If the two cards don't match, pause the game briefly and then flip them back over
			this.cards[rowNumber][columnNumber].reverseFlippability();
			
			isPaused=true;
			
			Timer t=new Timer(PAUSE_TIME, new TimerListener(rowNumber, columnNumber));
			t.start();
		}
	}
	
	/**
	 * Places matches into the grid while shuffling the cards into random locations
	 */
	private void arrangeMatches()
	{
		Random random=new Random();
		
		for(int i=0;i<cards.length*cards[0].length;i+=2)
		{
			Location firstCardLocation;
			Location secondCardLocation;
			
			//Set the locations of the first and second cards in a match
			do
			{
				firstCardLocation=new Location(random.nextInt(NUMBER_OF_ROWS), random.nextInt(NUMBER_OF_COLUMNS));
			}
			while(cards[firstCardLocation.getRow()][firstCardLocation.getColumn()]!=null);

			do
			{
				secondCardLocation=new Location(random.nextInt(NUMBER_OF_ROWS), random.nextInt(NUMBER_OF_COLUMNS));
			}
			while(cards[secondCardLocation.getRow()][secondCardLocation.getColumn()]!=null || secondCardLocation.equals(firstCardLocation));
			
			//What emotion are the two cards in the match both going to have?
			EmotionType matchType=EmotionType.values()[random.nextInt(EmotionType.values().length)];
			
			String secondImageFilePath="";
			
			//Grab a face picture for the first card in the match
			String firstImageFilePath=grabEmotion(matchType, PictureType.FACE_PICTURE);
			
			/**Grab another face picture for the second card in the match if the
			 * user is playing in "faces-to-faces" mode
			 */
			if(GameData.getGameMode().equals(Mode.FACES_TO_FACES))
			{
				secondImageFilePath=grabEmotion(matchType, PictureType.FACE_PICTURE);
				
				cards[firstCardLocation.getRow()][firstCardLocation.getColumn()]=new Card(new ImageIcon(firstImageFilePath));
				cards[secondCardLocation.getRow()][secondCardLocation.getColumn()]=new Card(new ImageIcon(secondImageFilePath));
				
				layout.setPictureType(firstCardLocation.getRow(), firstCardLocation.getColumn(), PictureType.FACE_PICTURE);
				layout.setPictureType(secondCardLocation.getRow(), secondCardLocation.getColumn(), PictureType.FACE_PICTURE);
			}
			
			/**
			 * Grab a word picture for the second card in the match if the
			 * user is playing in "faces-to-words" mode
			 */
			else if(GameData.getGameMode().equals(Mode.FACES_TO_WORDS))
			{
				secondImageFilePath=grabEmotion(matchType, PictureType.WORD_PICTURE);
				int cardWithWordPicture=random.nextInt(2);
				
				cards[firstCardLocation.getRow()][firstCardLocation.getColumn()]=new Card(new ImageIcon((cardWithWordPicture==0 ? secondImageFilePath : firstImageFilePath)));
				cards[secondCardLocation.getRow()][secondCardLocation.getColumn()]=new Card(new ImageIcon((cardWithWordPicture==1 ? secondImageFilePath : firstImageFilePath)));
				
				layout.setPictureType(firstCardLocation.getRow(), firstCardLocation.getColumn(), (cardWithWordPicture==0 ? PictureType.WORD_PICTURE : PictureType.FACE_PICTURE));
				layout.setPictureType(secondCardLocation.getRow(), secondCardLocation.getColumn(), (cardWithWordPicture==1 ? PictureType.WORD_PICTURE : PictureType.FACE_PICTURE));
			}
			
			//Store the emotion of each card in the match in the grid layout object
			layout.setEmotionType(firstCardLocation.getRow(), firstCardLocation.getColumn(), matchType);
			layout.setEmotionType(secondCardLocation.getRow(), secondCardLocation.getColumn(), matchType);
		}
	}
	
	/**
	 * Finds the location of an image for a particular emotion
	 * @param emotionType the emotion that the image must depict
	 * @param pictureType the type of picture to be located (i.e face/word picture)
	 * @return the location of the image
	 */
	private String grabEmotion(EmotionType emotionType, PictureType pictureType)
	{
		if(emotionType.equals(EmotionType.HAPPY))
			return ImageGrabber.grabHappyFace(pictureType);
		
		else if(emotionType.equals(EmotionType.SAD))
			return ImageGrabber.grabSadFace(pictureType);
		
		else if(emotionType.equals(EmotionType.CONFUSED))
			return ImageGrabber.grabConfusedFace(pictureType);
		
		else if(emotionType.equals(EmotionType.SCARED))
			return ImageGrabber.grabScaredFace(pictureType);
		
		else if(emotionType.equals(EmotionType.ANGRY))
			return ImageGrabber.grabAngryFace(pictureType);
		
		else if(emotionType.equals(EmotionType.SLEEPY))
			return ImageGrabber.grabSleepyFace(pictureType);
		
		else if(emotionType.equals(EmotionType.SURPRISED))
			return ImageGrabber.grabSurprisedFace(pictureType);
		
		else if(emotionType.equals(EmotionType.DISAPPOINTED))
			return ImageGrabber.grabDisappointedFace(pictureType);
		
		else
			return ImageGrabber.grabNervousFace(pictureType);
	}
	
	/**
	 * Resizes the image in each button to fully fit into it
	 */
	private void resizeButtonImages()
	{
		for(int i=0;i<buttonGrid.length;i++)
		{
			for(int j=0;j<buttonGrid[i].length;j++)
				resizeButtonImage(i, j);
		}
	}
	
	/**
	 * Resizes the image in a particular button to fully fit into it
	 * @param buttonRow the row of the button
	 * @param buttonColumn the column of the button
	 */
	private void resizeButtonImage(int buttonRow, int buttonColumn)
	{
		ImageIcon compressedImage=new ImageIcon(cards[buttonRow][buttonColumn].getVisibleSideIcon().getImage().getScaledInstance(buttonGrid[buttonRow][buttonColumn].getWidth(), buttonGrid[buttonRow][buttonColumn].getHeight(), java.awt.Image.SCALE_AREA_AVERAGING));
		buttonGrid[buttonRow][buttonColumn].getJButton().setIcon(compressedImage);
	}
	
	/**
	 * Monitors the window of this frame (especially when it is opening)
	 * and responds appropriately to its circumstances
	 * 
	 * @author Robert Dallara
	 *
	 */
	class GameWindowListener implements WindowListener
	{
		//When the window opens, make all of the buttons (and their images) the correct size
		public void windowOpened(WindowEvent event)
		{
			sizeButtons();
			resizeButtonImages();
		}
		
		public void windowClosing(WindowEvent event) {}
		public void windowDeactivated(WindowEvent event){}
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0) {}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
	}
	
	/**
	 * When two cards don't match, the game will be paused briefly. After this pause,
	 * this class will invoke a method which will flip both of the cards back over
	 * 
	 * @author Robert Dallara
	 *
	 */
	class TimerListener implements ActionListener
	{
		private int rowNumber;
		private int columnNumber;
		
		/**
		 * Constructs a new object that flips two cards that don't match back over
		 * @param rowNumber the row of the last card that was flipped over
		 * @param columnNumber the column of the last card that was flipped over
		 */
		public TimerListener(int rowNumber, int columnNumber)
		{
			this.rowNumber=rowNumber;
			this.columnNumber=columnNumber;
		}
		
		/**
		 * Run when the pause is over
		 */
		public void actionPerformed(ActionEvent event)
		{
			GameFrame.this.cards[rowNumber][columnNumber].reverseFlippability();
			GameFrame.this.cards[firstFlippedCardCoordinates[0]][firstFlippedCardCoordinates[1]].reverseFlippability();
			
			//Flip the last-flipped card (the second card in the non-match) back over
			GameFrame.this.flipCard(rowNumber, columnNumber);
			
			//Flip the first card in the non-match back over
			GameFrame.this.flipCard(firstFlippedCardCoordinates[0], firstFlippedCardCoordinates[1]);

			/**
			 * If either of the two cards contained a word picture, then resize that card's
			 * button so that the "question mark" can be displayed beneath it again
			 */
			if(layout.getPictureType(rowNumber, columnNumber).equals(PictureType.WORD_PICTURE))
				GameFrame.this.shrinkButton(rowNumber, columnNumber);
			
			if(layout.getPictureType(firstFlippedCardCoordinates[0], firstFlippedCardCoordinates[1]).equals(PictureType.WORD_PICTURE))
				GameFrame.this.shrinkButton(firstFlippedCardCoordinates[0], firstFlippedCardCoordinates[1]);
			
			GameFrame.this.firstFlippedCardCoordinates=null;
			
			//End the simulation pause
			((Timer) event.getSource()).stop();
			isPaused=false;
		}
	}
	
	/**
	 * Monitors each button for clicks and responds appropriately to them
	 * @author Robert Dallara
	 *
	 */
	class ButtonListener implements ActionListener
	{
		private int rowNumber;
		private int columnNumber;
		
		/**
		 * Creates a new button listener for a particular button
		 * @param rowNumber the row of the button
		 * @param columnNumber the column of the button
		 */
		public ButtonListener(int rowNumber, int columnNumber)
		{
			this.rowNumber=rowNumber;
			this.columnNumber=columnNumber;
		}
		
		/**
		 * Flips over the button's card when the button is pressed
		 */
		public void actionPerformed(ActionEvent event)
		{
			//Only run if the button's card can be flipped and if the simulation is not paused
			if(cards[rowNumber][columnNumber].isFlippable() && !isPaused)
			{
				if(layout.getPictureType(rowNumber, columnNumber).equals(PictureType.WORD_PICTURE))
					GameFrame.this.enlargeButton(rowNumber, columnNumber);
				
				if(GameFrame.this.firstFlippedCardCoordinates==null)
				{
					GameFrame.this.flipCard(rowNumber, columnNumber);
					
					GameFrame.this.firstFlippedCardCoordinates=new int[2];
					GameFrame.this.firstFlippedCardCoordinates[0]=rowNumber;
					GameFrame.this.firstFlippedCardCoordinates[1]=columnNumber;
					
					cards[rowNumber][columnNumber].reverseFlippability();
				}
				
				/**
				 * If one card has just been flipped over, then check to see if this
				 * card that has been flipped matches that first card
				 */
				else if(rowNumber!=GameFrame.this.firstFlippedCardCoordinates[0] || columnNumber!=GameFrame.this.firstFlippedCardCoordinates[1])
					GameFrame.this.checkForMatches(rowNumber, columnNumber);
			}
		}
	}
}
