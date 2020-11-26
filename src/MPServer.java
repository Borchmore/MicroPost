import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.Instant;

//class for my messenger's server
public class MPServer{

	//main method sets up the server and then enters a loop to accept client connections
	public static void main(String[] args) throws Exception{
		
		//creating HashMap loginMap to store login credentials for this server (key: username, value: user)
		HashMap<String, User> loginMap = new HashMap<String, User>();
		//creating MessageList mlist to store Messages sent to server
		ArrayList<Message> messageList = new ArrayList<Message>();
	
		//start server by creating ServerSocket srvsock
		try(ServerSocket srvsock = new ServerSocket(1312)){
		
			//get public IPV4 address of server
			URL ipurl = new URL("https://ipecho.net/plain");
			BufferedReader ipread = new BufferedReader(new InputStreamReader(ipurl.openStream()));
			System.out.println("Server up at " + ipread.readLine());
			
			//creating a User account for the server admin
			Scanner sc = new Scanner(System.in);
			System.out.println("Please enter server admin username");
			String adusername = sc.nextLine();
			System.out.println("Please enter server admin password");
			String adpassword = sc.nextLine();
			User aduser = new User(adusername, adpassword);
			//adding User account to loginMap
			loginMap.put(adusername, aduser);
			
			//creating server admin's first Message to the server
			//this is done to initialize mlist with a Message, and to set the server's "banner"
			System.out.println("Please enter your server's initial message");
			System.out.println("Note: this message will also be displayed as your server banner!");
			String admsg = sc.nextLine();
			Message initmsg = new Message(adusername, admsg, Instant.now().toString());
			//adding admin Message to mlist
			messageList.add(0, initmsg);
			//remember to add message to banner!!!
			
			while(true){
				//create new runnable UserSession when client connects to srvsock
				UserSession session = new UserSession(srvsock.accept(), loginMap, messageList);
				//create thread from UserSession session
				Thread thread = new Thread(session);
				//execute new thread
				thread.start();
			}
		}
		//if srvsock cannot be created
		catch(Exception e){
			System.out.println("Error, port 1312 may already be in use");
		}
	}
	
	//helper method to check client's login credentials, and add them to loginMap if client is a new User
	private static boolean login(String uinput, String pinput, HashMap<String, User> loginMap){
	
		//check if loginMap contains key for username input
		if(loginMap.containsKey(uinput)){
			//if so, get the User value and check password input
			if(loginMap.get(uinput).checkpw(pinput)){
				return true;
			}
			else{
				return false;
			}
		}
		//if loginMap does not contain key
		else{
			//create new User from username and password input
			User newUser = new User(uinput, pinput);
			loginMap.put(uinput, newUser);
			return true;
		}
	
	}
	
	//helper method to update page index when new command is given
	private static int updateIndex(String command, int index){
		
		//command 'n' checks next page, add 8 to index
		if(command.equals("next")){
			index = index + 8;
		}
		//command 'p' checks previous page, subtract 8 from index
		else if(command.equals("previous")){
			index = index - 8;
		}
		//command 'r' refreshes to most recent messages
		else if(command.equals("refresh")){
			index = 0;
		}
		//command 'm' refreshes current page (after sending a Message)
		else if(command.equals("message")){
			//really not needed
  			index = index;
		}
		
		return index;
		
	}
	
	//helper method to return page from list of messages
	private static ArrayList<Message> updateMessagePage(ArrayList<Message> messageList, int index){
		
		//create ArrayList for new page of Messages
		ArrayList<Message> newMessagePage = new ArrayList<Message>();
		
		//if index is valid
		if((index >= 0) && (index < messageList.size())){
			//loop through 8 times (8 messages per page)
			for(int i = index; i < index+8; i++){
				
				//if Message exists
				if(i < messageList.size()){
					//add Message
					newMessagePage.add(messageList.get(i));
				}
				//if not
				else{
					//create dummy Message
					Message dummy = new Message("","","");
					//add dummy Message
					newMessagePage.add(dummy);				
				}
			}
		}
		
		return newMessagePage;	
	}
	
