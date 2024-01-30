package SpotGuard.manage;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hc.core5.http.ParseException;

import SpotGuard.api.Spotify.SpotifyAPI;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Followers;

public class Manager {
	
	public static HashMap<String, User> userMap = new HashMap<String, User>();

	public static HashMap<String, PlayList> playlistMap = new HashMap<String, PlayList>();
	
	public static HashMap<String, PlayList> whitelistRequests = new HashMap<String, PlayList>();
	
	public static void addPlayList(PlayList pl) {
		playlistMap.put(pl.playlistID, pl);
	}
	
	public static void addUser(User user, String discordID) {
		userMap.put(discordID, user);
	}
	
	public static HashMap<String, User> getUsers() {
		return userMap;
	}
	
	public static void registerPlaylist(PlayList pl) {
		try {
			Followers f = SpotifyAPI.getAPI().getPlaylist(pl.getPlaylistID()).build().execute().getFollowers();
			System.out.println(f.getHref());
			//SpotifyAPI.getAPI().
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void requestWhitelistAdd(String did, PlayList pl) {
//		whitelistRequests
	}
	
}
