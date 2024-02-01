package SpotGuard.api.Discord.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.apache.hc.core5.http.ParseException;

import SpotGuard.api.Spotify.SpotifyAPI;
import SpotGuard.manage.Manager;
import SpotGuard.manage.PlayList;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.TooManyRequestsException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
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
	
	public static MessageCreateData managementDisplay(String did) { // Damn... StringSelectMenus are not allowed in Modals. This will have to be an ephemeral embedded message. :(
		MessageCreateBuilder builder = new MessageCreateBuilder();
		if (Manager.getUsers().get(did) == null) {
			System.err.println("User needs to register.");
			return null; //TODO return Registration request method in the form of MessageCreateData.
		}
		String sid = Manager.getUsers().get(did).getSpotifyID();
//		try {
			ArrayList<SelectOption> playlists = new ArrayList<SelectOption>();
			final CompletableFuture<Paging<PlaylistSimplified>> tracksFuture = SpotifyAPI.getAPI().getListOfUsersPlaylists(sid).build().executeAsync();
			PlaylistSimplified[] psa = tracksFuture.join().getItems();
			for (PlaylistSimplified ps : psa) {
				if (ps.getIsPublicAccess()) {
					PlayList pl = new PlayList(ps.getId(), ps.getOwner().getId(), did);
					Manager.addPlayList(pl);
					String status = Manager.playlistMap.get(ps.getId()).getIsProtected() ? "Protected" : "Vulnerable";
					playlists.add(SelectOption.of(ps.getName(), ps.getId()).withDescription(status));
					if(playlists.size() >= 25) {
						break;
					}
				}
			}
			StringSelectMenu playlistMenu = StringSelectMenu.create("managementPlaylists").addOptions(playlists).build();
			builder.setContent("Please select the playlist you would like to manage.");
			builder.addActionRow(playlistMenu);
			builder.addActionRow(Button.of(ButtonStyle.PRIMARY, "whitelistbutton", "Whitelist"), Button.of(ButtonStyle.PRIMARY, "backupbutton", "Backup").asDisabled(), Button.of(ButtonStyle.PRIMARY, "protectbutton", "Protect"), Button.of(ButtonStyle.DANGER, "stopbutton", "Stop"));
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
		String sid = Manager.getUsers().get(did).getSpotifyID();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<SelectOption> users = new ArrayList<SelectOption>();
			for (String id : Manager.playlistMap.get((String)Manager.getUsers().get(did).getAttribute("managePlaylistSelection")).getWhitelist()) {
				final CompletableFuture<User> userFuture = SpotifyAPI.getAPI().getUsersProfile(id).build().executeAsync();
				String username = userFuture.join().getDisplayName();
				users.add(SelectOption.of(username, id));

			}		
		StringSelectMenu userMenu = StringSelectMenu.create("managementWhitelist").addOptions(users).build();
		builder.setContent("Please select users to manage.");
		builder.addActionRow(userMenu);
		return builder.build();
	}
	
	public static MessageCreateData backupDisplay(String did) {
		return null;
	}
	
}
