
/**
 * Trimmed down version of course class to use for sending over a socket
 * @author Jerome Gobeil
 *
 */
public class CourseLite {

	String name;
	
	int number;
	
	/**
	 * Offering section number
	 */
	int[] offeringSecNum;
	
	/**
	 * Offering taken spots and max spots
	 */
	int[][] offeringSpots;
	
	/**
	 * Constructor for the course lite class
	 * @param name
	 * @param number
	 * @param offeringCount
	 */
	public CourseLite(String name, int number, int offeringCount)
	{
		this.name = name;
		this.number = number;
		offeringSecNum = new int[offeringCount];
		offeringSpots = new int[offeringCount][2];
	}
	
	/**
	 * Set the values for the offering
	 * @param index
	 * @param number
	 * @param takenSpots
	 * @param totalSpots
	 */
	public void setOffering(int index, int secNum, int takenSpots, int totalSpots)
	{
		offeringSecNum[index] = secNum;
		offeringSpots[index][0] = takenSpots;
		offeringSpots[index][1] = totalSpots;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public int getOfferingSecNumber(int index)
	{
		return offeringSecNum[index];
	}
	
	public int getOfferingTotalSpots(int index)
	{
		return offeringSpots[index][1];
	}
	
	public int getOfferingTakenSpots(int index)
	{
		return offeringSpots[index][0];
	}
	
}
