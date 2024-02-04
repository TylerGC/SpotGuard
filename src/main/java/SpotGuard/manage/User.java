package SpotGuard.manage;

import java.util.HashMap;

public class User {

	private String discordID;
	private String spotifyID;
	private String token;
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private boolean managing = false;
	
	public User(String did, String sid, String token) {
		discordID = did;
		spotifyID = sid;
		this.token = token;
	}
	
	public User(String did, String token) {
		discordID = did;
		this.token = token;
	}
	
	public String getDiscordID() {
		return discordID;
	}
	
	public String getSpotifyID() {
		return spotifyID;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String t) {
		token = t;
	}
	
	public void setSpotifyID(String sid) {
		spotifyID = sid;
	}
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public boolean isManaging() {
		return managing;
	}
	
	public void setManaging(boolean bool) {
		managing = bool;
	}
	
}
