package server.model;
import java.util.ArrayList;

/**
 * Represents a specific offering of the course, like a lecture section
 *
 */
public class CourseOffering {
	
	private int secNum;
	private int secCap;
	private Course theCourse;
	private ArrayList<Student> studentList;
	private ArrayList <Registration> offeringRegList;
	private final int minimumStudents = 8;
	
	
	
	/**
	 * Returns the number of people registered in the course.
	 * @return An integer representing the number of people registered.
	 */
	public int numberOfRegs() {
		return offeringRegList.size();
	}
	
	/**
	 * Returns the number of remaining spots in the offering
	 * @return
	 */
	public int remainingSpots()
	{
		return secCap - offeringRegList.size();
	}
	
	/**
	 * Returns the course's name.
	 * @return A string representing the name of the course.
	 */
	public String getCourseName() {
		return theCourse.getCourseName();
	}
	
	/**
	 * Getter for the course number
	 * @return
	 */
	public int getCourseNum() {
		return theCourse.getCourseNum();
	}
	
	/**
	 * Adds a student to the studentList. Should only be called from Registration.
	 * @param s Student to be added.
	 */
	public void addStudent(Student s) {
		studentList.add(s);
	}
	
	/**
	 * Removes a student from the studentList. Should only be called from Registration
	 * @param s Student to be removed.
	 */
	public void removeStudent(Student s) {
		studentList.remove(s);
	}
	
	/**
	 * Constructs a course offering with the given section number and cap.
	 * @param secNum Lecture Number
	 * @param secCap Maximum number of students in this lecture.
	 */
	public CourseOffering (int secNum, int secCap) {
		this.secNum = secNum;
		this.secCap = secCap;
		offeringRegList = new ArrayList <Registration>();
		studentList = new ArrayList<Student>();
	}
	
	/**
	 * Returns the section number
	 * @return An integer representing lecture number.
	 */
	public int getSecNum() {
		return secNum;
	}
	
	/**
	 * Returns the section cap 
	 * @return Section cap (maximum number of students in this section)
	 */
	public int getSecCap() {
		return secCap;
	}

	/**
	 * Returns this offering's course
	 * @return The course represented
	 */
	public Course getTheCourse() {
		return theCourse;
	}
	
	/**
	 * Sets the course to the chosen course.
	 * @param theCourse
	 */
	public void setTheCourse(Course theCourse) {
		this.theCourse = theCourse;
	}
	@Override
	public String toString () {
		String st = "\n";
		st += getTheCourse().getCourseName() + " " + getTheCourse().getCourseNum() + "\n";
		st += "Section Num: " + getSecNum() + ", section cap: "+ getSecCap() +"\n";
		return st;
	}
	
	/**
	 * Returns a string representing a list of students in registered in this offering.
	 * @return A string with list of students registered in this offering.
	 */
	public String studentList() {
		String st = "\n";
		for(Student s: studentList)
			st += s.toString() + "\n";
		return st;
	}
	
	/**
	 * Adds a registration to the course.
	 * @param registration Registration to be added
	 */
	public void addRegistration(Registration registration){
		
		offeringRegList.add(registration);
	}
	
	/**
	 * Removes a registration from the course.
	 * @param registration Registration to be removed
	 */
	public void removeRegistration(Registration registration) {
		offeringRegList.remove(registration);
	}
	
	/**
	 * Determines if the course has enough students to run
	 * @return True if there are enough students registered to run the course, false otherwise.
	 */
	public boolean canRun() {
		if(numberOfRegs()>= minimumStudents)
			return true;
		return false;
	}
	
	/**
	 * Returns true if this offering has no course assigned to it yet.
	 * @return T if the offering has no course, F if it does.
	 */
	public boolean hasNoCourse() {
		if (theCourse == null)
			return true;
		return false;
	}
	
	/**
	 * Checks if this offering's course is the same as the course being passed.
	 * @param c The course that must be compared
	 * @return True if they are the same course, false if they aren't
	 */
	public boolean isCourse(Course c) {
		if(theCourse.compareTo(c)==0)
			return true;
		return false;
	}
	
	/**
	 * Checks if the offering's lecture number is the same as passed integer
	 * @param i Number to compare lecture number to
	 * @return True if they're the same, false otherwise.
	 */
	public boolean isLectureNumber(int i) {
		if(secNum == i)
			return true;
		return false;
	}

}
