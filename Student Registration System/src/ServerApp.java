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
	
	//The client number to help keep track of the client when there's multiple connections
	int clientNumber;
	
	//Registration app for this client
	RegistrationApp reg;
	
	
/**
 * Constructor for the server app
 * @param clientNumber
 * @param in input stream to the client
 * @param out output stream to the client
 * @param cat course catalogue
 * @param db manager
 */
	public ServerApp(int clientNumber, ObjectInputStream in, ObjectOutputStream out, CourseCatalogue cat, DBManager db)
	{
		this.clientNumber = clientNumber;
		reg = new RegistrationApp(cat,db);
		clientIn = in;
		clientOut = out;
		
		System.out.println("Client " + clientNumber + ": Connected");
		
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
				Package<?> pac = (Package<?>)clientIn.readObject();
				
				//Deal with it
				dealWithPackage(pac);
			}
			
			//Catch any errors, if no longer connected to a socket stop looping
			catch (Exception e)
			{
				System.out.println("Client " + clientNumber + ": lost connection to socket: " + e.getMessage());
				break;
			}
			
		}
		
		System.out.println("Client " + clientNumber + ": Ending connection");
		
		//Try closing off communication
		try
		{
			clientOut.close();
			clientIn.close();
		}
		catch (IOException e)
		{
			System.err.println("Client " + clientNumber + ": Error Closing IN/OUT line: " + e.getMessage());
		}
	}
	
	/**
	 * Deals with the package
	 * @param pac
	 */
	private void dealWithPackage(Package<?> pac)
	{
		switch(pac.getType())
		{
			//Just print if its a message
			case MESSAGE:
				System.out.println("Client " + clientNumber + " Message: " + pac.getData());
				break;
				
				
			//Check id and password
			case LOGINREQUEST:
				System.out.println("Client " + clientNumber + ": Received Login Request");
				
				//Get Data
				String[] l = (String[]) pac.getData();
					
				//Check if valid and return the result
				sendLoginResult(reg.validateStudent(Integer.parseInt(l[0]), l[1]));
				break;

				
			//Add a course to the student
			case ADDCOURSE:
				System.out.println("Client " + clientNumber + ": Received Add Course");
				
				//Get Data
				String[] a = (String[]) pac.getData();
					
				//Add the course to the student
				reg.addCourseToStudent(a[0], Integer.parseInt(a[1]), Integer.parseInt(a[2]));
				
				//Send updated schedule
				sendSchedule();
				break;

			//Remove a course from the student
			case REMOVECOURSE:
				System.out.println("Client " + clientNumber + ": Received Remove Course");
				
				//Get Data
				String[] r = (String[]) pac.getData();
				
				//Remove the course
				reg.removeCourseFromStudent(r[0], Integer.parseInt(r[1]));
				
				//Send updated schedule
				sendSchedule();
				break;
				
			//Just send back their schedule
			case REQUESTSCHEDULE:
				System.out.println("Client " + clientNumber + ": Received Request Schedule");
				
				//Send the schedule
				sendSchedule();
				break;
				
			//Find a requested course, must be completed
			case FINDCOURSE:
				System.out.println("Client " + clientNumber + ": Received Find Course");
				
				//Try and find course, if cant send null
				sendCourse((String[])pac.getData());
				break;
				
			//Just send the entire list of all courses
			case REQUESTCOURSECATALOGUE:
				System.out.println("Client " + clientNumber + ": Received Request Catalogue");
				
				sendCatalogue();
				break;
				
		}
	}
	
	private void sendCatalogue()
	{
		System.out.println("Client " + clientNumber + ": Sending Catalogue");
		
		//Make the package
		Package pac = new Package(PackageType.CATALOGUE, reg.getEntireCourseList());
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends a courseLite object to the client for display
	 * @param f is the data from the find course packet
	 */
	private void sendCourse(String[] f)
	{
		System.out.println("Client " + clientNumber + ": Sending Course");
		
		CourseLite c;
		try
		{
			c = reg.findCourse(f[0],Integer.parseInt(f[1]));
		}
		catch (Exception e)
		{
			c = null;
		}
		
		//Make package
		Package pac = new Package(PackageType.COURSE, c);
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends the students schedule
	 */
	private void sendSchedule()
	{
		System.out.println("Client " + clientNumber + ": Sending Schedule");
		
		//Make package
		Package pac = new Package(PackageType.SCHEDULE, reg.getSchedule());
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends a message to the client with the result of the login attempt
	 * @param result
	 */
	private void sendLoginResult(Boolean result)
	{
		System.out.println("Client " + clientNumber + ": Sending Login Result");
		
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
			System.err.println("Client " + clientNumber + ": Error sending package: " + e.getMessage());
		}
		
	}
	
}
