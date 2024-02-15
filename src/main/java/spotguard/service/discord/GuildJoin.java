package spotguard.service.discord;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoin extends ListenerAdapter {

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		System.out.println("Joined new guild. Updating commands.");
		CommandHandler.instantiateCommands(event.getGuild());
	}
	
}
