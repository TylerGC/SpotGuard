package spotguard.service.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import spotguard.service.discord.ui.ButtonHandler;

public class DiscordAPI {

	static JDA discordAPI;
	
	//TODO Add sharding.
	public DiscordAPI() {
		JDABuilder builder = JDABuilder.createDefault(System.getenv("DISCORD_API"), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES);
		builder.addEventListeners(new CommandHandler(), new MessageReceived(), new GuildJoin(), new ButtonHandler());
		discordAPI = builder.build();
	}
	
	public static JDA getAPI() {
		return discordAPI;
	}
	
}
