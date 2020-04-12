import java.util.ArrayList;

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
	private int numberOfRegistrations() {
		return studentRegList.size();
	}
	
	/**
	 * Constructs a student with the given name and id.
	 * @param studentName Name of student
	 * @param studentId Student's id number
	 */
	public Student (String studentName, int studentId) {
		this.setStudentName(studentName);
		this.setStudentId(studentId);
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
	public Registration getRegistrationByCourse(Course c) {
		for(Registration r:studentRegList) {
			if(r.isForCourse(c))
				return r;
		}
		return null;
	}
	
	/**
	 * Registers a student in a course, if the student is able to.
	 * @param co Course Offering to register the student in.
	 * @throws Exception If the maximum number of registrations for this student has been reached
	 */
	public void addCourseOffering(CourseOffering co) throws Exception {
		if(numberOfRegistrations()>=MAXCOURSENUMBER) {
			// IMPORTANT! We need to change how this message is displayed.
			throw new Exception("Cannot register in course offering - maximum number of registrations has been reached."
					+ "\n To register in this course offering, you must drop out from another course.");
		}
		new Registration(this, co);
	}
	
	
	//IMPORTANT! We may want to change how this function works.
	/**
	 * Removes a course from the 
	 * @param c Course to remove
	 */
	public void removeCourse(Course c) {
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
