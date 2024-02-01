package SpotGuard.api.Discord.ui;

import java.security.NoSuchAlgorithmException;

import SpotGuard.api.Spotify.SpotifyAPI;
import SpotGuard.api.Spotify.http.RequestHandler;
import SpotGuard.manage.PlayList;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class Registration {

	public static void sendRegistrationForm(SlashCommandInteractionEvent event) throws NoSuchAlgorithmException {
		MessageCreateBuilder builder = new MessageCreateBuilder();
		MessageEmbed me = new MessageEmbed(null, "Registration Form", "SpotGuard requires some permissions to protect your playlists. Please click the button below to get started!", EmbedType.UNKNOWN, null, 0, null, null, null, null, null, null, null);
		builder.addEmbeds(me);
		String state = RequestHandler.createMD5Hash(event.getUser().getId());
		RequestHandler.addRequest(event.getUser(), state);
		builder.addActionRow(Button.link(SpotifyAPI.authorizationCodeUri(state), "Authorize"));//.setEphemeral(true)
		event.reply(builder.build()).setEphemeral(true).submit();
	}
	
	public static void sendRegistrationReply(User user) {
		MessageCreateBuilder builder = new MessageCreateBuilder();
		MessageEmbed me = new MessageEmbed(null, "Thank you for registering with SpotGuard!", "Now that you are registered, you can begin protecting your playlists with SpotGuard!\n\nClick on the 'Manage' button below to configure SpotGuard.\n\n**Note:** *Opening manager may take a while the first time you open it, especially if you have large playlists.* \n\nSpotGuard may contact you again to authorize actions on your behalf.", EmbedType.UNKNOWN, null, 0, null, null, null, null, null, null, null);
		Button manage = Button.primary("managebutton", "Manage");
		Button why = Button.secondary("registrationreplywhy", "Why?");
		builder.addEmbeds(me)
		.addActionRow(manage, why);
		user.openPrivateChannel().flatMap(channel -> channel.sendMessage(builder.build())).submit();
	}
	
	/**
	 * Sends the playlist owner a Discord message detailing a user's request to be added to the whitelist for one of their playlists.
	 * @param did The user requesting whitelist access.
	 * @param pl The PlayList the user is requesting whitelist access to.
	 */
	public static void sendWhitelistRequest(String did, PlayList pl) {
		
	}
	
	/**
	 * 
	 * @param did
	 * @param pl
	 * @param approved
	 */
	public static void sendWhitelistRequestResponse(String did, PlayList pl, boolean approved) {
		
	}
	
}