	//class for thread created when client connects to server
	private static class UserSession implements Runnable{
		
		//client Socket, passed from main when client connects and thread is created
		Socket sock;
		//HashMap loginMap for server, passed from main
		HashMap<String, User> loginMap;
		//MessageList mlist for server's messages, passed from main
		ArrayList<Message> messageList;
		
		//constructor for UserSession thread
		UserSession(Socket clientSock, HashMap<String, User> mainloginMap, ArrayList<Message> mainmessagelist){
			sock = clientSock;
			loginMap = mainloginMap;
			messageList = mainmessagelist;
		}
		
		//run method handles inputs from client and maintains server loginMap and mlist, runs when UserSession thread is executed
		public void run(){
			//main code block, closes socket when complete
			try{
			
				System.out.println("Socket " + sock + " connected");
				
				//create Scanner cli to read client inputs (only Strings)
				Scanner cli = new Scanner(sock.getInputStream());
				//create ObjectOutputStream to send server outputs (Strings, Messages, ArrayList, etc)
				ObjectOutputStream srv = new ObjectOutputStream(sock.getOutputStream());
				
				//creates String username, which is set inside loop below
				String username = "";
				//create boolean success, used to check if login is successful in loop below
				boolean success = false;
				//loop while login is not successful
				while(success == false){
					
					//get username from client
					username = cli.nextLine();
					//get password from client
					String password = cli.nextLine();
					//check if username and password are valid login credentials
					success = login(username, password, loginMap);
					
					//if so, return username to client, indicating successful login
					if(success == true){
						srv.writeObject(username);
						System.out.println("User " + username + " logged in");
					}
					//if not, return username + error, indicating unsuccessful login
					else{
						srv.writeObject(username + "error");
					}
					
				}
				
				//this code block sends the server banner and most recent page of Messages to client to initialize main
				//create index to track the current page in messageList
				int index = 0;
				//get banner, which is the text from the first message in this server's messageList
				String banner = messageList.get(messageList.size() - 1).getMsg();
				srv.writeObject(banner);
				//get most recent message page (page index = 0)
				ArrayList<Message> messagePage = updateMessagePage(messageList, index);
				srv.writeObject(messagePage);
				
				//loop to wait for client commands
				while(cli.hasNextLine()){
					
					//get client command
					String command = cli.nextLine();
					//update index based on command
					index = updateIndex(command, index);
					//if command is "message", also get new message from client
					if(command.equals("message")){
						//get message text from client
						String msgString = cli.nextLine();
						System.out.println("Message received from " + username + ": " + msgString);
						//create new message from username, message text, and current time
						Message newmsg = new Message(username, msgString, Instant.now().toString());
						//add new message to messageList
  						messageList.add(0, newmsg);
					}
					
					//get messagePage based on updated index (and message if applicable)
					messagePage = updateMessagePage(messageList, index);
					//send messagePage to client
					srv.writeObject(messagePage);
					//check if messagePage is valid (index in range of messageList.size()) and send new valid messagePage
					if(messagePage.size() == 0){
						//if index is below 0, reset to 0
						if(index < 0){
							index = 0;
						}
						//otherwise, index is above messageList.size(), so reset to previous valid index
						else{
							index = index - 8;
						}
						//update messagePage with valid index
						messagePage = updateMessagePage(messageList, index);
						//send valid messagePage to client
						srv.writeObject(messagePage);
					}
	
				}
					
			}
			//catch Exceptions (socket errors)
			catch(Exception e){
				System.out.println("Socket " + sock + " error");
			}
			//close client socket after client disconnects
			finally{
				try{
					sock.close();
					System.out.println("Socket " + sock + " closed");
				}
				catch(IOException ioe){
					System.out.println("Socket close error");
				}
			}
		}
	
	}
}
