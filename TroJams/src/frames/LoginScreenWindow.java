package frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import listeners.TextFieldFocusListener;
import logic.Guest;
import logic.User;
import networking.AuthenticatedLoginMessage;
import networking.TrojamClient;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class LoginScreenWindow extends JFrame {

	private JPanel mainPanel, cards;
	private CardLayout cl;
	private JButton loginButton;
	private JButton guestButton;
	private JButton createAccount;
	private JTextField username;
	private JTextField password;
	private JLabel alertLabel;
	private ImageIcon backgroundImage;
	private TrojamClient client;
	private String usernameString;
	JLabel logoLabel;
	
	//users map
	//could have use <String, String> instead of User object, but chose not to
	private HashMap<String, User> existingUsers;
	//the file that contains user account info
	private File file;

	public LoginScreenWindow(TrojamClient client) {
		super("TroJams");
		this.client = client;
		file = new File("users.txt");
		existingUsers = new HashMap<>();
		client.receiveLoginScreen(this);
		//reads in stored users from file and populates existingUsers
		//readFromFile();
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	private void initializeComponents(){

		this.setContentPane(new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image image = new ImageIcon("images/backgroundImage.png").getImage();
				backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
				g.drawImage(image, 0, 0, 1280, 800, this);
			}
		});

		loginButton = new JButton();
		ImageIcon image1 = new ImageIcon("images/button_log-in.png");
		loginButton.setIcon(image1);
		loginButton.setOpaque(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		
		guestButton = new JButton();
		ImageIcon image2 = new ImageIcon("images/button_join-as-guest.png");
		guestButton.setIcon(image2);
		guestButton.setOpaque(false);
		guestButton.setContentAreaFilled(false);
		guestButton.setBorderPainted(false);
		
		createAccount = new JButton();
		ImageIcon image3 = new ImageIcon("images/button_create-account.png");
		createAccount.setIcon(image3);
		createAccount.setOpaque(false);
		createAccount.setContentAreaFilled(false);
		createAccount.setBorderPainted(false);
		
		username = new JTextField("username");
		password = new JTextField("password");
		alertLabel = new JLabel();
		Image image = new ImageIcon("images/TroJamsLogo4.png").getImage();
		ImageIcon logoImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		logoLabel = new JLabel(logoImage);
		
		cards = new JPanel(new CardLayout());

	}
	
	private void createGUI(){
		
		setSize(AppearanceConstants.GUI_WIDTH, AppearanceConstants.GUI_HEIGHT);
		setLocation(100,100);
		
		mainPanel = new JPanel();
		JPanel textFieldOnePanel = new JPanel();
		JPanel textFieldTwoPanel = new JPanel();
		JLabel welcome = new JLabel("", JLabel.CENTER);
		JLabel TroJamsLabel = new JLabel("TroJams!", JLabel.CENTER);
		JPanel alertPanel = new JPanel();
		JPanel textFieldsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel welcomePanel = new JPanel(new GridLayout(2,1));
		JPanel logoPanel = new JPanel();
		
		//set mass component appearances

		AppearanceSettings.setForeground(AppearanceConstants.trojamPurple, createAccount, loginButton, guestButton, password, username);
		AppearanceSettings.setSize(400, 60, password, username);
		
		//AppearanceSettings.setSize(200, 100, loginButton, guestButton, createAccount);
		AppearanceSettings.setNotOpaque(loginButton, createAccount, guestButton);
		
		AppearanceSettings.unSetBorderOnButtons(loginButton, createAccount, guestButton);
		
		AppearanceSettings.setTextAlignment(welcome, alertLabel, TroJamsLabel);
		AppearanceSettings.setForeground(Color.white, welcome, TroJamsLabel);
		
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, password, alertLabel, username, loginButton, createAccount, guestButton);
		
		AppearanceSettings.setNotOpaque(mainPanel, welcome, alertLabel, TroJamsLabel, alertPanel, textFieldsPanel,
				buttonsPanel, welcomePanel, textFieldOnePanel, textFieldTwoPanel, cards);


		//other appearance settings
		welcome.setFont(AppearanceConstants.fontLarge);
		TroJamsLabel.setFont(AppearanceConstants.fontHuge);

		loginButton.setEnabled(false);
		guestButton.setEnabled(true);
		createAccount.setEnabled(true);
		
		//add components to containers
		welcomePanel.add(welcome);
		welcomePanel.add(TroJamsLabel);
		
		alertPanel.add(alertLabel);
		textFieldOnePanel.add(username);
		textFieldTwoPanel.add(password);
		
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		//adds components to the panel with glue in between each
		AppearanceSettings.addGlue(buttonsPanel, BoxLayout.LINE_AXIS, true, loginButton, guestButton, createAccount);
		
		AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false, welcomePanel);
		//don't want glue in between the following two panels, so they are not passed in to addGlue
		mainPanel.add(alertPanel);
		mainPanel.add(textFieldOnePanel);
		AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false, textFieldTwoPanel);
		mainPanel.add(buttonsPanel);
		
		logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.LINE_AXIS));
		logoPanel.add(Box.createHorizontalGlue());
		logoPanel.add(logoLabel);
		logoPanel.add(Box.createHorizontalGlue());
		logoPanel.setOpaque(false);
		
		mainPanel.add(logoPanel);
		mainPanel.add(Box.createVerticalGlue());
		cards.add(mainPanel, "main window");
		//CreateAccountWindow caw = new CreateAccountWindow(this, client);
		//cards.add(caw, "create account window");
		add(cards, BorderLayout.CENTER);

	}

	/*public void dispose() {
		this.dispose();
	}*/
	
	//returns whether the buttons should be enabled
	private boolean canPressButtons(){
		return (!username.getText().isEmpty() && !username.getText().equals("username") && 
				!password.getText().equals("password") && !password.getText().isEmpty());
	}
	
	public void enableCreateAccountButton(){
		createAccount.setEnabled(true);
	}
	//reads in users map from the file
