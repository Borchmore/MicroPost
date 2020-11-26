//class for User object
public class User{

	//create username and password Strings
	private String username, password;

	//User constructor
	public User(String uinput, String pinput){
		username = uinput;
		password = pinput;
	}

	//method to check if password input matches User password
	public boolean checkpw(String pinput){
		if(!pinput.equals(password)){
			return false;
		}
		return true;
	}	

}
