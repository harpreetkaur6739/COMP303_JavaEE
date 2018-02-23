package network.server;

// Fig. 27.5: Server.java
// Server portion of a client/server stream-socket connection. 
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import atm.Atm;

public class Server{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int port = 8080;
	private static final int maxConnections = 100;
	
	private ObjectOutputStream output; // output stream to client
	private ObjectInputStream input; // input stream from client
	private ServerSocket server; // server socket
	private Socket connection; // connection to client
	private int counter = 1; // counter of number of connections
	private Atm atmApplication;

	// set up and run server 
	public void runServer()
	{
		try // set up server to receive connections; process connections
		{
			server = new ServerSocket( port, maxConnections ); // create ServerSocket

			while(true){
				try{
					waitForConnection(); // wait for a connection
					getStreams(); // get input & output streams
					processConnection(); // process connection
				} // end try
				catch(EOFException eofException){
					System.out.println( "\nServer terminated connection" );
				} // end catch
				finally{
					closeConnection(); //  close connection
					++counter;
				} // end finally
			} // end while
		} // end try
		catch ( IOException ioException ){
			ioException.printStackTrace();
		} // end catch
	} // end method runServer

	
	private void waitForConnection() throws IOException{
	   
		System.out.println( "Waiting for connection\n" );
		
		connection = server.accept(); // allow server to accept connection            
		System.out.println( "Connection " + counter + " received from: " + connection.getInetAddress().getHostName() );
   
	} // end method waitForConnection

   // get streams to send and receive data
   private void getStreams() throws IOException
   {
      // set up output stream for objects
      output = new ObjectOutputStream( connection.getOutputStream() );
      output.flush(); // flush output buffer to send header information

      // set up input stream for objects
      input = new ObjectInputStream( connection.getInputStream() );

      System.out.println( "\nGot I/O streams\n" );
   } // end method getStreams

   // process connection with client
   private void processConnection() throws IOException
   {
      Object request;
      do // process messages sent from client
      { 
         try // read message and display it
         {
        	 request =  input.readObject(); // read new message
        	 processMessage(request); // process the request received from client 
         } // end try
         catch ( ClassNotFoundException classNotFoundException ) 
         {
        	 System.out.println( "\nUnknown object type received" );
         } // end catch

      } while(true);
   } // end method processConnection

   // close streams and socket
   private void closeConnection() {
	   System.out.println( "\nTerminating connection\n" );
	   try{
         output.close(); // close output stream
         input.close(); // close input stream
         connection.close(); // close socket
	   } // end try
	   catch ( IOException ioException ) {
         ioException.printStackTrace();
      } // end catch
   } // end method closeConnection

   
   // send message to client
   private void sendResponse(Object message)
   {
      try // send object to client
      {
         output.writeObject( message );
         output.flush(); // flush output to client
         System.out.println( "\nSERVER>>> " + message );
      } // end try
      catch ( IOException ioException ) 
      {
         System.out.println( "\nError writing object" );
      } // end catch
   } // end method sendData

   public void processMessage(Object request){
	  
	   Map<String, String> data = (Map<String, String>)request;
	   
	   System.out.println(data.entrySet());
	   
	   atmApplication = new Atm(data);
	   sendResponse(atmApplication.processRequest());   
   }

} 