//	private void readFromFile(){
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/TroJamUsers?user=root&password=adam0601&useSSL=false");			
//			st = conn.createStatement();
//			rs = st.executeQuery("SELECT * FROM Users");
//			while(rs.next()){
//				String username = rs.getString("username");
//				String password = rs.getString("password");
//				System.out.println(username + " " + password);
//				User tempUser = new User(username, password);
//				existingUsers.put(username, tempUser);
//			}
//			
//		} catch (SQLException sqle) {
//			System.out.println("sqle: " + sqle.getMessage());
//		} catch (ClassNotFoundException cnfe) {
//			System.out.println("cnfe: " + cnfe.getMessage());
//		} finally{
//			try{
//				if(rs != null) {
//					rs.close();
//				}
//				if (st!=null){
//					st.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			} catch( SQLException sqle) {
//				
//			}
//		}
//
//	}

	private void addListeners(){
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//focus listeners
		username.addFocusListener(new TextFieldFocusListener("username", username));
		password.addFocusListener(new TextFieldFocusListener("password", password));
		//document listeners
		username.getDocument().addDocumentListener(new MyDocumentListener());
		password.getDocument().addDocumentListener(new MyDocumentListener());
		
		//action listeners
		//loginButton.addActionListener(new loginEvent());
		loginButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.setEnabled(false);
				usernameString = username.getText();
				client.attemptToLogin(username.getText(), password.getText());
			}
			
		});
				
		//SEND MESSAGE TO SERVER, WAITS TO GET ARRAY OF PARTIES
		
		
