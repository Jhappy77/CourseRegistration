package server.model;

/**
 * A class that holds the information of an individual's registration in a course.
 */
public class Registration {
	private Student theStudent;
	private CourseOffering theOffering;
	private char grade;
	
	/**
	 * Constructs a registration with a student and an offering
	 * @param st Registration's student
	 * @param of Registration's course offering
	 */
	public Registration(Student st, CourseOffering of) {
		theStudent = st;
		theOffering = of;
		addRegistration();
	}
	
	/**
	 * Adds the registration to the student and the offering.
	 */
	private void addRegistration () {
		theStudent.addRegistration(this);
		theOffering.addRegistration(this);
		theOffering.addStudent(theStudent);
	}
	
	/**
	 * Removes this registration from the student's registration list,
	 * and from the course's registration list.
	 */
	public void removeRegistration() {
		this.setGrade('W');
		theStudent.removeRegistration(this);
		theOffering.removeRegistration(this);
		theOffering.removeStudent(theStudent);
	}
	
	/**
	 * Checks if this registration's offering has the same course as the one being passed.
	 * @param c Course we are checking for
	 * @return True if this registration is for the same course, false if it isn't
	 */
	public boolean isForCourse(Course c) {
		return theOffering.getTheCourse() == c;
	}
	
	/**
	 * Getter for the student
	 * @return the student
	 */
	public Student getTheStudent() {
		return theStudent;
	}
	
	/**
	 * Setter for the student
	 * @param theStudent
	 */
	public void setTheStudent(Student theStudent) {
		this.theStudent = theStudent;
	}
	
	/**
	 * Getter for the course offering
	 * @return the offering 
	 */
	public CourseOffering getTheOffering() {
		return theOffering;
	}
	
	/**
	 * Setter for the offering
	 * @param theOffering
	 */
	public void setTheOffering(CourseOffering theOffering) {
		this.theOffering = theOffering;
	}
	
	/**
	 * Getter for the grade
	 * @return the grade
	 */
	public char getGrade() {
		return grade;
	}
	
	/**
	 * Setter for the grade
	 * @param grade
	 */
	public void setGrade(char grade) {
		this.grade = grade;
	}
	
	/**
	 * Override to print the registration
	 */
	@Override
	public String toString () {
		String st = "\n";
		st += "Student Name: " + getTheStudent() + "\n";
		st += "The Offering: " + getTheOffering () + "\n";
		st += "Grade: " + getGrade();
		st += "\n-----------\n";
		return st;
	}
	
}
