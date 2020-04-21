package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.model.CourseCatalogue;
import server.model.DatabaseOperator;
import server.model.PseudoDB;
import server.model.RealDB;

/**
 * Class will create and monitor a port. When a new client joins it will create a server app instance for them
 * @author Jerome Gobeil
 *
 */
public class Server {

	/**
	 * The socket for the server
	 */
	ServerSocket socket;

	/**
	 * The catalogue of courses
	 */
	CourseCatalogue catalogue;

	/**
	 * The database Manager
	 */
	DatabaseOperator db;

	/**
	 * The number of clients
	 */
	int clientCount;

	/**
	 * The pool of threads
	 */
	ExecutorService pool;

	/**
	 * Makes a server with the passed port Number
	 * @param portNumber
	 */
	public Server(int portNumber, int threadCount)
	{
		clientCount = 0;

		//Make the thread pool
		pool = Executors.newFixedThreadPool(10);

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

		// You can comment & uncomment the following lines of code
		// to switch between using the real database or the pseudo database.
		db = new PseudoDB();
		//db = new RealDB();


		catalogue = db.loadDatabase();

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
				pool.execute(serverApp);

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
		//Create a server to monitor port 8007 with 2 threads
		Server s = new Server(8007, 2);
		s.monitorSocket();
	}

}