//		new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String usernameString = username.getText();
//				String passwordString = password.getText();
//				
//				//if the username does not exist
//				if (!existingUsers.containsKey(usernameString)){
//					alertLabel.setForeground(Color.white);
//					alertLabel.setText("Wait a second! This username does not exist.");
//				}
//				//else if the username exists
//				else{
//					User user = existingUsers.get(usernameString);
//					//if the user gave the wrong password
//					if (!user.verifyPassword(passwordString)) {
//						alertLabel.setText("The password you provided does not match our records");
//					}
//					//login successful - GO TO MAIN TROJAMS WINDOW
//					else{
//						new SelectionWindow(user, null).setVisible(true);
//						dispose();
//					}
//				}
//			}
//			
//		});
		
		createAccount.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {	
				
				//SEND MESSAGE TO SERVER, WAITS TO GET ARRAY OF PARTIES
				createAccount.setEnabled(false);
				String usernameString = username.getText();
				String passwordString = password.getText();
				//if this username has already been chosen
				if (existingUsers.containsKey(usernameString)){
					alertLabel.setText("This username has already been chosen by another user");
				}
				//username has not been chosen, send newly created user with username and password to Create Account Window to then 
				//fill in the rest of the info about the user.
				else{
					User newUser = new User(usernameString);
					CreateAccountWindow caw = new CreateAccountWindow(newUser, passwordString, LoginScreenWindow.this, client); //Pass in user and this GUI so that when the user is created, the 
						//create account window can call insertUserIntoDB
					cl = (CardLayout) cards.getLayout();
					cards.add(caw, "create account window");
					cl.show(cards, "create account window");
					//dispose();
				}
				
			}
			
		});
		
		guestButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {			
				//SEND MESSAGE TO SERVER, WAITS TO GET ARRAY OF PARTIES to pass into new selection window
				Guest g = new Guest();
				client.setAccount(g);
				//have selectionWindow take in an instance of account
				new SelectionWindow(new Guest(), null, client).setVisible(true); //Pass in user and this GUI so that when the user is created, the 
					//create account window can call insertUserIntoDB 
				dispose();
			}
		
		});
		
	}

//	void insertUserIntoDB(User user){
//		System.out.println("Insert");
//		Connection conn = null;
//		PreparedStatement ps = null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/TroJamUsers?user=root&password=adam0601&useSSL=false");	
//			String query = "INSERT INTO Users (username, password, firstName, lastName, imageFilePath) VALUES ('" + user.getUsername() + "','" + user.getPassword() + "','" + user.getFirstName() + "','" + user.getLastName() + "','" + user.getImageFilePath() + "')";
//			ps = conn.prepareStatement(query);
//			ps.execute();
//		} catch (SQLException sqle) {
//			System.out.println("sqle: " + sqle.getMessage());
//		} catch (ClassNotFoundException cnfe) {
//			System.out.println("cnfe: " + cnfe.getMessage());
//		} finally{
//			try{
//				if (ps!=null){
//					ps.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			} catch( SQLException sqle) {
//				
//			}
//		}
//	}
	
	//sets the buttons enabled or disabled
	private class MyDocumentListener implements DocumentListener{
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			//createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			//createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			//createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
	}

	public void attemptLogIn(AuthenticatedLoginMessage alm) {
		if(alm.isAuthenticated()){
			System.out.println("authenticated user");
			User newUser = new User(usernameString, alm.getfirstName(), alm.getLastName(), alm.getFilepath());
			client.setAccount(newUser);
			SelectionWindow sw = new SelectionWindow(newUser, null, client);
			client.setSelectionWindow(sw);
			sw.setVisible(true);
			dispose();
		}else{
			System.out.println("non-authenticated user");
			alertLabel.setForeground(Color.white);
			alertLabel.setText("Wait a second! This username/password combo does not exist.");
			loginButton.setEnabled(true);
		}
	}


	public void createAccount(boolean accountCreated, User newUser) {
		//TODO do the createAccountStuff
		System.out.println("made it back: " + accountCreated);
		if(accountCreated){
			SelectionWindow sw = new SelectionWindow(newUser, null, client);
			client.setSelectionWindow(sw);
			sw.setVisible(true);
			dispose();
		} else {
			//TODO some sort of warning here
			System.out.println("account was not created");
		}
	}
	
	public void showLoginWindow() {
		cl = (CardLayout) cards.getLayout();
		cl.show(cards, "main window");
	}
}