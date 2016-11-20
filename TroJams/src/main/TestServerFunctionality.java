package main;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import frames.SelectionWindow;
import logic.Party;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;
import networking.TrojamClient;

public class TestServerFunctionality {
	public static void main (String [] args) {
		User u = new User("testUser", "testPassword");
		User u2 = new User("Hunter", "mwahahaha");
		User u3 = new User("Clairisse", "fightON");
		User u4 = new User("Adam", "ooooo");
		
		Image image = new ImageIcon("images/party-purple.jpg").getImage();
		ImageIcon tempImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		PrivateParty p1 = new PrivateParty("party1", "password1", u2, tempImage);
		PrivateParty p2 = new PrivateParty("party2", "password2", u3, tempImage);
		PublicParty p3 = new PublicParty("party3", u4, tempImage);
		ArrayList <Party> parties = new ArrayList <Party>();
		parties.add(p1);
		parties.add(p2);
		parties.add(p3);
		TrojamClient tj = new TrojamClient("localhost", 1111);
		tj.setAccount(u);
		SelectionWindow sw = new SelectionWindow(u, parties, tj);
		tj.setSelectionWindow(sw);
		sw.setVisible(true);
		
		
		TrojamClient tj2 = new TrojamClient("localhost", 1111);
		tj2.setAccount(u2);
		SelectionWindow sw2 = new SelectionWindow(u, parties, tj2);
		tj2.setSelectionWindow(sw2);
		sw2.setVisible(true);
	}
	
}
