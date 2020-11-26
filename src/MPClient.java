import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.Instant;

//class for my messenger's client
public class MPClient{
	
	//main method handles connecting to server, checking server responses, and communicating between server and GUI
	public static void main(String[] args) throws Exception{
		
  		//create client socket to connect to server
  		Socket sock = new Socket();
  		
  		//main code block, closes socket when complete
  		try{	
  			//create GUI
			MPClientGUI gui = new MPClientGUI();
			
			//enter first GUI screen to check ip address for server
  			gui.ipScreen();
  			
  			//boolean ipsuccess to check whether connection was successful in below while loop
  			boolean ipSuccess = false;
  			//loop while connection is unsuccessful
  			while(!ipSuccess){
  				
  				//wait until user enters command in ipScreen
  				while(!gui.getIpStatus()){}
  				
  				//get ip address from GUI
  				String ip = gui.getIpField();
  			
  				//try connecting client socket to server
  				try{
  					sock.connect(new InetSocketAddress(ip, 1312), 500);
  					ipSuccess = true;
  				}
  				//if there is an error when connecting
  				catch(Exception e){
					//update GUI with error message
  					gui.updateIpMsg("Error: Invalid IP address");
  					//reset command status in ipScreen
  					gui.setIpStatus(false);
  					//recreate client socket after failed connect
  					sock = new Socket();
  				}
  				
  			}
  			//socket is connected to server now that the loop is passed
  			
  			//create ObjectInputStream srv to read server inputs (Strings, Messages, ArrayList, etc)
  			ObjectInputStream srv = new ObjectInputStream(sock.getInputStream());
  			//create PrintWriter cli to write client outputs (only Strings)
  			PrintWriter cli = new PrintWriter(sock.getOutputStream(), true);
	
  			//after connecting to server, enter next GUI screen to check username and password
  			gui.credsScreen();
  			
  			//boolean credsSuccess to check whether login credentials are valid in below while loop
  			boolean credsSuccess = false;
  			//loop while credentials are invalid
  			while(!credsSuccess){
				
				//wait until user enters command in credsScreen
  				while(!gui.getCredsStatus()){}
  				
  				//get username from GUI
  				String username = gui.getUsernameField();
  				//send username to server
  				cli.println(username);
  				//get password from GUI
  				String password = gui.getPasswordField();
  				//send password to server
  				cli.println(password);
  				
  				//get response from server
  				String response = (String) srv.readObject();
  				
  				//if server sends username back, this indicates a valid login
  				if(response.equals(username)){
  					credsSuccess = true;
  				}
  				//if not, login credentials are invalid
  				else{
  					//send error message to GUI
  					gui.updateCredsMsg("Error: Invalid Credentials");
  					//reset command status in credsScreen
  					gui.setCredsStatus(false);
  				}	
  			}
  			//user is logged in to the server now that the loop is passed
  		
  			//after logging in, enter main GUI screen to view and write messages
  			gui.mainScreen();
  			
  			//receive banner message from server
  			String banner = (String) srv.readObject();
  			//add banner message to server
  			gui.updateBanner(banner);
  			
  			//receive most recent page of messages from server
  			ArrayList<Message> messagePage = (ArrayList<Message>) srv.readObject();
  			//send these messages to the GUI
  			gui.setMessages(messagePage);
  			//update messageLabels in GUI
  			gui.updateMessageLabels();
  			//reset command status in mainScreen
  			gui.setMainCommand("");
  			
  			//endless loop at main screen
  			while(true){

				//wait until user enters command in mainScreen
  				while(gui.getMainCommand().equals("")){}
  			
  				//send command to server
  				cli.println(gui.getMainCommand());
  				//if command is message, also send the message
  				if(gui.getMainCommand().equals("message")){
  					//send message to server
  					cli.println(gui.getNewMsgField());
  				}
  				
  				//after server processes command, receive updated messagePage
  				messagePage = (ArrayList<Message>) srv.readObject();
  				//if messagePage is empty, command was invalid
  				if(messagePage.size() == 0){
  					//server sends new valid messagePage
  					messagePage = (ArrayList<Message>) srv.readObject();
  				}

				//send updated messagePage to GUI
  				gui.setMessages(messagePage);
  				//update messageLabels in GUI
  				gui.updateMessageLabels();
  				//reset command status in mainScreen
  				gui.setMainCommand("");
  			}
  		}
  		//catches exceptions in try block (mostly socket errors)
  		catch(Exception e){
			System.out.println("Socket error");
		}
		//close socket when complete
		finally{
			try{
				sock.close();
			}
			catch(IOException ioe){
				System.out.println("Socket close error");
			}
			System.out.println("Socket " + sock + " closed");
		}
	}	
}
