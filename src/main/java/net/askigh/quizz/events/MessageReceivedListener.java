package net.askigh.quizz.events;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {

	/**
	 *@usage : Method called whenever a message is sent. It's looking for bot's commands listed below
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {


		User sender = event.getAuthor();
		TextChannel channel = event.getTextChannel();
		Message message = event.getMessage();

		String messageContent = message.getRawContent();

		/* List of all available commands. 
		 * Type '?help' in a discord server containing "It's Quizz Time !" for further details
		 * Each command calls a method processing an action depending of the command
		 */
		for(BotCommand command : QuizMain.getCore().getCommands())
			if(messageContent.startsWith(command.getName()))
				command.execute(sender, channel, message);

	}

}
