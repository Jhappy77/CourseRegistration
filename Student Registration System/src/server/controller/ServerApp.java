package server.controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import server.model.CourseCatalogue;
import server.model.DBManager;

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
				System.out.println("Client " + clientNumber + ": Error comunicating: " + e.getMessage());
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
				sendLoginResult(l);
				break;

				
			//Add a course to the student
			case ADDCOURSE:
				System.out.println("Client " + clientNumber + ": Received Add Course");
				
				//Get data
				String[] a = (String[]) pac.getData();
				
				//Register for course and send answer back
				addCourse(a);
				
				break;

			//Remove a course from the student
			case REMOVECOURSE:
				System.out.println("Client " + clientNumber + ": Received Remove Course");
				
				//Get Data
				String[] r = (String[]) pac.getData();
				
				//Remove the course and send answer back
				removeCourse(r);

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
			
			//Just logout the student
			case LOGOUT:
				System.out.println("Client " + clientNumber + ": Received Logout");
				
				reg.deselectStudent();
				break;
				
		}
	}
	
	/**
	 * Remove the course from the selected student
	 * @param r
	 */
	private void removeCourse(String[] r)
	{
		Package ret;
		
		//Remove the course
		try
		{
			//Remove the course
			reg.removeCourseFromStudent(r[0], Integer.parseInt(r[1]));
			ret = new Package(PackageType.COURSECHANGED, "Successfully Removed Course");
			System.out.println("Client " + clientNumber + ": Sending Success Removing Course");
		}
		//If a error occurs signing up for course send that back
		catch (Exception e)
		{
			System.out.println("Client " + clientNumber +": Error removing course: " + e.getMessage());
			ret = new Package(PackageType.ERROR,e.getMessage());
		}
		
		//Send package
		sendPackage(ret);

	}
	
	/**
	 * Register the selected student for a course and send a error if it cant
	 * @param a
	 */
	private void addCourse(String[] a)
	{
		Package ret;
		
		//Add the course to the student
		try
		{
			reg.addCourseToStudent(a[0], Integer.parseInt(a[1]), Integer.parseInt(a[2]));
			ret = new Package(PackageType.COURSECHANGED, "Successfully Added Course");
			System.out.println("Client " + clientNumber + ": Sending Success Adding Course");
		}
		//If a error occurs signing up for course send that back
		catch (Exception e)
		{
			System.out.println("Client " + clientNumber +": Error adding course: " + e.getMessage());
			ret = new Package(PackageType.ERROR,e.getMessage());
		}
		
		//Send package
		sendPackage(ret);

	}
	
	/**
	 * Send the Catalogue to the client
	 */
	private void sendCatalogue()
	{
		
		Package pac;
		
		//Make the package
		try
		{
			 pac = new Package(PackageType.CATALOGUE, reg.getEntireCourseList());
			 System.out.println("Client " + clientNumber + ": Sending Catalogue");
		}
		
		//If an error occurs such as now courses send appropriate error to the Client
		catch (Exception e)
		{
			System.out.println("Client " + clientNumber +": Error sending catalogue: " + e.getMessage());
			pac = new Package(PackageType.ERROR, e.getMessage());
		}
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends a courseLite object to the client for display
	 * @param f is the data from the find course packet
	 */
	private void sendCourse(String[] f)
	{
		Package pac;
		
		try
		{
			pac = new Package(PackageType.COURSE, reg.findCourse(f[0],Integer.parseInt(f[1])));
			System.out.println("Client " + clientNumber + ": Sending Course");
		}
		
		//If a error occurs send a appropriate message to the client
		catch (Exception e)
		{
			System.out.println("Client " + clientNumber + ": Error Sending Course: " + e.getMessage());
			pac = new Package(PackageType.ERROR, e.getMessage());
		}
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends the students schedule
	 */
	private void sendSchedule()
	{

		//Make the package
		Package pac;
		try
		{
			pac = new Package(PackageType.SCHEDULE, reg.getSchedule());
			System.out.println("Client " + clientNumber + ": Sending Schedule");
		}
		
		//If a error occurs send a appropriate response
		catch (Exception e)
		{
			System.out.println("Client " + clientNumber + ": Error Sending Schedule: " + e.getMessage());
			pac = new Package(PackageType.ERROR, e.getMessage());
		}
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends a message to the client with the result of the login attempt
	 * @param result
	 */
	private void sendLoginResult(String[] input)
	{

		//Make package
		Package pac;
		try
		{
			pac = new Package(PackageType.LOGINRESULT, reg.validateStudent(Integer.parseInt(input[0]),input[1]));
			System.out.println("Client " + clientNumber + ": Sending Login Success");
		}
		catch (Exception e)
		{
			System.out.println("Client " + clientNumber + ": Error Logging In: " + e.getMessage());
			pac = new Package(PackageType.ERROR, e.getMessage());
		}
		 
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
