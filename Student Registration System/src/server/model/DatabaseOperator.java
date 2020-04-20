package server.model;

/**
 * An interface for classes that operate the database.
 * @author Joel Happ
 */
public interface DatabaseOperator {
	
	public abstract CourseCatalogue loadDatabase();
	public abstract Student getStudent(int id);
	public abstract void saveCourse(Course c);
	// Add this line in RegApp (makeNewCourse): 
	//db.saveCourse(cat.searchCatalogue(courseName, courseNumber));
}
