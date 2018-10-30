package net.askigh.quizz.events;

import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.reactions.BotReaction;
import net.askigh.quizz.reactions.list.DeleteHelpReaction;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReactionsListener extends ListenerAdapter {

	/**
	 * @usage : This event is called whenever a reaction is added. It will be used in detection of game steps
	 */
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {

		// Getting all the variables we need
		User user = event.getUser();
		TextChannel channel = event.getTextChannel();
		String messageID = event.getMessageId();
		Message message = channel.getMessageById(messageID).complete();	

		// We want neither bot's reactions nor user's reactions towards other users
		if(user.isBot() || !message.getAuthor().isBot()) return;

		Game current = QuizMain.getCore().getManager().findGameByID(messageID);

		// No 'current == null' check : DeleteHelpReaction is an emote that doesn't belong to a game
		
		for(BotReaction emote : QuizMain.getCore().getEmotes()) {

			if(event.getReactionEmote().getName().equals(emote.getEmote())) {

				if(current != null && current.getState() == emote.getRequiredState()) {
					emote.execute(event, current, message, channel, user);
					return;
					
				}
				else if(current == null && emote instanceof DeleteHelpReaction) {
					emote.execute(null, null, message, channel, user);
					return;
					
				}
			}
		}
		
		// No known emote found, cancelling event
		event.getReaction().removeReaction().queue();
	}

}
