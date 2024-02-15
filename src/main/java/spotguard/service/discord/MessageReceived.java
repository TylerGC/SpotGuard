package spotguard.service.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceived extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getMessage().getContentStripped().equalsIgnoreCase("/refresh") && event.getGuild() != null) {
			CommandHandler.instantiateCommands(event.getGuild());
			event.getChannel().sendMessage("Commands refreshed for " + event.getGuild().getName()).submit();
		}
	}
	
}