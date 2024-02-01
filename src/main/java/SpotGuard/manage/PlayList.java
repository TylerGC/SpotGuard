package SpotGuard.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hc.core5.http.ParseException;

import SpotGuard.api.Discord.DiscordAPI;
import SpotGuard.api.Spotify.SpotifyAPI;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.TooManyRequestsException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

/**
 * A PlayList object that represents a playlist under SpotGuard's protection, including the rules to follow and enforce.
 * 
 * @author TylerGC
 *
 */
public class PlayList {
	
	String playlistID;
	String ownerID;
	String discordID;
	List<String> whitelist = new ArrayList<String>();
	boolean isProtected = false;

	public PlayList(String plid, String oid, String did) {
		playlistID = plid;
		ownerID = oid;
		discordID = did;
		whitelist.add(ownerID);
			for (int i = 0; i < 110; i++) {
				final CompletableFuture<Paging<PlaylistTrack>> tracksFuture = SpotifyAPI.getAPI().getPlaylistsItems(plid).offset(i * 100).build().executeAsync();
				PlaylistTrack[] tracks = tracksFuture.join().getItems();
				for (PlaylistTrack plt : tracks) {
					addToWhitelist(plt.getAddedBy().getId());
				}
				if (tracks.length < 100) {
					break;
				}
			}
//		} catch (ParseException | SpotifyWebApiException | IOException e) {
//			if (e instanceof TooManyRequestsException) {
//				//TODO handle this bullshit >:(
//				System.out.println("Retry after: " + ((TooManyRequestsException)e).getRetryAfter() + " seconds");
//			}
//			e.printStackTrace();
	}
	
	public List<String> getWhitelist() {
		return whitelist;
	}
	
	public void addToWhitelist(String userID) {
		if(!whitelist.contains(userID))
			whitelist.add(userID);
	}
	
	public String getOwner() {
		return ownerID;
	}
	
	public String getOwnerDiscordID() {
		return discordID;
	}
	
	public String getPlaylistID() {
		return playlistID;
	}
	
	public void setIsProtected(boolean bool) {
		isProtected = bool;
	}
	
	public boolean getIsProtected() {
		return isProtected;
	}
	
}
