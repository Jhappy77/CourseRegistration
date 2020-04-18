package client.controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.view.MyGUI;
import javafx.application.Application;
import server.controller.CourseLite;
import server.controller.Package;
import server.controller.PackageType;

/**
 * Communicates through the port to the server app.
 * @author Jerome Gobeil + Joel Happ
 */
public class ClientPort {

	/**
	 * The socket connected to the server
	 */
	Socket socket;
	
	/**
	 * Server input to the port
	 */
	ObjectInputStream serverIn;
	
	/**
	 * Server output to the port
	 */
	ObjectOutputStream serverOut;
	
	/**
	 * Create a new client app and connect to the port
	 * @param portNumber
	 */
	public ClientPort(String serverName, int portNumber)
	{
		//Connect to the server
		try
		{
			socket = new Socket(serverName, portNumber);
			
			//Create input and output streams
			serverIn = new ObjectInputStream(socket.getInputStream());
			serverOut = new ObjectOutputStream(socket.getOutputStream());
		}
		//Throw an exception
		catch (IOException e)
		{
			System.err.println("Error connecting to the server: " + e.getMessage());
		}

	}
	
	/**
	 * Listens for a single response from the server, and responds accordingly.
	 */
	public Package readResponse() {
		try
		{
			//Listen for info from the server
			Package<?> pac = (Package<?>)serverIn.readObject();
			
			//Deal with the package
			return pac;
		}
		catch (Exception e)
		{
			System.err.println("Error comunicating with server: " + e.getMessage());
			closeAll();
		}
		// !! We may want to change this? Needed to include it to satisfy Java
		return null;
	}
	
	/**
	 * Attempts to close everything.
	 */
	private void closeAll() {
		//Deal with ending the communication
		System.out.println("Ended comunication with server");
		
		//Close everything
		try 
		{
			serverIn.close();
			serverOut.close();
			socket.close();
		}
		catch (IOException e)
		{
			System.err.println("Error closing socket: " + e.getMessage());
		}
	}
	
	/**
	 * Sends a request for the catalog to be sent
	 */
	public CourseLite[] requestCatalogue() throws Exception
	{
		//Make package
		Package pac = new Package(PackageType.REQUESTCOURSECATALOGUE, null);
				
		//Send package
		sendPackage(pac);
		
		// Gets package
		Package<?> resp = readResponse();
		
		// Deals with response
		switch(resp.getType()) {
		case CATALOGUE:
			CourseLite[] catalogue = (CourseLite[])resp.getData();
			if(catalogue==null)
				throw new Exception("The catalogue is empty.");
			return catalogue;
		
		//If theres an error return what kind of error it is
		case ERROR:
			throw new Exception((String)resp.getData());
		
		default:
			throw new Exception("Error communicating with server- unexpected type.");
	}
	}
	
	/**
	 * Sends a request for the schedule to be sent
	 */
	public CourseLite[] requestSchedule() throws Exception
	{
		//Make package
		Package pac = new Package(PackageType.REQUESTSCHEDULE, null);
		
		//Send package
		sendPackage(pac);
		
		//Reads response
		Package<?> resp = readResponse();
		
		switch(resp.getType()) {
			case SCHEDULE:
				CourseLite[] schedule = (CourseLite[])resp.getData();
				if(schedule==null)
					throw new Exception("Your schedule is empty.");
				return schedule;
			
			//If theres an error return what kind of error it is
			case ERROR:
				throw new Exception((String)resp.getData());
				
			default:
				throw new Exception("Error communicating with server- unexpected type");
		}

	}
	
	/**
	 * Sends a login request with id and password. 
	 * Throws an exception with a relevant message if login wasn't successful.
	 * @param id Entered student's ID to check
	 * @param password Entered password to check
	 * @return the students name if successfull
	 */
	public String attemptLogin(int id, String password) throws Exception
	{
		//Make package
		String[] info = {Integer.toString(id),password};
		Package pac = new Package(PackageType.LOGINREQUEST, info);
				
		//Send message
		sendPackage(pac);
		
		//Reads response
		Package<?> resp = readResponse();
		
		switch(resp.getType()) {
		
		case LOGINRESULT:
			
			if(resp.getData() != null)
				return (String)resp.getData();
			else
				throw new Exception("Login attempt unsuccessful");
		
		//If theres an error return what kind of error it is
		case ERROR:
			throw new Exception((String)resp.getData());
		
		default:
			throw new Exception("Error communicating with server, login unsuccessful");
		}
		
	}
	
