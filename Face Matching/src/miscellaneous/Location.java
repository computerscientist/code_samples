package miscellaneous;

/**
 * Used to store the location of a card in the grid of cards
 * @author Robert Dallara
 *
 */
public class Location {

	private int row;
	private int column;
	
	/**
	 * Constructs a new location storing object for a card
	 * @param row the row of the card
	 * @param column the column of the card
	 */
	public Location(int row, int column)
	{
		this.row=row;
		this.column=column;
	}
	
	/**
	 * Returns the row of the card's location
	 * @return the card's row
	 */
	public int getRow()
	{
		return this.row;
	}
	
	/**
	 * Returns the column of the card's location
	 * @return the card's column
	 */
	public int getColumn()
	{
		return this.column;
	}
	
	/**
	 * Checks to see if this location is equal to another location
	 * @param anotherLocation the other location to compare this one to
	 * @return whether or not this location is equal to the other location
	 */
	public boolean equals(Location anotherLocation)
	{
		return (this.getRow()==anotherLocation.getRow()) && (this.getColumn()==anotherLocation.getColumn());
	}
}
