package spotguard.service.spotify;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.apache.hc.core5.http.ParseException;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyApiThreading;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.TooManyRequestsException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;
import spotguard.manage.Manager;

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
	  private static int retryTime = 100;

	  private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setClientId(clientId)
	    .setClientSecret(clientSecret)
	    .setRedirectUri(redirectUri)
	    .build();

	  public static String authorizationCodeUri(String state) {
		  AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
		          .state(state)
		          .scope("user-library-modify,user-library-read,playlist-modify-public,user-read-private,user-read-email")
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
		      e.printStackTrace();
		    }
		  }
	
	  public static SpotifyApi getAPI() {
		  return spotifyApi;
	  }
	  
	  public static void throttleWait(int retryTime) {
		  try {
			  System.out.println("Telling thread to wait for " + (retryTime * 1000));
			  SpotifyApiThreading.THREAD_POOL.wait(retryTime * 1000);//.wait(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	  }
	  
	  public static CompletableFuture<AbstractDataRequest<?>> queue(AbstractDataRequest<AbstractDataRequest<?>> request) {
		  return request.executeAsync();
	  }
	  
}
