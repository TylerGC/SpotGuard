package SpotGuard.api.Discord.ui;

import java.io.IOException;
import java.util.ArrayList;

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
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

public class Management {

	//Oh man... might want to think this system out some first. Didn't think Discord was so limited in what Modals will show.
	//Turns out you can't add text on it's own at all.
	//
	//Should definitely utilize StringSelectMenu. Probably create a Player-like "session" system so can track the selections by action...
	//https://javadoc.io/doc/net.dv8tion/JDA/latest/net/dv8tion/jda/api/interactions/components/selections/StringSelectMenu.html#create(java.lang.String)
	
	public static MessageCreateData managementDisplay(String did) { // Damn... StringSelectMenus are not allowed in Modals. This will have to be an ephemeral embedded message. :(
		MessageCreateBuilder builder = new MessageCreateBuilder();
		String sid = Manager.getUsers().get(did).getSpotifyID();
		try {
			ArrayList<SelectOption> playlists = new ArrayList<SelectOption>();
			//TODO Check if playlist is owned by user! This helps reduce errors as well as stay within SelectMenu limits (25)
			for (PlaylistSimplified ps : SpotifyAPI.getAPI().getListOfUsersPlaylists(sid).build().execute().getItems()) {
				System.out.println("Found a playlist. Name: " + ps.getName() + ", coll: " + ps.getIsCollaborative() + ", public: " + ps.getIsPublicAccess());
				if (ps.getIsPublicAccess()) {
					PlayList pl = new PlayList(ps.getId(), ps.getOwner().getId(), did);
					Manager.addPlayList(pl);
					System.out.println("Playlist applies!");
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
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static MessageCreateData whitelistDisplay(String did) {
		//"managementWhitelist"
		//Gonna have to read playlist items (tracks) and getAddedBy.
		//Kind of a blessing in disguise because then the whitelist will be pre-populated, reducing potential for damage.
		MessageCreateBuilder builder = new MessageCreateBuilder();
		String sid = Manager.getUsers().get(did).getSpotifyID();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<SelectOption> users = new ArrayList<SelectOption>();
		try {
			for (PlaylistTrack pt : SpotifyAPI.getAPI().getPlaylist((String)Manager.getUsers().get(did).getAttribute("managePlaylistSelection")).build().execute().getTracks().getItems()) {
				System.out.println("We have a playlist track! Added by: " + pt.getAddedBy().getDisplayName());
				if (!names.contains(pt.getAddedBy().getId())) {
					names.add(pt.getAddedBy().getId());
					users.add(SelectOption.of(pt.getAddedBy().getDisplayName(), pt.getAddedBy().getId()).withDescription(Manager.playlistMap.get((String)Manager.getUsers().get(did).getAttribute("managePlaylistSelection")).getWhitelist().contains(pt.getAddedBy().getId()) ? "Allowed" : "Blocked"));
				}
			}
			StringSelectMenu userMenu = StringSelectMenu.create("managementWhitelist").addOptions(users).build();
			builder.setContent("Please select users to manage.");
			builder.addActionRow(userMenu);
			return builder.build();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static MessageCreateData backupDisplay(String did) {
		return null;
	}
	
}
