package net.askigh.quizz.reactions.list;

import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.reactions.BotReaction;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class LeaveGameReaction extends BotReaction {

	public LeaveGameReaction(String emote, GameState required) {
		super(emote, required);
		
	}

	@Override
	public void execute(MessageReactionAddEvent event, Game current, Message message, TextChannel channel, User user) {
		
		Game game = gameManager.findGameByUser(user);

		if(game == null || !game.equals(current))
			message.editMessage(user.getAsMention()+", vous n'êtes pas dans la partie !").queue();

		else {

			message.editMessage(user.getAsMention()+" a quitté la partie...").queue();
			current.removePlayer(user);
		}
		event.getReaction().removeReaction(user).queue();

	}


}
