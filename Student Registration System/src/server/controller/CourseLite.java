package server.controller;
import java.io.Serializable;

/**
 * Trimmed down version of course class to use for sending over a socket
 * @author Jerome Gobeil
 *
 */
public class CourseLite implements Serializable{

	/**
	 * The course name
	 */
	String name;
	
	/**
	 * The course number
	 */
	int number;
	
	/**
	 * Offering section number
	 */
	int[] offeringSecNum;
	/**
	 * Number of offerings
	 */
	int offeringCount;
	
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
		this.offeringCount = offeringCount;
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
	
	/**
	 * Getter for course name
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Getter for course Number
	 * @return the course number
	 */
	public int getNumber()
	{
		return number;
	}
	
	/**
	 * Get the number of offerings
	 * @return the number of offerings
	 */
	public int getOfferingCount()
	{
		return offeringCount;
	}
	
	/**
	 * Get offering section number by index
	 * @param index
	 * @return offering section number
	 */
	public int getOfferingSecNumber(int index)
	{
		return offeringSecNum[index];
	}
	
	/**
	 * Get offering total spots by index
	 * @param index
	 * @return offerings total spots
	 */
	public int getOfferingTotalSpots(int index)
	{
		return offeringSpots[index][1];
	}
	
	/**
	 * Get offering taken spots by index
	 * @param index
	 * @return offerings taken spots
	 */
	public int getOfferingTakenSpots(int index)
	{
		return offeringSpots[index][0];
	}
	
}
