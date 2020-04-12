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
	
	//Connection to the GUI?
	
	/**
	 * Create a new client app and connect to the port
	 * @param portNumber
	 */
	public ClientApp(String serverName, int portNumber)
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
		while(true)
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
			}
		}
	}

	//Start the client
	public static void main (String [] args) {
		ClientApp client = new ClientApp("localhost", 9090);
		client.comunicate();
	}
	
}
