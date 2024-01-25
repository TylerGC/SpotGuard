package SpotGuard.api;

import de.sonallux.spotify.api.SpotifyWebApi;
import de.sonallux.spotify.api.authorization.ApiAuthorizationProvider;
import de.sonallux.spotify.api.authorization.SimpleApiAuthorizationProvider;

/**
 * Spotify API connection.
 * 
 * @author TylerGC
 *
 */
public class SpotifyAPI {
	
	static ApiAuthorizationProvider authProvider;
	static SpotifyWebApi spotifyApi;

	public static void init() {
		authProvider = new SimpleApiAuthorizationProvider(System.getenv("PLAYLISTGUARD_TOKEN"));
		spotifyApi = SpotifyWebApi.builder().authorization(authProvider).build();

		//var artist = spotifyApi.getArtistsApi().getArtist("<artist id>").build().execute();
		//System.out.println(artist.getName());
	} 
	
	public static SpotifyWebApi getAPI() {
		return spotifyApi;
	}
	
}
