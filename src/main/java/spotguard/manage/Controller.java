package spotguard.manage;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import se.michaelthelin.spotify.SpotifyApiThreading;

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
				trigger();
			}
			
		};
		
		Timer timer = new Timer("SpotGuardTick");
		timer.scheduleAtFixedRate(clock, 50, 60000);

	}
	
	public static void loadConfig() {
		
	}
	
	public static void saveConfig() {
		
	}
	
	public static void trigger() {
		SpotifyApiThreading.THREAD_POOL.submit(new Runnable() {

			@Override
			public void run() {
				process();
			}
			
		});
	}
	
	public static void process() {		
		System.out.println("Cycle: " + System.currentTimeMillis());
//		for (Entry<String, PlayList> entry : Manager.playlistMap.entrySet()) {
//			PlayList pl = entry.getValue();
//			//TODO check if playlist is protected or not!!
//			JsonArray toRemove = new JsonArray();
//			
//					final CompletableFuture<Paging<PlaylistTrack>> tracksFuture = SpotifyAPI.getAPI().getPlaylistsItems(pl.getPlaylistID()).build().executeAsync();
//	                PlaylistTrack[] tracks = tracksFuture.join().getItems();
//					for (PlaylistTrack plt : tracks) {
//						//System.out.println("Track: " + plt.getTrack().getUri());
//						if (!pl.whitelist.contains(plt.getAddedBy().getId())) {
//							//TODO Incorporate "position" to ensure no tracks get deleted unnecessarily. This removes the need for a duplicate check but also means more API calls.
//							JsonObject track = new JsonObject();
//							track.addProperty("uri", plt.getTrack().getUri());
//							toRemove.add(track);
//						}
//						if(toRemove.size() >= 100) {
//							//SpotifyAPI.getAPI().removeItemsFromPlaylist(pl.playlistID, toRemove).build().execute();
//							//TODO Queue removal
//							toRemove = new JsonArray(); // clear out the list and start new
//						}
//					}
//				//try {
//					//SpotifyAPI.getAPI().removeItemsFromPlaylist(pl.playlistID, toRemove).build().execute();
//				//} catch (ParseException | SpotifyWebApiException | IOException e) {
//				//	// TODO Auto-generated catch block
//				//	e.printStackTrace();
//				//}
//		}
	}
	
}
