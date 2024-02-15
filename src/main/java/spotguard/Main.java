package spotguard;

import spotguard.manage.Controller;
import spotguard.service.discord.DiscordAPI;
import spotguard.service.spotify.SpotifyAPI;
import spotguard.service.spotify.http.SimpleHTTPServer;

public class Main {

	public static void main(String... args) {
		System.out.println("Initializing SpotGuard...");
		SimpleHTTPServer.listen();
		System.out.println("HTTP Auth Response Server opened.");
		new SpotifyAPI();
		System.out.println("Spotify API connection established.");
		Controller.loadConfig();
		
		// TODO Remove test data
//		PlayList pl = new PlayList("4O0shT6h6OlY8VfZLeA7Pb", "1217057623");
//		pl.getWhitelist().add("1217057623");
//		Manager.addPlayList(pl);
		
		System.out.println("Loaded playlist configuration.");
		new Controller();
		System.out.println("SpotGuard is armed.");
		new DiscordAPI();
		System.out.println("Discord API connection established.");
		//TODO Add shutdown hook
	}
	
}
