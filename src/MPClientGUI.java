import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//class for my messenger's client GUI
public class MPClientGUI{

	//Frame for GUI
	private Frame frame;
	
	//Panel for checking IP
	private Panel ipPanel;
	private volatile Label ipMsg;
	private volatile TextField ip;
	private volatile boolean ipStatus;
	
	//Panel for checking credentials
	private Panel credsPanel;
	private volatile Label credsMsg;
	private volatile TextField user;
	private volatile TextField pw;
	private volatile boolean credsStatus;

	//Panel for the main program functions, includes all panels listed below
	private Panel mainPanel;
	//Panel to list messages
	private Panel listPanel;
	private volatile ArrayList<Message> messages = new ArrayList<Message>();
	private ArrayList<Label> messageLabels = new ArrayList<Label>();
	//Panel for commands
	private Panel commandsPanel;
	//Panel for page commands
	private Panel pagePanel;
	private Button prevBtn;
	private Button refreshBtn;
	private Button nextBtn;
	//Panel for message commands
	private Panel msgPanel;
	private volatile TextField newMsg;
	private volatile String mainCommand;

	//GUI constructor
	public MPClientGUI(){
	
		//create frame
		frame = new Frame("MicroPost");
		//set frame size
		frame.setSize(800,600);
		//no, I don't want you to resize the frame lol
		frame.setResizable(false);
		//add WindowListener to listen for frame closing
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent wclose){
				//close program when frame closes
				System.exit(0);
			}
		});
		//make frame visible
		frame.setVisible(true);
	
	}
	
	//get ipStatus, whether user has entered command on ipScreen
	public boolean getIpStatus(){
		return ipStatus;
	}
	
	//set ipStatus
	public void setIpStatus(boolean status){
		ipStatus = status;
	}
	
	//get text from field ip
	public String getIpField(){
		return ip.getText();
	}
	
	//update message on ipScreen
	public void updateIpMsg(String imsg){
		ipMsg.setText(imsg);
		frame.setVisible(true);
	}
	
	//initialize screen for ipPanel
	public void ipScreen(){
		
		//create ipPanel
		ipPanel = new Panel();
		//set ipPanel layout
		ipPanel.setLayout(new GridLayout(3, 0));
		
		
		//create Label for message on ipPanel
		ipMsg = new Label("Welcome to MicroPost! Give the IPV4 address of the host server");
		ipMsg.setAlignment(Label.CENTER);
		ipPanel.add(ipMsg);
		
		//create TextField to enter ip
		ip = new TextField("ip.xxx.xxx.xxx");
		ip.addKeyListener(new IpKeyListener());
		ipPanel.add(ip);
		
		//create button to set ipStatus
		Button ipBtn = new Button("Connect");
		ipBtn.addActionListener(new IpActionListener());
		ipBtn.addKeyListener(new IpKeyListener());
		ipPanel.add(ipBtn);
		ipStatus = false;
		
		//add ipPanel to frame
		frame.add(ipPanel);
		frame.setVisible(true);
		
		//make ipPanel focusable
		ipPanel.requestFocusInWindow();
		
	}
	
	//class to listen for actions on ipScreen
	private class IpActionListener implements ActionListener{
		//action method	
      		public void actionPerformed(ActionEvent e) {
      			//set ipStatus
			setIpStatus(true);
        	}
   	}
	
	//class to listen for key events on ipScreen
	private class IpKeyListener implements KeyListener{
      		public void keyTyped(KeyEvent e) {           
      		}
		
      		public void keyPressed(KeyEvent e) {
      			//if enter key pressed
         		if(e.getKeyCode() == KeyEvent.VK_ENTER){
         			//set ipStatus
            			setIpStatus(true);
			}
      		}	

      		public void keyReleased(KeyEvent e) {            
      		}    
   	} 
	
	//get credsStatus, whether user has entered command on credsScreen
	public boolean getCredsStatus(){
		return credsStatus;
	}
	
	//set credsStatus
	public void setCredsStatus(boolean status){
		credsStatus = status;
	}
	
	//get text from field username
	public String getUsernameField(){
		return user.getText();
	}
	
	//get text from field password
	public String getPasswordField(){
		return pw.getText();
	}
	
	//update message on credsScreen
	public void updateCredsMsg(String cmsg){
		credsMsg.setText(cmsg);
		frame.setVisible(true);
	}
	
	//initialize credsScreen
	public void credsScreen(){
		
		//remove previous panel
		frame.remove(ipPanel);
		
		//create credsPanel
		credsPanel = new Panel();
		//set credsPanel layout
		credsPanel.setLayout(new GridLayout(4,0));
		
		//create Label for message on credsPanel
		credsMsg = new Label("Connected! Please enter username and password to login or create account");
		credsMsg.setAlignment(Label.CENTER);
		credsPanel.add(credsMsg);
		
		//create TextField to enter username
		user = new TextField("username");
		user.addKeyListener(new CredsKeyListener());
		credsPanel.add(user);
		
		//create TextField to enter password
		pw = new TextField("password");
		pw.addKeyListener(new CredsKeyListener());
		credsPanel.add(pw);
		
		//create Button to set credsStatus
		Button credsBtn = new Button("Login");
		credsBtn.addActionListener(new CredsActionListener());
		credsBtn.addKeyListener(new CredsKeyListener());
		credsPanel.add(credsBtn);
		credsStatus = false;
		
		//add credsPanel to frame
		frame.add(credsPanel);
		frame.setVisible(true);
		
		//make credsPanel focusable
		credsPanel.requestFocusInWindow();
		
	}
	
	//class to listen for action events on credsScreen
	private class CredsActionListener implements ActionListener{	
      		public void actionPerformed(ActionEvent e) {
			
			//check username length
			if((getUsernameField().length() < 1) || (getUsernameField().length() > 16)){
				updateCredsMsg("Error: username must be between 1 and 16 characters");
			}
			//check password length
			else if((getPasswordField().length() < 1) || (getPasswordField().length() > 16)){
				updateCredsMsg("Error: password must be between 1 and 16 characters");
			}
			//if both ok, set credsStatus
			else{
				setCredsStatus(true);
			}
      			
        	}
   	}
   	
   	//class to listen for key events on credsScreen
   	private class CredsKeyListener implements KeyListener{
      		public void keyTyped(KeyEvent e) {           
      		}

      		public void keyPressed(KeyEvent e) {
      			
      			//if enter key pressed
         		if(e.getKeyCode() == KeyEvent.VK_ENTER){
         			//check username length
            			if((getUsernameField().length() < 1) || (getUsernameField().length() > 16)){
					updateCredsMsg("Error: username must be between 1 and 16 characters");
				}
				//check password length
				else if((getPasswordField().length() < 1) || (getPasswordField().length() > 16)){
					updateCredsMsg("Error: password must be between 1 and 16 characters");
				}
				//if both ok, set credsStatus
				else{
					setCredsStatus(true);
				}
			}
			
      		}	

      		public void keyReleased(KeyEvent e) {            
      		}    
   	} 
	
	//get credsStatus, whether user has entered command on credsScreen
	public String getMainCommand(){
		return mainCommand;
	}
	
	//set credsStatus
	public void setMainCommand(String cmd){
		mainCommand = cmd;
	}
	
	//get text from field newMsg
	public String getNewMsgField(){
		return newMsg.getText();
	}
	
	//set Messages, when new messagePage is returned
	public void setMessages(ArrayList<Message> msgs){
		messages = msgs;
	}
	
	//update Labels for each Message in mainScreen
   	public void updateMessageLabels(){
   		for(int i = 0; i < messages.size(); i++){
   		   	messageLabels.get((i*4)+1).setText("--------------------------------");
   		   	messageLabels.get((i*4)+2).setText(messages.get(i).getUsername());
   		   	messageLabels.get((i*4)+3).setText(messages.get(i).getMsg());
   			messageLabels.get((i*4)+4).setText(messages.get(i).getTime());
   		}
   		
   	}
   	
   	//update Fonts for each Label (similar to above, but only used once to set initial font)
   	private void updateMessageLabelFonts(){
   		for(int i = 0; i < 8; i++){
   			messageLabels.get((i*4)+2).setFont(new Font("SANS_SERIF", Font.BOLD, 11));
   			messageLabels.get((i*4)+3).setFont(new Font("SANS_SERIF", Font.PLAIN, 11));
   			messageLabels.get((i*4)+4).setFont(new Font("SANS_SERIF", Font.ITALIC, 11));
   		}
   	}
   	
   	//update banner in mainScreen
	public void updateBanner(String banner){
		messageLabels.get(0).setText(banner);
	}
   	
   	//update main message (error message) in mainScreen
   	public void updateMainMsg(String mmsg){
   		messageLabels.get(33).setText(mmsg);
   		frame.setVisible(true);
   	}
	
	//initialize mainScreen
	public void mainScreen(){
		
		//remove previous panel
		frame.remove(credsPanel);
		
		
		//create mainPanel
		mainPanel = new Panel();
		//set mainPanel layout
		mainPanel.setLayout(new BorderLayout());
		
		
		//create listPanel
		listPanel = new Panel();
		//set listPanel layout 
		listPanel.setLayout(new GridLayout(34, 0));
		//add banner to list of messageLabels
		messageLabels.add(new Label());
		//create Label for each Message
		for(int i = 0; i < 32; i++){
			messageLabels.add(new Label());
		}
		//create Label for main message
		messageLabels.add(new Label("--------------------------------"));
		
		//adding messageLabels to listPanel
		for(int i = 0; i < messageLabels.size(); i++){
			listPanel.add(messageLabels.get(i));
		}
		
		//set messageLabel fonts
		updateMessageLabelFonts();
		
		//adding listPanel to mainPanel
		mainPanel.add(listPanel, BorderLayout.CENTER);
		
		
		//create commandsPanel
		commandsPanel = new Panel();
		//set commandsPanel layout
		commandsPanel.setLayout(new GridLayout(2,0));
		
		//create pagePanel
		pagePanel = new Panel();
		//set pagePanel layout
		pagePanel.setLayout(new FlowLayout());
		
		//create Button to move to previous page of Messages
		prevBtn = new Button("<");
		prevBtn.setActionCommand("previous");
		prevBtn.addActionListener(new MainActionListener());
		prevBtn.addKeyListener(new MainKeyListener());
		pagePanel.add(prevBtn);
		
		//create Button to refresh to most recent Messages
		refreshBtn = new Button("Refresh");
		refreshBtn.setActionCommand("refresh");
		refreshBtn.addActionListener(new MainActionListener());
		refreshBtn.addKeyListener(new MainKeyListener());
		pagePanel.add(refreshBtn);
		
		//create Button to move to next page of Messages
		nextBtn = new Button(">");
		nextBtn.setActionCommand("next");
		nextBtn.addActionListener(new MainActionListener());
		nextBtn.addKeyListener(new MainKeyListener());
		pagePanel.add(nextBtn);
		
		//add pagePanel to commands Panel
		commandsPanel.add(pagePanel);
		
		//create msgPanel
		msgPanel = new Panel();
		//set msgPanel layout
		msgPanel.setLayout(new FlowLayout());
		
		//create TextField to enter Message
		newMsg = new TextField("Write a message...");
		newMsg.addKeyListener(new MainKeyListener());
		msgPanel.add(newMsg);
		
		//create Button to send Message
		Button sendBtn = new Button("Send");
		sendBtn.setActionCommand("message");
		sendBtn.addActionListener(new MainActionListener());
		sendBtn.addKeyListener(new MainKeyListener());
		msgPanel.add(sendBtn);
		
		//add msgPanel to commandsPanel
		commandsPanel.add(msgPanel);
		
		//add commandsPanel to mainPanel
		mainPanel.add(commandsPanel, BorderLayout.SOUTH);

		
		//add mainPanel to frame
		frame.add(mainPanel);	
		frame.setVisible(true);
		
		//make mainPanel focusable
		mainPanel.requestFocusInWindow();
	}
	
	//class to listen for action events on mainScreen
	private class MainActionListener implements ActionListener{	
      		public void actionPerformed(ActionEvent e) {
      			
      			//if Message is too long
      			if((e.getActionCommand().equals("message")) && (getNewMsgField().length() > 100)){
      				//display error message
      				updateMainMsg("Error: Message cannot be greater than 100 characters (+" + (getNewMsgField().length() - 100) + ")");
      			}
      			//if not
      			else{
      				//set command to command given
				setMainCommand(e.getActionCommand());
				//clear error message if necessary
				updateMainMsg("--------------------------------");
			}
        	}
   	}
	
	//class to listen for key events on mainScreen
	private class MainKeyListener implements KeyListener{
      		public void keyTyped(KeyEvent e) {           
      		}

      		public void keyPressed(KeyEvent e) {
      			
      			//if enter key pressed
         		if(e.getKeyCode() == KeyEvent.VK_ENTER){
         			//if source of key press is prevBtn
         			if(e.getSource().equals(prevBtn)){
         				//set command to previous
         				setMainCommand("previous");
         				updateMainMsg("--------------------------------");
         			}
         			//if source of key press is refreshBtn
         			else if(e.getSource().equals(refreshBtn)){
         				//set command to refresh
         				setMainCommand("refresh");
         				updateMainMsg("--------------------------------");
         			}
         			//if source of key press is nextBtn
         			else if(e.getSource().equals(nextBtn)){
         				//set command to refresh
         				setMainCommand("next");
         				updateMainMsg("--------------------------------");
         			}
         			//otherwise check newMsg
         			else{
         				//if Message is too long
         				if(getNewMsgField().length() > 100){
         					//display error message
      						updateMainMsg("Error: Message cannot be greater than 100 characters (+" + (getNewMsgField().length() - 100) + ")");
      					}
      					//if not
      					else{
      						//set command to message
						setMainCommand("message");
						updateMainMsg("--------------------------------");
					}
         			}

			}
			
      		}	

      		public void keyReleased(KeyEvent e) {            
      		}    
   	} 
	
}
