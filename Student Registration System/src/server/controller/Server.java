package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import server.model.CourseCatalogue;
import server.model.DBManager;

/**
 * Class will create and monitor a port. When a new client joins it will create a server app instance for them
 * @author Jerome Gobeil
 *
 */
public class Server {

	//The servers socket
	ServerSocket socket;
	
	//The course catalogue
	CourseCatalogue catalogue;
	
	//The database manager
	DBManager db;
	
	//The number of connections
	int clientCount;
	
	/**
	 * Makes a server with the passed port Number
	 * @param portNumber 
	 */
	public Server(int portNumber)
	{
		clientCount = 0;
		
		//Start the server socket
		try
		{
			socket = new ServerSocket(portNumber);
			
			System.out.println("Server Started");
		}
		catch (IOException e)
		{
			System.out.println("Error Starting Server: " + e.getMessage());
			System.exit(0);
		}
		
		//Make the course catalogue and the database
		db = new DBManager();
		catalogue = new CourseCatalogue();
		catalogue.loadFromDataBase(db);
		
		
		//Fill the catalogue with students???
		db.sampleDBTest(catalogue);
	}
	
	/**
	 * Monitors the socket and creates a new server class whenever a new client joins
	 */
	private void monitorSocket()
	{
		while (true)
		{
			try
			{
				//Wait untill a new client is found
				Socket clientSocket = socket.accept();
				ObjectOutputStream clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream clientInput = new ObjectInputStream(clientSocket.getInputStream());
				
				clientCount += 1;
				
				//Create a server app to run on a different thread and deal with the client
				ServerApp serverApp = new ServerApp(clientCount,clientInput,clientOutput,catalogue,db);
				
				//Start the thread for the server
				//Beep boop
				
			}
			catch (IOException e)
			{
				System.err.println("Error Connecting to a client: " + e.getMessage());
			}
		}
		
	}
	
	/**
	 * Generate the server
	 */
	public static void main (String [] args)
	{
		Server s = new Server(8007);
		s.monitorSocket();
	}
	
}
