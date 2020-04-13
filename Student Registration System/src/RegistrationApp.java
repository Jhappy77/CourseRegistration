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
	public void addCourseToStudent(String courseName, int courseNumber, int offeringSecNumber) {
		
		//Check if student is selected
		if (selectedStudent != null)
		{
			try {
				//Find course and add it to the student
				selectedStudent.addCourseOffering(getCourseOffering(courseName, courseNumber, offeringSecNumber));
			}catch(Exception e) {
				System.err.println("Error trying to add course " + e.getMessage());
			}
		}
		else
			System.err.println("Trying to add a course when no student selected");
		
	}
	
	/**
	 * Finds the course offering from the given course
	 * @param courseName
	 * @param courseNumber
	 * @param offeringSecNumber
	 * @return the offering
	 */
	private CourseOffering getCourseOffering(String courseName, int courseNumber, int offeringSectionNumber) throws Exception{
		return getCourse(courseName, courseNumber).getCourseOfferingBySecNum(offeringSectionNumber);
	}
	
	/**
	 * Finds the course corresponding to the given name and number
	 * @param courseName
	 * @param courseNumber
	 * @return the course
	 */
	private Course getCourse(String courseName, int courseNumber) throws Exception
	{
		return cat.searchCatalogue(courseName, courseNumber);
	}
	
	/**
	 * Returns a list of all the students registrations
	 * @return CourseLite[] of the courses the student is in
	 */
	public CourseLite[] getSchedule()
	{
		if (selectedStudent != null)
		{
			//If no registrations return null
			if (selectedStudent.numberOfRegistrations() == 0)
				return null;
			
			//Make a list for the courses
			CourseLite[] courseList = new CourseLite[selectedStudent.numberOfRegistrations()];
			
			//For each course make a CourseLite for it and add registered section
			for (int i = 0; i < selectedStudent.numberOfRegistrations(); i++)
			{
				CourseOffering off = selectedStudent.getOfferingByIndex(i);
				
				courseList[i] = new CourseLite(off.getTheCourse().getCourseName(),off.getTheCourse().getCourseNum(),1);
				courseList[i].setOffering(0, off.getSecNum(), off.getSecCap(), off.studentList().length());
			}
			
			return courseList;
		}
		else
			System.err.println("Trying to get schedule when no student selected");
		
		return null;
	}
	
	/**
	 * Makes a array of courseLite for every single course in the catalogue
	 * @return the array of course lite
	 */
	public CourseLite[] getEntireCourseList()
	{
		//If there aren't any course just return null
		if (cat.getCourseCount() == 0)
			return null;
		
		CourseLite[] courseList = new CourseLite[cat.getCourseCount()];
		
		//Make a courselite for every course
		for (int i = 0; i < cat.getCourseCount(); i++)
		{
			courseList[i] = makeCourseLite(cat.getCourseByIndex(i));
		}
		
		return courseList;
	}
	
	/**
	 * Finds the course corresponding to the given name and number, returns null if course cant be found
	 * @param courseName
	 * @param courseNumber
	 * @return the course lite class
	 */
	public CourseLite findCourse(String courseName, int courseNumber)
	{
		Course c;
		
		//Try and get the course
		try 
		{
			c = getCourse(courseName, courseNumber);
		}
		
		//If course can't be found return a null course
		catch (Exception e)
		{
			return null;
		}
		
		//Make a course lite and return it
		return makeCourseLite(c);
	}
	
	/**
	 * Makes a course lite object and returns it
	 * @param c the course
	 * @return the courseLite
	 */
	private CourseLite makeCourseLite(Course c)
	{
		CourseLite newCourse = new CourseLite(c.getCourseName(),c.getCourseNum(),c.getNumOfferings());
		
		for (int i = 0; i < c.getNumOfferings(); i++)
		{
			try
			{
				newCourse.setOffering(i,c.getCourseOfferingByIndex(i).getSecNum(), c.getCourseOfferingByIndex(i).studentList().length(), c.getCourseOfferingByIndex(i).getSecCap());
			}
			catch (Exception e)
			{
				System.err.println("Error getting course offerings: " + e.getMessage());
			}
		}
		
		return newCourse;
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

}
