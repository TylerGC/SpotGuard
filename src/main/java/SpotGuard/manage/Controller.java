package SpotGuard.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.hc.core5.http.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import SpotGuard.api.Spotify.SpotifyAPI;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

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
			//TODO check if playlist is protected or not!!
			JsonArray toRemove = new JsonArray();
			 
				try {
					for (PlaylistTrack plt : SpotifyAPI.getAPI().getPlaylistsItems(pl.playlistID).build().execute().getItems()) {
						System.out.println("Track: " + plt.getTrack().getUri());
						if (!pl.whitelist.contains(plt.getAddedBy().getId())) {
							//TODO Incorporate "position" to ensure no tracks get deleted unnecessarily. This removes the need for a duplicate check but also means more API calls.
							JsonObject track = new JsonObject();
							track.addProperty("uri", plt.getTrack().getUri());
							toRemove.add(track);
						}
						if(toRemove.size() >= 100) {
							//SpotifyAPI.getAPI().removeItemsFromPlaylist(pl.playlistID, toRemove).build().execute();
							//TODO Queue removal
							toRemove = new JsonArray(); // clear out the list and start new
						}
					}
				} catch (ParseException | SpotifyWebApiException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//try {
					//SpotifyAPI.getAPI().removeItemsFromPlaylist(pl.playlistID, toRemove).build().execute();
				//} catch (ParseException | SpotifyWebApiException | IOException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
		}
	}
	
}