	/**
	 * Sends a message to the server to remove a certain course from student
	 * @param courseName
	 * @param courseNumber
	 */
	public String removeCourse(String courseName, int courseNumber) throws Exception
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber)};
		Package pac = new Package(PackageType.REMOVECOURSE, info);
		
		//Send message
		sendPackage(pac);
		
		//Get response
		Package<?> resp = readResponse();
		
		switch(resp.getType()) {
		
		case COURSECHANGED:
			
			if(resp.getData() != null)
				return (String)resp.getData();
			else
				throw new Exception("Error Removing course");
		
		//If theres an error return what kind of error it is
		case ERROR:
			throw new Exception((String)resp.getData());
		
		default:
			throw new Exception("Error communicating with server, unexpected type");
		}
		
	}
	
	/**
	 * Sends a message to the server to add a certain course
	 * @param courseName
	 * @param courseNumber
	 * @param offeringSecNumber
	 */
	public String addCourse(String courseName, int courseNumber, int offeringSecNumber) throws Exception
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber), Integer.toString(offeringSecNumber)};
		Package pac = new Package(PackageType.ADDCOURSE, info);
		
		//Send message
		sendPackage(pac);
		
		//Get response
				Package<?> resp = readResponse();
				
				switch(resp.getType()) {
				
				case COURSECHANGED:
					
					if(resp.getData() != null)
						return (String)resp.getData();
					else
						throw new Exception("Error Adding course");
				
				//If theres an error return what kind of error it is
				case ERROR:
					throw new Exception((String)resp.getData());
				
				default:
					throw new Exception("Error communicating with server, unexpected type");
				}
	}
	
	/**
	 * Sends a message to the server to try and find a certain course
	 * @param courseName
	 * @param courseNumber
	 * @exception If the course couldn't be found
	 */
	public CourseLite findCourse(String courseName, int courseNumber) throws Exception
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber)};
		Package pac = new Package(PackageType.FINDCOURSE, info);
		
		//Send message
		sendPackage(pac);
		
		//Get response
		Package<?> resp = readResponse();
		
		
		switch(resp.getType()) {
		case COURSE:
			CourseLite course = (CourseLite)resp.getData();
			if(course==null)
				throw new Exception("No course found!");
			return course;
		
		//If there is a error return the message
		case ERROR:
			throw new Exception((String)resp.getData());
			
		default:
			throw new Exception("Error communicating with server, unexpected type");
		}
		
	}
	
	/**
	 * Sends a message to the server to make a course
	 * @param courseName
	 * @param courseNumber
	 * @param numberOfOfferings
	 * @param maxStudentsPerOffering
	 */
	public String makeCourse(String courseName, int courseNumber, int numberOfOfferings, int maxStudentsPerOffering) throws Exception
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber),Integer.toString(numberOfOfferings),Integer.toString(maxStudentsPerOffering)};
		Package pac = new Package(PackageType.NEWCOURSE, info);
		
		//Send message
		sendPackage(pac);
		
		//Get response
		Package<?> resp = readResponse();
		
		switch(resp.getType()) {
		
		case COURSECHANGED:
			
			if(resp.getData() != null)
				return (String)resp.getData();
			else
				throw new Exception("Error making course");
		
		//If theres an error return what kind of error it is
		case ERROR:
			throw new Exception((String)resp.getData());
		
		default:
			throw new Exception("Error communicating with server, unexpected type");
		}
		
	}
	
	// !! This needs to be called by the GUI when logging out so the server is notified
	/**
	 * Sends logout package to the server
	 */
	public void logout()
	{
		//Make package
		Package pac = new Package(PackageType.LOGOUT, null);
				
		//Send message
		sendPackage(pac);
				
		// !! Add functionality to get confirmation of success
		// !! as well as error messages if necessary
		
	}
	
	/**
	 * Sends a message to be printed on the server
	 * @param message
	 */
	public void sendMessage(String message)
	{
		//Make package
		Package pac = new Package(PackageType.MESSAGE, message);
		
		//Send message
		sendPackage(pac);
	}
	
	/**
	 * Send the passed package to the server
	 * @param pac the package
	 */
	private void sendPackage(Package pac)
	{
		try
		{
			serverOut.writeObject(pac);
		}
		catch(IOException e)
		{
			System.err.println("Error sending package to server: " + e.getMessage());
		}
	}
}
