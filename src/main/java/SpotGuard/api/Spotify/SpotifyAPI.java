package SpotGuard.api.Spotify;

import de.sonallux.spotify.api.SpotifyWebApi;
import de.sonallux.spotify.api.authorization.SpotifyAuthorizationException;
import de.sonallux.spotify.api.authorization.client_credentials.ClientCredentialsFlow;

/**
 * Spotify API connection.
 * 
 * @author TylerGC
 *
 */
public class SpotifyAPI {
	
	private static SpotifyWebApi spotifyApi;

	public SpotifyAPI() {		
		var clientCredentialsFlow = new ClientCredentialsFlow(System.getenv("CLIENT_ID"), System.getenv("CLIENT_SECRET"));
        spotifyApi = SpotifyWebApi.builder().authorization(clientCredentialsFlow).build();
        
        try {
        	clientCredentialsFlow.authorize();
        } catch (SpotifyAuthorizationException e) {
        	e.printStackTrace();
        }
	}
	
	public static SpotifyWebApi getAPI() {
		return spotifyApi;
	}
	
}
