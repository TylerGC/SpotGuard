package SpotGuard;

import SpotGuard.api.Spotify.SpotifyAPI;
import SpotGuard.manage.Controller;
import SpotGuard.manage.Manager;
import SpotGuard.manage.PlayList;
import de.sonallux.spotify.api.SpotifyApiException;

public class Main {

	public static void main(String... args) {
		System.out.println("Initializing SpotGuard...");
		new SpotifyAPI();
		System.out.println("API connection established.");
		Controller.loadConfig();
		
		// TODO Remove test data
		PlayList pl = new PlayList("4O0shT6h6OlY8VfZLeA7Pb", "1217057623");
		pl.getWhitelist().add("1217057623");
		Manager.addPlayList(pl);
		
		System.out.println("Loaded playlist configuration.");
		new Controller();
		System.out.println("SpotGuard is armed.");
		//TODO Add shutdown hook
	}
	
}
