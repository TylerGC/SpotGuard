package SpotGuard.api.Discord.ui;

import SpotGuard.manage.Manager;
import SpotGuard.manage.PlayList;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class ButtonHandler extends ListenerAdapter {

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		switch(event.getButton().getId()) {
		case "managebutton":
			event.deferReply(true).submit();
			event.getHook().sendMessage(Management.managementDisplay(event.getUser().getId(), "")).setEphemeral(true).submit();
			break;
			
		case "whitelistbutton":
			event.deferEdit().submit();
			MessageEditBuilder builder = new MessageEditBuilder();
			builder.applyCreateData(Management.whitelistDisplay(event.getUser().getId()));
			event.getHook().editMessageById(event.getMessageId(), builder.build()).submit();
			break;
			
		case "protectbutton":
			event.deferEdit().submit();
			PlayList protectPl = Manager.playlistMap.get(Manager.getUsers().get(event.getUser().getId()).getAttribute("managePlaylistSelection"));
			protectPl.setIsProtected(true);
			MessageEditBuilder protectEditBuilder = new MessageEditBuilder();
			protectEditBuilder.applyCreateData(Management.managementDisplay(event.getUser().getId(), "*Playlist is now being protected!*"));
			event.getHook().editMessageById(event.getMessageId(), protectEditBuilder.build()).submit();
			break;
			
		case "stopbutton":
			event.deferEdit().submit();
			PlayList stopPl = Manager.playlistMap.get(Manager.getUsers().get(event.getUser().getId()).getAttribute("managePlaylistSelection"));
			stopPl.setIsProtected(false);
			MessageEditBuilder stopEditBuilder = new MessageEditBuilder();
			stopEditBuilder.applyCreateData(Management.managementDisplay(event.getUser().getId(), "*Playlist is no longer being protected!*"));
			event.getHook().editMessageById(event.getMessageId(), stopEditBuilder.build()).submit();
			break;
			
		case "backupbutton":
			//TODO Make a gameplan for this shit. Either select an existing playlist as destination or just straight up save URIs.
			//We're gonna save the current playlist items in the PlayList instance.
			break;
			
		case "restorebutton":
			//TODO Restore from PlayList instance's backup.
			//When we restore, set playlist to NOT protected. This way the changes aren't immediately reversed and the user can reconfigure.
			event.deferEdit().submit();
			PlayList backupPl = Manager.playlistMap.get(Manager.getUsers().get(event.getUser().getId()).getAttribute("managePlaylistSelection"));
			backupPl.setIsProtected(false);
			//TODO restore from backup.
			MessageEditBuilder backupEditBuilder = new MessageEditBuilder();
			backupEditBuilder.applyCreateData(Management.managementDisplay(event.getUser().getId(), "*Playlist has been successfully backed up!*"));
			event.getHook().editMessageById(event.getMessageId(), backupEditBuilder.build()).submit();
			break;
			
		case "backbutton":
			event.deferEdit().submit();
			MessageEditBuilder backBuilder = new MessageEditBuilder();
			backBuilder.applyCreateData(Management.managementDisplay(event.getUser().getId(), ""));
			event.getHook().editMessageById(event.getMessageId(), backBuilder.build()).submit();
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
