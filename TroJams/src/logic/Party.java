package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public abstract class Party {
	
	private String partyName;
	private User host;
	private ArrayList <PartySong> songList = new ArrayList<PartySong>();
	private HashMap <PartySong, Integer> songSet = new HashMap<PartySong, Integer>();
	private HashSet <Account> partyMembers;
	
	//abstract class for a party
	public Party (String partyName, User host) {
		this.partyName = partyName;
		this.host = host;
		partyMembers = new HashSet<Account>();
	}
	
	public ArrayList<PartySong> getSongs() {
		return songList;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public void leaveParty(Account account) {
		partyMembers.remove(account);
	}
	
	//add a user to the party
	public void addAccount(Account account) {
		//not sure if this will be done through a database
	}
	
	public void addSong(PartySong song) {
		if (songSet.containsKey(song)) {
			return;
		}
		songList.add(song);
		songSet.put(song, songList.size()-1);
	}
	
	public void upvoteSong(PartySong song) {
		int loc = songSet.get(song);
		song.upvote();
		//look at the indices before in the array and keep swapping while the
		//number of votes of loc - 1 is less than the number of votes of song
		while (loc > 0 && songList.get(loc - 1).getVotes() < songList.get(loc).getVotes()) {
			PartySong tempSong = songList.get(loc-1);
			songSet.put(tempSong, loc);
			songSet.put(song, loc - 1);
			songList.set(loc, tempSong);
			songList.set(loc - 1, song);
			loc --;
		}
	}
	
	public void downvoteSong(PartySong song) {
		int loc = songSet.get(song);
		song.downvote();
		//look at the indices after in the array and keep swapping while the
		//number of votes of loc + 1 is greater than the number of votes of song
		while (loc < songList.size() && songList.get(loc + 1).getVotes() > songList.get(loc).getVotes()) {
			PartySong tempSong = songList.get(loc+1);
			songSet.put(tempSong, loc);
			songSet.put(song, loc + 1);
			songList.set(loc, tempSong);
			songList.set(loc + 1, song);
			loc ++;
		}
		//if votes < 0, remove from list
		if (song.getVotes() < 0) {
			songSet.remove(song);
			songList.remove(songList.size()-1);
		}
	}
	
	public void playNextSong() {
		songList.remove(0);
		//decrement indices of songs since the 0th song has been removed from the array
		for (Entry<PartySong, Integer>  e: songSet.entrySet()) {
			e.setValue(e.getValue()-1);
		}
	}
}
