package SpotGuard;

import SpotGuard.api.SpotifyAPI;
import SpotGuard.manage.Controller;

public class Main {

	public static void main(String... args) {
		System.out.println("Initializing SpotGuard...");
		SpotifyAPI.init();
		System.out.println("API connection established.");
		Controller.loadConfig();
		System.out.println("Loaded playlist configuration.");
	}
	
}
