package SpotGuard.api.Discord.ui;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import SpotGuard.api.Spotify.SpotifyAPI;
import SpotGuard.manage.Manager;
import SpotGuard.manage.PlayList;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.User;

public class Management {

	//Oh man... might want to think this system out some first. Didn't think Discord was so limited in what Modals will show.
	//Turns out you can't add text on it's own at all.
	//
	//Should definitely utilize StringSelectMenu. Probably create a Player-like "session" system so can track the selections by action...
	//https://javadoc.io/doc/net.dv8tion/JDA/latest/net/dv8tion/jda/api/interactions/components/selections/StringSelectMenu.html#create(java.lang.String)
	
	public static MessageCreateData managementDisplay(String did, String message) { 
		MessageCreateBuilder builder = new MessageCreateBuilder();
		if (Manager.getUsers().get(did) == null) {
			System.err.println("User needs to register.");
			return null; //TODO return Registration request method in the form of MessageCreateData.
		}
		String sid = Manager.getUsers().get(did).getSpotifyID();
//		try {
			ArrayList<SelectOption> playlists = new ArrayList<SelectOption>();
			Future<Paging<PlaylistSimplified>> playlistsFuture = SpotifyAPI.getAPI().getListOfUsersPlaylists(sid).build().executeAsync();
			PlaylistSimplified[] psa = new PlaylistSimplified[0];
			try {
				psa = playlistsFuture.get().getItems();
			for (PlaylistSimplified ps : psa) {
				if (ps.getIsPublicAccess()) {
					if (!Manager.playlistMap.containsKey(ps.getId())) {
						PlayList pl = new PlayList(ps.getId(), ps.getOwner().getId(), did);
						for (int i = 0; i < 110; i++) {
							final CompletableFuture<Paging<PlaylistTrack>> tracksFuture = SpotifyAPI.getAPI().getPlaylistsItems(pl.getPlaylistID()).offset(i * 100).build().executeAsync();
							PlaylistTrack[] tracks = tracksFuture.join().getItems();
							for (PlaylistTrack plt : tracks) {
								pl.getWhitelist().put(plt.getAddedBy().getId(), true);
							}
							if (tracks.length < 100) {
								break;
							}
						}
						Manager.addPlayList(pl);
					}
					String status = Manager.playlistMap.get(ps.getId()).getIsProtected() ? "Protected" : "Vulnerable";
					playlists.add(SelectOption.of(ps.getName(), ps.getId()).withDescription(status));
					if(playlists.size() >= 25) {
						break;
					}
				}
			}
			StringSelectMenu playlistMenu = StringSelectMenu.create("managementPlaylists").addOptions(playlists).build();
			builder.setContent(message + "\n**Please select the playlist you would like to manage.**");
			builder.addActionRow(playlistMenu);
			//TODO Break out buttons? Make it so Restore is disabled if no backup present.
			builder.addActionRow(Button.of(ButtonStyle.PRIMARY, "whitelistbutton", "Whitelist"), Button.of(ButtonStyle.PRIMARY, "backupbutton", "Backup").asDisabled(), Button.of(ButtonStyle.PRIMARY, "protectbutton", "Protect"), Button.of(ButtonStyle.DANGER, "stopbutton", "Stop"), Button.of(ButtonStyle.SECONDARY, "restorebutton", "Restore").asDisabled());
			} catch (InterruptedException | ExecutionException e) {
				//Shit we're dumb lmao, use e.getCause() to retrieve the TooManyRequestsException in order to get the retry time... gah damn lmao.
				//((TooManyRequestsException)e.getCause()).getRetryAfter();
				//Also have UnauthorizedException: The access token expired //obviously this is when we would request the user for a new token. :)
				//
				//IMPORTANT: We're going to have to make managementDisplay() THROW the error(s), so we can catch them in a place that allows us to send
				//this request back to the right place, like where the Event is.
				if (e.getMessage().contains("Too Many Requests")) {
					System.out.println("Getting throttled!");
					SpotifyAPI.throttleWait();
//					managementDisplay(did, message);
					return null;
				}
				e.printStackTrace();
			}
			return builder.build();
//		} catch (ParseException | SpotifyWebApiException | IOException e) {
//			if (e instanceof UnauthorizedException) {
//				System.err.println("Token is probably expired. Take this time to send the user an auth request.");
//			} else if (e instanceof TooManyRequestsException) {
//				System.out.println("Retry after: " + ((TooManyRequestsException)e).getRetryAfter() + " seconds");
//				//builder.clear(); //Oooh so we can reference it if it's outside of the try/catch... we should use this to queue calls.
//			}
//			e.printStackTrace();
//		}
//		return null;
	}
	
	public static MessageCreateData whitelistDisplay(String did) {
		//"managementWhitelist"
		MessageCreateBuilder builder = new MessageCreateBuilder();
		ArrayList<SelectOption> users = new ArrayList<SelectOption>();
		PlayList pl = Manager.playlistMap.get((String)Manager.getUsers().get(did).getAttribute(("managePlaylistSelection")));
			for (String id : pl.getWhitelistMembers()) {
				final CompletableFuture<User> userFuture = SpotifyAPI.getAPI().getUsersProfile(id).build().executeAsync();
				User user = userFuture.join();
				users.add(SelectOption.of(user.getDisplayName(), id).withDescription(pl.isWhitelisted(user.getId()) ? "Allowed" : "Disallowed"));

			}		
		StringSelectMenu userMenu = StringSelectMenu.create("managementWhitelist").addOptions(users).build();
		builder.setContent("Please select users to manage.");
		builder.addActionRow(userMenu);
		builder.addActionRow(Button.of(ButtonStyle.SUCCESS, "allowbutton", "Allow"), Button.of(ButtonStyle.DANGER, "disallowbutton", "Disallow"), Button.of(ButtonStyle.DANGER, "removebutton", "Remove").asDisabled(), Button.of(ButtonStyle.SECONDARY, "backbutton", "Back"));
		return builder.build();
	}
	
	public static MessageCreateData backupDisplay(String did) {
		return null;
	}
	
}
