package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import frames.LoginScreenWindow;
import frames.SelectionWindow;
import logic.Account;
import logic.Party;
import logic.PartySong;
import logic.User;

public class TrojamClient extends Thread{
	private Account account;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private SelectionWindow sw;
	private LoginScreenWindow lsw;

	public TrojamClient(String IPAddress, int port) {
		System.out.println("creating a new client!!!");
		this.account = null;
		this.sw = null;
		try {
			s = new Socket(IPAddress, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			//oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			//before we enter the run method, we want to send our account
			this.start();
			
		} catch (NumberFormatException | IOException e) {
			System.out.println("yo");
		}
	}
	
	public void receiveLoginScreen(LoginScreenWindow lsw){
		this.lsw = lsw;
	}
	
	public void setSelectionWindow(SelectionWindow sw) {
		this.sw = sw;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account a) {
		System.out.println("setting account");
		this.account = a;
		
		try {
			oos.writeObject(a);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sent stuff");
		//this.start();
	}
	
	@Override
	public void run() {
		System.out.println("run was invoked");
		while (true){
			try {
				Object obj = ois.readObject();
				System.out.println("got message");
				//handle different types of messages
				if (obj instanceof StringMessage) {
					StringMessage message = (StringMessage) obj;
					parseStringMessage(message);
				} else if (obj instanceof SongVoteMessage) {
					System.out.println("client has received song upvoted message!");
				}else if (obj instanceof PartyMessage) {
					PartyMessage pm = (PartyMessage) obj;
					//if (pm.getName().equals("newParty")) {
					System.out.println("new party sent to client");
					sw.addNewParty(pm.getParty());
						//}
				} else if(obj instanceof AuthenticatedLoginMessage){
					System.out.println("client received loginmessage");
					AuthenticatedLoginMessage alm = (AuthenticatedLoginMessage) obj;
					lsw.attemptLogIn(alm.isAuthenticated());
				} else if(obj instanceof AccountCreatedMessage){
					System.out.println("client received account created message");
					AccountCreatedMessage acm = (AccountCreatedMessage) obj;
					lsw.createAccount(acm.accountCreated(), acm.getUser());
				}
			} catch (ClassNotFoundException | IOException e) {}
		}
	}
	
	public void attemptToLogin(String username, String password){
		try {
			oos.writeObject(new LoginMessage(username, password));
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendNewPartyMessage(NewPartyMessage npm) {
		try {
			System.out.println("trying to send a new party");
			oos.writeObject(npm);
			oos.flush();
			System.out.println("sent a new party");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//handle string messages sent to the client
	private void parseStringMessage(StringMessage message) {
		String name = message.getName();
		String content = message.getContent();
		if (name.equals("teamLeft")) {
			//perform action for when an account leaves the party 
		} else if (name.equals("songAdded")) {
			//perform action for when a song is added to the party
		} else if (name.equals("songUpvoted")) {
			//perform action for when a song is upvoted
		} else if (name.equals("songDownvoted")) {
			//perform action for when a song is downvoted
		}
		
	}

	public void createAccount(User newUser, String password) {
		System.out.println("sending create account message");
		
		try {
			oos.writeObject(new CreateAccountMessage(newUser, password));
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void sendVotesChange(Party party, PartySong partySong, String voteType) {
		System.out.println(partySong.getName() + " was voted");
		try {
			oos.writeObject(new SongVoteMessage(voteType, party, partySong));
			oos.flush();
			System.out.println("send party song info to serverthread");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
