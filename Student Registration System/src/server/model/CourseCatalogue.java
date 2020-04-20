package server.model;
import java.util.ArrayList;

/**
 * Class to manage the list of all the classes once the program has started
 * @author Joel Happ + Jerome Gobeil
 *
 */
public class CourseCatalogue {
	
	private ArrayList <Course> courseList;
	
	/**
	 * Constructs and initializes a course catalogue
	 */
	public CourseCatalogue () {
		courseList = new ArrayList<Course>();
	}
	
	/**
	 * Getter for the course list size
	 * @return size of the course list
	 */
	public int getCourseCount()
	{
		return courseList.size();
	}
	
	/**
	 * Get a course by its index in the course list
	 * @param index
	 * @return the course at the index
	 */
	public Course getCourseByIndex(int index)
	{
		return courseList.get(index);
	}
	
//	/**
//	 * Creates a new DBManager, and sets the course list to be the result of
//	 * reading from the database.
//	 */
//	public void loadFromDataBase(PseudoDB db) {
//		setCourseList(db.readCourses());
//	}
	
	/**
	 * Searches the course catalogue for the given course name and course number.
	 * @param courseName Name to search for, e.g. "MATH"
	 * @param courseNum Number of the course, e.g. "201"
	 * @return the course if its found, or null if its not.
	 * @exception If the course could not be found
	 */
	public Course searchCatalogue (String courseName, int courseNum) throws Exception{
		courseName = courseName.trim().toUpperCase();
		for (Course c : courseList) {
			if (courseName.equals(c.getCourseName()) &&
					courseNum == c.getCourseNum()) {
				return c;
			}	
		}
		throw new Exception ("Course could not be found");
	}
	
	/**
	 * Returns the list of courses.
	 * @return An arraylist of courses.
	 */
	public ArrayList <Course> getCourseList() {
		return courseList;
	}

	/**
	 * Sets course list to the a list of courses provided to it.
	 * @param courseList The list to be the course list.
	 */
	public void setCourseList(ArrayList <Course> courseList) {
		this.courseList = courseList;
	}
	
	
	@Override
	public String toString () {
		String st = "All courses in the catalogue: \n";
		for (Course c : courseList) {
			st += c;  //This line invokes the toString() method of Course
			st += "\n";
		}
		return st;
	}

}
