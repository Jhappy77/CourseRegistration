package server.model;
import java.util.ArrayList;

/**
 * Class to keep track of each course
 * @author Joel Happ
 *
 */
public class Course implements Comparable<Course>{

	/**
	 * Name of the course
	 */
	private String courseName;
	
	/**
	 * Number for the course
	 */
	private int courseNum;
	
	/**
	 * List of pre requisites for the course
	 */
	private ArrayList<Course> preReq;
	
	/**
	 * List of all the offerings for the course
	 */
	private ArrayList<CourseOffering> offeringList;

	
	/**
	 * Constructs a course and initializes its prerequisites and offeringList.
	 * @param courseName
	 * @param courseNum
	 */
	public Course(String courseName, int courseNum) {
		this.setCourseName(courseName);
		this.setCourseNum(courseNum);
		// Both of the following are only association
		preReq = new ArrayList<Course>();
		offeringList = new ArrayList<CourseOffering>();
	}

	/**
	 * Adds an offering (Lecture) to this course. 
	 * @param offering Offering to add.
	 */
	public void addOffering(CourseOffering offering) {
		if (offering != null && offering.hasNoCourse()) {
			offering.setTheCourse(this);
			if (!(offering.getCourseName().equals(courseName))
					|| (offering.getCourseNum() != courseNum)) {
				//We may want to change how this message is displayed.
				System.err.println("Error! This section belongs to another course!");
				return;
			}
			
			offeringList.add(offering);
		}
	}
	
	/**
	 * Adds a course to the list of prerequisites for this course.
	 * @param course Course to add as a prerequisite.
	 */
	public void addPreReq(Course course) {
		if(course.getCourseName()==courseName) {
			System.err.println("Error! A course cannot have itself as a prerequisite!");
			return;
		}
		preReq.add(course);
	}

	/**
	 * Returns a string representing the full name of the course.
	 * @return A string representing full name, e.g. "MATH 277"
	 */
	public String getFullCourseName() {
		return courseName + " " + Integer.toString(courseNum);
	}
	
	/**
	 * Getter for the course name
	 * @return the name
	 */
	public String getCourseName() {
		return courseName;
	}
	
	/**
	 * Getter for the number of offerings
	 * @return the count of offerings
	 */
	public int getNumOfferings()
	{
		return offeringList.size();
	}

	/**
	 * Setter for the course name
	 * @param courseName
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * Getter for the course number
	 * @return the course number
	 */
	public int getCourseNum() {
		return courseNum;
	}

	/**
	 * Setter for the course number
	 * @param courseNum
	 */
	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
	
	/**
	 * To string override to print out the course
	 */
	@Override
	public String toString () {
		String st = "\n";
		st += getCourseName() + " " + getCourseNum ();
		st += "\nAll course sections:\n";
		for (CourseOffering c : offeringList)
			st += c;
		st += "\n-------\n";
		return st;
	}

	
	/**
	 * Gets course offering by lecture number.
	 * @param i Lecture number (Section Number) to search for
	 * @return The course offering.
	 * @exception Throws exception if the section number doesn't exist for this course.
	 */
	public CourseOffering getCourseOfferingBySecNum(int i) throws Exception {
		for(CourseOffering c: offeringList) {
			if(c.isLectureNumber(i))
				return c;
		}
		throw new Exception("Could not find the offering section number " + i);
	}
	
	/**
	 * Get a course offering by index
	 * @param i is the index
	 * @return the course offering
	 * @throws Exception if no offering at given index
	 */
	public CourseOffering getCourseOfferingByIndex(int i) throws Exception
	{
		try
		{
			return offeringList.get(i);
		}
		catch (Exception e)
		{
			throw new Exception("Could not find the offering at index " + i);
		}

	}

	/**
	 * Implements compareTo with another course, based on the name of the course.
	 * If the name is equal, return 0.
	 */
	@Override
	public int compareTo(Course c) {
		return courseName.compareTo(c.getCourseName());
	}

}
