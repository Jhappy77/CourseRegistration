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
 * 
 * 
 * We will be rewriting this class to change its relationship with Controller.
 * In the future, it will no longer have an association relationship with controller,
 * and the dealWithPackage function will be split into separate functions.
 */


public class ClientPort {

	//The socket
	Socket socket;
	
	//Input and output stream
	ObjectInputStream serverIn;
	ObjectOutputStream serverOut;
	
	//While true the client will communicate with server
	Boolean keepCommunicating;
	
	//Connection to the GUI?
	
	/**
	 * Create a new client app and connect to the port
	 * @param portNumber
	 */
	public ClientPort(String serverName, int portNumber)
	{
		
		keepCommunicating = true;
		
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
		
//		//TESTING FUNCTIONALITY, REMOVE LATER
//		sendMessage("Im in dude");
//		attemptLogin(300769, "6969");
//		attemptLogin(300769, "1234");
//		findCourse("NotReal", 42069);
//		findCourse("ENGG", 202);
//		addCourse("ENGG", 202, 1);
//		addCourse("PHYS", 259, 2);
//		removeCourse("ENGG", 202);
//		requestCatalogue();

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
	 * Class that communicates with the server and deals with anything it receives
	 */
	void communicate()
	{
		while(keepCommunicating)
		{
			try
			{
				//Listen for info from the server
				Package<?> pac = (Package<?>)serverIn.readObject();
				
				//Deal with the package
				//dealWithPackage(pac);
			}
			catch (Exception e)
			{
				System.err.println("Error comunicating with server: " + e.getMessage());
				break;
			}
		}
		// Ends communication with Server
		closeAll();
		
	}
	
	
	////////////////////////////////////////////////////////////INTERACTION WITH GUI STARTS HERE////////////////////////////////////////////////
	//IF creating new packages or getting errors from them check out the PackageType enum to see what the data contains
	
	/**
	 * (DEPRECATED) - DELETE EVENTUALLY
	 * We most likely won't need this class anymore but I left it in case
	 * 
	 * Deals with the given package
	 * @param pac
	 *//*
	private void dealWithPackage(Package pac)
	{
		switch(pac.getType())
		{
		
			//Just prints a message if received, mostly for testing purposes (Could be used for errors with a pop up window?);
			case MESSAGE:
				System.out.println(pac.getData());
				break;

			//Get the result from trying to login
			case LOGINRESULT:
					
				//pac.getData() is a Boolean, true if login success (correct username/password) and false if incorrect
				//When correct username and password inputed the student is automatically selected
				
				System.out.println("Result from trying to login: " + pac.getData());
				
				break;
			
			//Get a new schedule
			case SCHEDULE:
				
				CourseLite[] schedule = (CourseLite[])pac.getData();
				
				//pac data is a array of courseLite objects, the courseLite objects will only have one offering which is the one the student is registered in
				//To access the offerings data/info just use functions getOfferingTotalSpots(0), getOfferingSecNum(0), etc (Check out courseLite class for details)
				
				//TESTING
				if (schedule == null)
					System.out.println("Schedule empty");
				else
					System.out.println("Schedule length is: " + schedule.length);
				
				break;
				
			case CATALOGUE:
				
				CourseLite[] catalogue = (CourseLite[])pac.getData();
				
				//Pac data contains a array of course lite objects, the courseLite object contains all the info about the course including a list of all the offerings
				//Check out the courseLite class to find all the getters
				
				//TESTING
				if (catalogue == null)
					System.out.println("Catalogue is empty");
				else
					System.out.println("Catalogue contains " + catalogue.length + " courses");
				
				
				break;
				
			case COURSE:
				
				//Pac data is a single courseLite object, contains all the info about the course and its offerings
				//Check out courseLite class for the getters
				
				//TESTING

				break;

		}
	}*/
	
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
		
		// !! Need to update this to get proper exception message,
		// !! based on the error with logon. (Wrong password,
		// !! (account doesn't exist)
		
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
	public void removeCourse(String courseName, int courseNumber)
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber)};
		Package pac = new Package(PackageType.REMOVECOURSE, info);
		
		//Send message
		sendPackage(pac);
		
		// !! Add functionality to get confirmation of success
		// !! as well as error messages if necessary
		
	}
	
	/**
	 * Sends a message to the server to add a certain course
	 * @param courseName
	 * @param courseNumber
	 * @param offeringSecNumber
	 */
	public void addCourse(String courseName, int courseNumber, int offeringSecNumber)
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber), Integer.toString(offeringSecNumber)};
		Package pac = new Package(PackageType.ADDCOURSE, info);
		
		//Send message
		sendPackage(pac);
		
		// !! Add functionality to get confirmation of success
		// !! as well as error messages if necessary
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
		
		// !! I'm not sure if there are any other possible exception cases
		// !! that we need to be aware of.
	}
	
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
