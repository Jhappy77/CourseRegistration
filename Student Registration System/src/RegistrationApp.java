import java.util.Scanner;


/**
 * Class to deal with all the clients courses
 * @author Joel Happ + Jerome Gobeil
 *
 */
public class RegistrationApp {

	private CourseCatalogue cat;
	private DBManager db;
	
	Student selectedStudent;
	
	public RegistrationApp(CourseCatalogue cat, DBManager db) {
		this.cat = cat;
		this.db = db;
	}
	
	/**
	 * Remove selected course from the selected student
	 * @param student
	 */
	public void removeCourseFromStudent(String courseName, int courseNumber) {
		if (selectedStudent != null)
		{
			try {
				selectedStudent.removeCourse(getCourse(courseName, courseNumber));
			} catch(Exception e) {
				System.err.println("Error removing course from student: " + e.getMessage());
			}
		}
		else
			System.err.println("Trying to remove course from null student");
	}


	/**
	 * Adds the passed offering to the selected student
	 */
	public void addCourseToStudent(String courseName, int courseNumber, int courseOffering) {
		
		//Check if student is selected
		if (selectedStudent != null)
		{
			try {
				//Find course and add it to the student
				selectedStudent.addCourseOffering(getCourseOffering(courseName, courseNumber, courseOffering));
			}catch(Exception e) {
				System.err.println("Error trying to add course " + e.getMessage());
			}
		}
		else
			System.err.println("Trying to add a course when no student selected");
		
	}
	
//	private void viewAllCoursesStudent() {
//		System.out.println(getStudent().stringAllRegs());
//	}
	
	private void viewAllCoursesCatalogue() {
		System.out.println(cat);
	}
	
	private void viewAllStudents() {
		db.printAllStudents();
	}
	
	/**
	 * Finds the course offering from the given course
	 * @param courseName
	 * @param courseNumber
	 * @param offeringIndex
	 * @return the offering
	 */
	private CourseOffering getCourseOffering(String courseName, int courseNumber, int offeringIndex) throws Exception{
		return getCourse(courseName, courseNumber).getCourseOfferingByNum(offeringIndex);
	}
	
	/**
	 * Finds the course corresponding to the given name and number
	 * @param courseName
	 * @param courseNumber
	 * @return the course
	 */
	public Course getCourse(String courseName, int courseNumber) throws Exception
	{
		return cat.searchCatalogue(courseName, courseNumber);
	}
	
	/**
	 * Finds a student by ID and checks if their password is correct
	 * @param id
	 * @param password
	 * @return true if success, false if fail
	 */
	public Boolean validateStudent(int id, String password) {
		selectedStudent = db.getStudent(id);
		if(selectedStudent==null) { 
			return false;
		}
		else
		{
			if (selectedStudent.checkPassword(password))
			{
				return true;
			}
			else
			{
				selectedStudent = null;
				return false;
			}
		}
	}
	
	
	///////////////////// SCANNERS //////////////////////
//	private String scanStudentName() {
//		System.out.println("Please enter the name of the student:");
//		return scan.nextLine();
//	}
//	private String scanCourseName() {
//		System.out.println("Please enter the name of the course: (e.g. ENSF)");
//		return scan.nextLine().trim().toUpperCase();
//	}
//	
//	private int scanCourseNumber() {
//		System.out.println("Please enter the number of the course: (e.g. 409)");
//		return scan.nextInt();
//	}

}
