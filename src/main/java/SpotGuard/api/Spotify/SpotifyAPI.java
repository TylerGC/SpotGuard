package SpotGuard.api.Spotify;

import java.io.IOException;
import java.net.URI;

import org.apache.hc.core5.http.ParseException;

import SpotGuard.manage.Manager;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

/**
 * Spotify API connection.
 * 
 * @author TylerGC
 *
 */
public class SpotifyAPI {
	
	  private static final String clientId = System.getenv("CLIENT_ID");
	  private static final String clientSecret = System.getenv("CLIENT_SECRET");
	  private static final URI redirectUri = SpotifyHttpManager.makeUri("http://spotguard.online/register/");

	  private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setClientId(clientId)
	    .setClientSecret(clientSecret)
	    .setRedirectUri(redirectUri)
	    .build();

	  public static String authorizationCodeUri(String state) {
		  AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
		          .state(state)
		          .scope("user-library-modify,user-library-read,playlist-modify-public")
		          .show_dialog(true)
		          .build();
	    final URI uri = authorizationCodeUriRequest.execute();
	    System.out.println("URI: " + uri.toString());
	    return uri.toString();
	  }
	  
	  private static AuthorizationCodeRequest authorizationCodeRequest = null;
	  
	  public static void authorizationCode(String code, String did) {
		  authorizationCodeRequest = spotifyApi.authorizationCode(code)
				    .build();
		    try {
		      final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

		      spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
		      spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
		      
		      System.out.println("Authenticated User: " + spotifyApi.getCurrentUsersProfile().build().execute().getDisplayName());
		      System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
		      
		      Manager.getUsers().get(did).setSpotifyID(spotifyApi.getCurrentUsersProfile().build().execute().getId());
		    } catch (IOException | SpotifyWebApiException | ParseException e) {
		      System.out.println("Error: " + e.getMessage());
		    }
		  }
	
	  public static SpotifyApi getAPI() {
		  return spotifyApi;
	  }
	  
}
