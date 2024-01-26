package SpotGuard.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import SpotGuard.api.Spotify.SpotifyAPI;

import java.util.Timer;
import java.util.TimerTask;

import de.sonallux.spotify.api.SpotifyApiException;
import de.sonallux.spotify.api.models.PlaylistTrack;

/**
 * Acts as the controller and user interface for SpotGuard. Allows users to interact with the playlist settings and rules, and routes actions.
 * 
 * TODO: Integrate with Web app/Android app to authorize with Spotify account. Only allow owners of playlist to edit playlist rules.
 * 
 * @author TylerGC
 *
 */
public class Controller {

	public Controller() {
		TimerTask clock = new TimerTask() {

			@Override
			public void run() {
				System.out.println("we clockin'");
				process();
			}
			
		};
		Timer timer = new Timer("Uhh");
		timer.scheduleAtFixedRate(clock, 50, 60000);

	}
	
	public static void loadConfig() {
		
	}
	
	public static void saveConfig() {
		
	}
	
	public static void process() {		
		for (Entry<String, PlayList> entry : Manager.playlistMap.entrySet()) {
			PlayList pl = entry.getValue();
			List<Map<String, Object>> toRemove = new ArrayList<Map<String, Object>>();
			try {
				for (PlaylistTrack plt : SpotifyAPI.getAPI().getPlaylistsApi().getPlaylistsTracks(pl.playlistID).build().execute().getItems()) {
					System.out.println("Track: " + plt.getTrack().getUri());
					if (!pl.whitelist.contains(plt.getAddedBy().getId())) {
						Map<String, Object> removeTrack = new HashMap<String, Object>();
						removeTrack.put("uri", plt.getTrack().getUri());
						//TODO Incorporate "position" to ensure no tracks get deleted unnecessarily. This removes the need for a duplicate check but also means more API calls.
						toRemove.add(removeTrack);
					}
					if(toRemove.size() >= 100) {
						SpotifyAPI.getAPI().getPlaylistsApi().removeTracksPlaylist(pl.playlistID, toRemove).build().execute();
						toRemove.clear();
					}
				}
				//request user auth here? Maybe thru email?
				SpotifyAPI.getAPI().getPlaylistsApi().removeTracksPlaylist(pl.playlistID, toRemove).build().execute();
			} catch (SpotifyApiException e) {
				if (e.getMessage().contains("403")) {
					//TODO Request user authentication for track removal.
				}
				e.printStackTrace();
			}
		}
	}
	
}
