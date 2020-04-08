import java.util.ArrayList;

public class CourseCatalogue {
	
	private ArrayList <Course> courseList;
	
	public CourseCatalogue () {
		loadFromDataBase ();
	}
	
	/**
	 * Creates a new DBManager, and sets the course list to be the result of
	 * reading from the database.
	 */
	private void loadFromDataBase() {
		DBManager db = new DBManager();
		setCourseList(db.readFromDataBase());
	}
	
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
		throw new Exception ("The course that was searched for was not found.");
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
