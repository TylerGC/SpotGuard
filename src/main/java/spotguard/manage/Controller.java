package spotguard.manage;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import se.michaelthelin.spotify.SpotifyApiThreading;
import se.michaelthelin.spotify.exceptions.detailed.TooManyRequestsException;
import spotguard.service.spotify.SpotifyAPI;

/**
 * Acts as the controller for SpotGuard. Executes SpotGuard's background tasks.
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
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
		        .setPrettyPrinting().create();
		try {
			String userJson = Files.readString(Path.of("./data/users.JSON"));
			String playlistJson = Files.readString(Path.of("./data/playlists.JSON"));
			Type userMapType = new TypeToken<HashMap<String, User>>() { }.getType();
			Type playlistMapType = new TypeToken<HashMap<String, PlayList>>() { }.getType();
			HashMap<String, User> loadedUserMap = gson.fromJson(userJson, userMapType);
			HashMap<String, PlayList> loadedPlaylistMap = gson.fromJson(playlistJson, playlistMapType);
			Manager.userMap = loadedUserMap;
			Manager.playlistMap = loadedPlaylistMap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveConfig() {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
		        .setPrettyPrinting().create();
		try {
			FileWriter fwu = new FileWriter("./data/users.JSON");
			gson.toJson(Manager.userMap, fwu);
			FileWriter fwp = new FileWriter("./data/playlists.JSON");
			gson.toJson(Manager.playlistMap, fwp);
			fwu.close();
			fwp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
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
		try {
			Manager.applyAllPlaylistRules();
		} catch (InterruptedException | ExecutionException e) {
			if (e.getCause() instanceof TooManyRequestsException) {
				System.out.println("Retry in: " + ((TooManyRequestsException)e.getCause()).getRetryAfter());
				SpotifyAPI.throttleWait(((TooManyRequestsException)e.getCause()).getRetryAfter());
			} else {
				e.printStackTrace();
			}
		}
		saveConfig();
	}
	
}
