import java.util.ArrayList;
import java.io.Serializable;

//class for Message object
public class Message implements Serializable{
	
	//set serialVersionUID since Message is serializable
	static final long serialVersionUID = 666L;
	//create username, msg, and time Strings
	private String username, msg, time;

	//Message constructor
	public Message(String uinput, String minput, String tinput){
		if(!uinput.equals("")){
			uinput = uinput + ":";
		}
		username = uinput;
		msg = minput;
		time = tinput;
	}

	//get username of Message
	public String getUsername(){
		return username;
	}

	//get text of Message
	public String getMsg(){
		return msg;
	}
	
	//get time of Message
	public String getTime(){
		return time;
	}

}	
