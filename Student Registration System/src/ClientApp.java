import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Communicates through the port to the server app
 * @author Jerome Gobeil
 *
 */
public class ClientApp {

	//The socket
	Socket socket;
	
	//Input and output stream
	ObjectInputStream serverIn;
	ObjectOutputStream serverOut;
	
	//While true the server will comunicate
	Boolean keepCommunicating;
	
	//Connection to the GUI?
	
	/**
	 * Create a new client app and connect to the port
	 * @param portNumber
	 */
	public ClientApp(String serverName, int portNumber)
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
		
		//A test functionality
		sendMessage("Im in dude");
		attemptLogin(300769, "1234");
		addCourse("ENGG", 202, 0);
		addCourse("PHYS", 259, 0);
		removeCourse("ENGG", 202);

	}
	
	/**
	 * Class that communicates with the server and deals with anything it receives
	 */
	private void comunicate()
	{
		while(keepCommunicating)
		{
			try
			{
				//Listen for info from the server
				Package<?> pac = (Package<?>)serverIn.readObject();
				
				//Deal with the package
				dealWithPackage(pac);
			}
			catch (Exception e)
			{
				System.err.println("Error comunicating with server: " + e.getMessage());
				break;
			}
		}
		
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
	 * Deals with the given package
	 * @param pac
	 */
	private void dealWithPackage(Package pac)
	{
		switch(pac.getType())
		{
		
			//For testing purposes, just sends a message to print
			case MESSAGE:
				System.out.println(pac.getData());
				break;

			//Get the result from trying to login
			case LOGINRESULT:
					
				//Just a placeholder, would change GUI here
				System.out.println("Result from trying to login: " + pac.getData());
				
				break;
			
			//Get a new schedule
			case SCHEDULE:
				
				CourseLite[] schedule = (CourseLite[])pac.getData();
				
				//Testing 
				System.out.println("Schedule length is: ");
				
				//Update the GUI from here
				
				break;
				
			case CATALOGUE:
				
				CourseLite[] catalogue = (CourseLite[])pac.getData();
				//Make pop up or whatever
				
				break;
				
			case COURSE:
				
				CourseLite course = (CourseLite)pac.getData();
				//Update GUI or whatever the fuck you want really
				
				break;

		}
	}
	
	/**
	 * Sends a request for the catalogue to be sent
	 */
	public void requestCatalogue()
	{
		//Make package
		Package pac = new Package(PackageType.REQUESTCOURSECATALOGUE, null);
				
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends a request for the schedule to be sent
	 */
	public void requestSchedule()
	{
		//Make package
		Package pac = new Package(PackageType.REQUESTSCHEDULE, null);
		
		//Send package
		sendPackage(pac);
	}
	
	/**
	 * Sends a login request with id and password
	 * @param id
	 * @param password
	 */
	public void attemptLogin(int id, String password)
	{
		//Make package
		String[] info = {Integer.toString(id),password};
		Package pac = new Package(PackageType.LOGINREQUEST, info);
				
		//Send message
		sendPackage(pac);
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
	}
	
	/**
	 * Sends a message to the server to add a certain course
	 * @param courseName
	 * @param courseNumber
	 * @param offeringIndex
	 */
	public void addCourse(String courseName, int courseNumber, int offeringIndex)
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber), Integer.toString(offeringIndex)};
		Package pac = new Package(PackageType.ADDCOURSE, info);
		
		//Send message
		sendPackage(pac);
	}
	
	/**
	 * Sends a message to the server to try and find a certain course
	 * @param courseName
	 * @param courseNumber
	 */
	public void findCourse(String courseName, int courseNumber)
	{
		//Make package
		String[] info = {courseName,Integer.toString(courseNumber)};
		Package pac = new Package(PackageType.FINDCOURSE, info);
		
		//Send message
		sendPackage(pac);
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
	
	/**
	 * Ends communication with the server
	 */
	public void endCommunication()
	{
		keepCommunicating = false;
	}

	/**
	 * Simply starts the client and makes it comunicate with the server
	 * @param args
	 */
	public static void main (String [] args) {
		ClientApp client = new ClientApp("localhost", 9090);
		client.comunicate();
	}
	
}
