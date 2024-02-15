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
		System.out.println("Loaded playlist configuration.");
		new Controller();
		System.out.println("SpotGuard is armed.");
		new DiscordAPI();
		System.out.println("Discord API connection established.");
		Runtime.getRuntime().addShutdownHook(new Thread() 
	    { 
	      public void run() 
	      { 
	        System.out.println("Shutting down! Saving data...");
	        Controller.saveConfig();
	      } 
	    }); 
		System.out.println("Shutdown hook registered.");
		
		System.out.println("SpotGuard is ready for service!");
	}
	
}
