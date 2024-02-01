package SpotGuard.manage;

import java.util.HashMap;

public class Manager {
	
	public static HashMap<String, User> userMap = new HashMap<String, User>();

	public static HashMap<String, PlayList> playlistMap = new HashMap<String, PlayList>();
	
	public static HashMap<String, PlayList> whitelistRequests = new HashMap<String, PlayList>();
	
	public static void addPlayList(PlayList pl) {
		if (!playlistMap.containsKey(pl.getPlaylistID()))
			playlistMap.put(pl.playlistID, pl);
	}
	
	public static void addUser(User user, String discordID) {
		userMap.put(discordID, user);
	}
	
	public static HashMap<String, User> getUsers() {
		return userMap;
	}
	
	public static void requestWhitelistAdd(String did, PlayList pl) {
//		whitelistRequests
	}
	
}
