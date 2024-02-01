package SpotGuard.api.Discord.ui;

import SpotGuard.manage.Manager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonHandler extends ListenerAdapter {

	//TODO Make Management screen one reply that you edit over and over. To stop spam,  and also unwanted form entries.
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		switch(event.getButton().getId()) {
		case "managebutton":
			event.deferReply(true).submit();
			event.getHook().sendMessage(Management.managementDisplay(event.getUser().getId())).setEphemeral(true).submit();
			break;
		case "whitelistbutton":
			//TODO Edit source event comment... requires MessageEditCreate/MessageEditData. Should be able to change whitelistDisplay return type as it's only accessible thru managementDisplay.
			event.deferReply(true).submit();
			event.getHook().sendMessage(Management.whitelistDisplay(event.getUser().getId())).setEphemeral(true).submit();
			break;
		case "protectbutton":
			Manager.playlistMap.get(Manager.getUsers().get(event.getUser().getId()).getAttribute("managePlaylistSelection")).setIsProtected(true);
			event.reply("Playlist is now being protected.").setEphemeral(true).submit();
			break;
		case "stopbutton":
			Manager.playlistMap.get(Manager.getUsers().get(event.getUser().getId()).getAttribute("managePlaylistSelection")).setIsProtected(false);
			break;
		case "backupbutton":
			//TODO Make a gameplan for this shit. Either select an existing playlist as destination or just straight up save URIs.
			//We're gonna save the current playlist items in the PlayList instance.
			break;
		case "restorebutton":
			//TODO Restore from PlayList instance's backup.
			//When we restore, set playlist to NOT protected. This way the changes aren't immediately reversed and the user can reconfigure.
			break;
		}
	}
	
	@Override
	public void onStringSelectInteraction(StringSelectInteractionEvent event) {
		System.out.println("StringSelectInteraction logged. ID: " + event.getComponentId() + ", selection: " + event.getSelectedOptions().getLast().getLabel());
		if (event.getComponent().getId().equals("managementPlaylists")) {
			Manager.getUsers().get(event.getUser().getId()).setAttribute("managePlaylistSelection", event.getSelectedOptions().getLast().getValue());
		} else if (event.getComponent().getId().equals("managementWhitelist")) {
			
		}
		event.deferEdit().submit(); //This allows us to log the change on our side without an event failure. Also implies we can edit the message to reflect the choices better. TODO!
	}
	
}
