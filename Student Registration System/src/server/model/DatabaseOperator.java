package server.model;

/**
 * An interface for classes that operate the database.
 * @author Joel Happ
 */
public interface DatabaseOperator {
	
	public abstract CourseCatalogue loadDatabase();
	public abstract Student getStudent(int id);
	
}
