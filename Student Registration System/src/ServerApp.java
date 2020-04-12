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
		//Just print if its a message
		if (pac.getType() == PackageType.MESSAGE)
		{
			System.out.println(pac.getData());
		}
		
		//Check id and password
		else if (pac.getType() == PackageType.LOGINREQUEST)
		{
			//Split the data into id and password
			String[] s = pac.data.split(" ");
			
			//Check if valid and return the result
			loginResult(reg.validateStudent(Integer.parseInt(s[0]), s[1]));
		}
	}
	
	/**
	 * Sends a message to the client with the result of the login attempt
	 * @param result
	 */
	private void loginResult(Boolean result)
	{
		Package pac;
		if (result)
		{
			pac = new Package(PackageType.LOGINRESULT, "SUCCESS");
		}
		else
		{
			pac = new Package(PackageType.LOGINRESULT, "FAIL");
		}
		
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
