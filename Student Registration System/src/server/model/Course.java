package server.model;
import java.util.ArrayList;

public class Course implements Comparable<Course>{

	private String courseName;
	private int courseNum;
	
	// The preReq list keeps track of all the courses that are pre-requisites for this
	// course.
	private ArrayList<Course> preReq;
	
	// The offering list keeps track of all the students in a particular offering
	// of the course.
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
	
	
	public String getCourseName() {
		return courseName;
	}
	
	public int getNumOfferings()
	{
		return offeringList.size();
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
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
