package server.model;
import java.util.ArrayList;

/**
 * Class created for each student to keep track of registrations
 * @author Joel Happ + Jerome Gobeil
 *
 */
public class Student {
	
	private String studentName;
	private int studentId;
	private String password;
	private ArrayList<Registration> studentRegList;
	private final int MAXCOURSENUMBER = 6;
	
	/**
	 * Returns number of courses the student has registered in.
	 * @return Number of registrations.
	 */
	public int numberOfRegistrations() {
		return studentRegList.size();
	}
	
	/**
	 * Get the registered offering based on the given index
	 * @param index
	 * @return the offering
	 */
	public CourseOffering getOfferingByIndex(int index)
	{
		return studentRegList.get(index).getTheOffering();
	}
	
	/**
	 * Constructs a student with the given name and id.
	 * @param studentName Name of student
	 * @param studentId Student's id number
	 * @param password
	 */
	public Student (String studentName, int studentId, String password) {
		this.setStudentName(studentName);
		this.setStudentId(studentId);
		this.password = password;
		studentRegList = new ArrayList<Registration>();
	}
	
	/**
	 * Generates a string representing all the registrations this student has
	 * @return a String with the .toString() of all the registrations
	 */
	public String stringAllRegs() {
		String st = "";
		for(Registration r:studentRegList) {
			st += "\n" + r;
		}
		return st;
	}
	
	
	/**
	 * Searches for a registration by course, returns the registration if found.
	 * @param c The course whose registration is being searched for.
	 * @return The registration matching the course, or null if there is none.
	 */
	public Registration getRegistrationByCourse(Course c) throws Exception{
		for(Registration r:studentRegList) {
			if(r.isForCourse(c))
				return r;
		}
		throw new Exception("Student not registered in course");
	}
	
	/**
	 * Registers a student in a course, if the student is able to.
	 * @param co Course Offering to register the student in.
	 * @throws Exception If the maximum number of registrations for this student has been reached or if student is already in course
	 */
	public void addCourseOffering(CourseOffering co) throws Exception {
		
		//Check if the student is already in the course
		if(checkEnrolled(co.getTheCourse()) != -1)
			throw new Exception("Student already registered in course");
		
		if(numberOfRegistrations()>=MAXCOURSENUMBER) {
			throw new Exception("Cannot register in course offering - maximum number of registrations has been reached."
					+ "\n To register in this course offering, you must drop out from another course.");
		}
		new Registration(this, co);
	}
	
	
	/**
	 * Removes a course from the student
	 * @param c Course to remove
	 */
	public void removeCourse(Course c) throws Exception{
		getRegistrationByCourse(c).removeRegistration();
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	@Override
	public String toString () {
		String st = "Student Name: " + getStudentName() + "\n" +
				"Student Id: " + getStudentId() + "\n\n";
		return st;
	}

	/**
	 * Adds a registration to this student. Should only be called from Registration class.
	 * @param registration The registration to add.
	 */
	public void addRegistration(Registration registration) {
		studentRegList.add(registration);
	}
	
	/**
	 * Removes a registration from this student. Should only be called from Registration class.
	 * @param registration The registration to remove.
	 */
	public void removeRegistration(Registration registration) {
		studentRegList.remove(registration);
	}
	
	/**
	 * Checks if the student is enrolled in the passed course
	 * @param c the course to check for
	 * @return -1 if not enrolled, else it returns the section number of the student
	 */
	public int checkEnrolled(Course c)
	{
		for(Registration r : studentRegList)
		{
			if (r.getTheOffering().getTheCourse() == c)
				return r.getTheOffering().getSecNum();
		}
		return -1;
	}
	
	/**
	 * Checks the students password, returns true if correct
	 * @param password
	 * @return true if correct, false if not
	 */
	public Boolean checkPassword(String password)
	{
		if (this.password.equals(password))
			return true;
		return false;
	}

}
