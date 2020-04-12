import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class to directly deal with the client
 * @author Jerome Gobeil
 *
 */
public class ServerApp {

	//Input for the client
	ObjectInputStream clientIn;
	
	//Output for the client
	ObjectOutputStream clientOut;
	
	//Registration app for this client
	RegistrationApp reg;
	
	
	/**
	 * Constructor for the server app
	 * @param in
	 * @param out
	 */
	public ServerApp(ObjectInputStream in, ObjectOutputStream out, CourseCatalogue cat, DBManager db)
	{
		reg = new RegistrationApp(cat,db);
		clientIn = in;
		clientOut = out;
		
		System.out.println("Connected to a Client");
		
		//For testing purposes, should be run from a thread
		Execute();
	}
	
	//Monitors the input and deals with anything that comes up
	public void Execute()
	{
		//Wait for a package to come in
		while (true)
		{
			try
			{
				//Get package
				Package pac = (Package)clientIn.readObject();
				
				//Deal with it
				dealWithPackage(pac);
			}
			
			//Catch any errors
			catch (Exception e)
			{
				System.out.println("Error reading package from client: " + e.getMessage());
			}
			
		}
	}
	
	/**
	 * Deals with the package
	 * @param pac
	 */
	private void dealWithPackage(Package pac)
	{
		switch(pac.getType())
		{
			//Just print if its a message
			case MESSAGE:
				System.out.println(pac.getData());
				break;
				
				
			//Check id and password
			case LOGINREQUEST:
				//Get Data
				String[] l = (String[]) pac.getData();
					
				//Check if valid and return the result
				loginResult(reg.validateStudent(Integer.parseInt(l[0]), l[1]));
				break;

				
			//Add a course to the student
			case ADDCOURSE:
				//Get Data
				String[] a = (String[]) pac.getData();
					
				//Add the course to the student
				reg.addCourseToStudent(a[0], Integer.parseInt(a[1]), Integer.parseInt(a[2]));
				
				//Send updated schedule back to student
				
				break;

			//Remove a course from the student
			case REMOVECOURSE:
				//Get Data
				String[] r = (String[]) pac.getData();
				
				//Remove the course
				reg.removeCourseFromStudent(r[0], Integer.parseInt(r[1]));
				
				//Send updated schedule back to the student
				
				break;
				
		}
		
		
	}
	
	/**
	 * Sends a message to the client with the result of the login attempt
	 * @param result
	 */
	private void loginResult(Boolean result)
	{
		//Make package
		Package pac = new Package(PackageType.LOGINRESULT, result);
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Send the passed package to the client
	 * @param pac
	 */
	private void sendPackage(Package pac)
	{
		try
		{
			clientOut.writeObject(pac);
		}
		catch (IOException e)
		{
			System.err.println("Error sending package to client: " + e.getMessage());
		}
		
	}
	
}
