package spotguard.service.discord;

import java.security.NoSuchAlgorithmException;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import spotguard.manage.Controller;
import spotguard.service.discord.ui.Management;
import spotguard.service.discord.ui.Registration;

public class CommandHandler extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (event.getCommandString().equalsIgnoreCase("/refresh")) {
			instantiateCommands(event.getGuild());
			event.reply("Commands refreshed for guild: " + event.getGuild().getName()).setEphemeral(true).submit();
		} else if (event.getName().contentEquals("test")) {
	         TextInput subject = TextInput.create("subject", "Subject", TextInputStyle.SHORT)
	                 .setPlaceholder("Subject of this ticket")
	                 .setMinLength(10)
	                 .setMaxLength(100) // or setRequiredRange(10, 100)
	                 .build();
			Modal modal = Modal.create("This is a test modal.", "Test Title")
					.addActionRows(ActionRow.of(subject))
					.build();
			event.replyModal(modal).submit();
			//Message message = new MessageCreateBuilder().addComponents(modal).build();
		} else if (event.getCommandString().equalsIgnoreCase("/register")) {
			try {
				//event.reply(SpotifyAPI.authorizationCodeUri(state)).setEphemeral(true).submit();
				Registration.sendRegistrationForm(event);
				//event.reply("SpotGuard requires some permissions to protect your playlists. Please click the button below to get started!").addActionRow(Button.link(SpotifyAPI.authorizationCodeUri(state), "Authorize")).setEphemeral(true).submit();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				//make another without state inclusion... inform user of additional steps
				e.printStackTrace();
			}
		} else if (event.getName().contentEquals("manage")) {
			event.deferReply(true).submit();
			event.getHook().sendMessage(Management.managementDisplay(event.getUser().getId(), "")).setEphemeral(true).submit();
		} else if (event.getName().contentEquals("save")) {
			event.deferReply(true).submit();
			Controller.saveConfig();
		}
	}
	
	public static void instantiateCommands(Guild guilds) {
		guilds.updateCommands()
		.addCommands(Commands.slash("refresh", "Refreshes commands for guild."),
					Commands.slash("register", "Register your Spotify account with SpotGuard."),
					Commands.slash("manage", "Manage your Spotify playlist protection."),
					Commands.slash("save", "Test action for serialization of data"))
		.submit();
				//.addOption(OptionType.STRING, "<prompt>", "How you would like the AI to respond.", true));
	}
}
