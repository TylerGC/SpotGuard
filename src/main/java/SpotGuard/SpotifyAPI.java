package SpotGuard;

import de.sonallux.spotify.api.SpotifyWebApi;
import de.sonallux.spotify.api.authorization.ApiAuthorizationProvider;
import de.sonallux.spotify.api.authorization.SimpleApiAuthorizationProvider;

public class SpotifyAPI {
	
	static ApiAuthorizationProvider authProvider;
	static SpotifyWebApi spotifyApi;

	public void init() {
		authProvider = new SimpleApiAuthorizationProvider("<your access token>");
		spotifyApi = SpotifyWebApi.builder().authorization(authProvider).build();

		//var artist = spotifyApi.getArtistsApi().getArtist("<artist id>").build().execute();
		//System.out.println(artist.getName());
	}
	
}
