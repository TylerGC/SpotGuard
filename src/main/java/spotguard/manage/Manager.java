package spotguard.manage;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import spotguard.service.spotify.SpotifyAPI;

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
	
	public static void applyAllPlaylistRules() {
		for (Entry<String, PlayList> entry : Manager.playlistMap.entrySet()) {
			PlayList pl = entry.getValue();
			if (pl.isProtected) {
				JsonArray toRemove = new JsonArray();
				final CompletableFuture<Paging<PlaylistTrack>> tracksFuture = SpotifyAPI.getAPI().getPlaylistsItems(pl.getPlaylistID()).build().executeAsync();
	            PlaylistTrack[] tracks = tracksFuture.join().getItems();
				for (PlaylistTrack plt : tracks) {
					if (!pl.whitelist.containsKey(plt.getAddedBy().getId())) {
						//TODO Incorporate "position" to ensure no tracks get deleted unnecessarily. This removes the need for a duplicate check but also means more API calls.
						JsonObject track = new JsonObject();
						track.addProperty("uri", plt.getTrack().getUri());
						if (!toRemove.contains(track))
							toRemove.add(track);
					}
					if(toRemove.size() >= 100) {
						removeItemsAsync(pl, toRemove);
						toRemove = new JsonArray(); // clear out the list and start new
					}
				}
				removeItemsAsync(pl, toRemove);
			}
		}
	}
	
	public static void removeItemsAsync(PlayList playlist, JsonArray items) {
		final CompletableFuture<SnapshotResult> removeFuture = SpotifyAPI.getAPI().removeItemsFromPlaylist(playlist.playlistID, items).build().executeAsync();
		SnapshotResult result = removeFuture.join();
		//TODO oh hey, snapshots are cool. Basically version control for playlists, you should really utilize that. Too bad you designed this before you knew that!

	}
	
}
