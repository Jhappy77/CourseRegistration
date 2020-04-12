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
		
		//Send a test message
		try 
		{
			Package p = new Package(PackageType.MESSAGE, "Test Message");
			serverOut.writeObject(p);
		}
		catch (IOException e)
		{
			System.err.println("Error sending package to server: " + e.getMessage());
		}
		
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
				Package pac = (Package)serverIn.readObject();
				
				//Deal with the package
				
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
		
			//Just print if its a message
			case MESSAGE:
				System.out.println(pac.getData());
				break;

			//Get the result from trying to login
			case LOGINRESULT:
					
				//Just a placeholder, would change GUI here
				System.out.println("Result from trying to login: " + pac.getData());
				break;

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
