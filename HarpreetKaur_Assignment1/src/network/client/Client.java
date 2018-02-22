package network.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.Soundbank;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String[] atmOptions = {" ", "Balance Enquiry", "Withdrawal", "Deposit"};
	
	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	private String message = ""; // message from server
	private String serverAddress; // host server for this application
	private int serverPort;
	private Socket clientSocket; // socket to communicate with server
	
	private JLabel lblCustNum;
	private JLabel lblPassword;
	private JLabel lblLoginPage;
	private JLabel lblAmount;
	private JLabel lblOperation;
	private JButton btnSubmit;
	
	private JTextField txtCustNum;	
	private JTextField txtPassword;	
	private JTextField txtAmount;
	private JComboBox cmbOperationList;	
	private JTextArea messageArea;

   // initialize chatServer and set up GUI
   public Client( String host, int port)
   {
      super( "Client" );

      serverAddress = host; // set server to which this client connects
      serverPort = port;
      //
      cmbOperationList = new JComboBox(atmOptions);
      cmbOperationList.setSelectedIndex(0);      
		 
      lblCustNum = new JLabel("Customer Id:");
      lblPassword = new JLabel("Password:");
      lblOperation = new JLabel("Operation:");
      lblAmount = new JLabel("Amount:");
      lblLoginPage = new JLabel("Login");
      
      txtCustNum = new JTextField(20);
      txtPassword = new JTextField(20);      
      txtAmount = new JTextField(20);
      messageArea = new JTextArea(5,20);
      
      btnSubmit = new JButton("Submit");     
      btnSubmit.addActionListener(
		       	new ActionListener(){
		           
		       		// Handle click event		           
		       		public void actionPerformed( ActionEvent event ){
		           	
		       			Map<String, String> data = new HashMap<String, String>();
		       			data.put("customer", txtCustNum.getText());
		       			data.put("customerPin", txtPassword.getText());
		       			data.put("amount", txtAmount.getText());		            	
		       			data.put("operation", cmbOperationList.getSelectedIndex() + "");
		           
		       			sendRequest(data);
		           } 
		        } 
		     ); 
      
      
      JPanel contentPanel = new JPanel();
      contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
      
      JPanel custPanel = new JPanel();
      custPanel.setLayout(new BoxLayout(custPanel, BoxLayout.LINE_AXIS));
      custPanel.add(lblCustNum);
      custPanel.add(txtCustNum);
      
      contentPanel.add(custPanel);
      
      JPanel pwdPanel = new JPanel();
      pwdPanel.setLayout(new BoxLayout(pwdPanel, BoxLayout.LINE_AXIS));
      pwdPanel.add(lblPassword);
      pwdPanel.add(txtPassword);
      
      contentPanel.add(pwdPanel);
      
      JPanel amtPanel = new JPanel();
      amtPanel.setLayout(new BoxLayout(amtPanel, BoxLayout.LINE_AXIS));
      amtPanel.add(lblAmount);
      amtPanel.add(txtAmount);
      
      contentPanel.add(amtPanel);
      
      JPanel operPanel = new JPanel();
      operPanel.setLayout(new BoxLayout(operPanel, BoxLayout.LINE_AXIS));
      operPanel.add(lblOperation);
      operPanel.add(cmbOperationList);
      
      contentPanel.add(operPanel);
      contentPanel.add(btnSubmit);
      
      
      Container containerPane = getContentPane();
      
      containerPane.setLayout(new BorderLayout());
      
      containerPane.add(lblLoginPage, BorderLayout.NORTH);
      containerPane.add(contentPanel, BorderLayout.CENTER); 
      containerPane.add( messageArea, BorderLayout.SOUTH);
      
      pack();
      
      
	
      setSize( 300, 250); // set size of frame
      setVisible( true ); // show window
   } 

   // connect to server and process messages from server
   public void runClient() 
   {
      try // connect to server, get streams, process connection
      {
         connectToServer(); // create a Socket to make connection
         getStreams(); // get the input and output streams
         processConnection(); // process connection
      } 
      catch ( EOFException eofException ) 
      {
    	  System.out.println("\nClient terminated connection" );
        
      } 
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      }
      finally 
      {
         closeConnection(); // close connection
      }
   } 

   // connect to server
   private void connectToServer() throws IOException
   {      
     System.out.println("Attempting connection\n");
     
     // create Socket to make connection to server
     clientSocket = new Socket( InetAddress.getByName( serverAddress ), serverPort );

      // display connection information
      System.out.println( "Connected to: " + clientSocket.getInetAddress().getHostName() + "on port: " + clientSocket.getPort() );
   } // end method connectToServer

   
   // get streams to send and receive data
   private void getStreams() throws IOException
   {
      output = new ObjectOutputStream( clientSocket.getOutputStream() );      
      output.flush(); // flush output buffer to send header information

      input = new ObjectInputStream( clientSocket.getInputStream() );

      System.out.println( "\nGot I/O streams\n" );
   } 

   // process connection with server
   private void processConnection() throws IOException
   {
      do // process messages sent from server
      { 
         try // read message and display it
         {
            message = (String) input.readObject(); // read new message
           
            displayMessage(message); //display message received from server on screen
            
         } // end try
         catch ( ClassNotFoundException classNotFoundException ) 
         {
        	 System.out.println( "\nUnknown object type received" );
         } // end catch

      } while ( !message.equals( "SERVER>>> TERMINATE" ) );
   } // end method processConnection

   
   private void displayMessage( final String messageToDisplay )
   {
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run() // updates displayArea
            {
               messageArea.setText(messageToDisplay);
            }
         }  
      ); 
   }    
   
   
   // close streams and socket
   private void closeConnection() 
   {
	  System.out.println( "\nClosing connection" );
      try 
      {
         output.close(); // close output stream
         input.close(); // close input stream
         clientSocket.close(); // close socket
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method closeConnection

   
   // send message to server
   private void sendRequest(Object message)
   {
      try // send object to server
      {
         output.writeObject( message );
         output.flush(); // flush data to output
         System.out.println( "\nCLIENT>>> " + message );
      } // end try
      catch ( IOException ioException )
      {
        System.out.println( "\nError writing object" );
      } // end catch
   } // end method sendData


} // end class Client


