package net.askigh.quizz.reactions.list;

import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.reactions.BotReaction;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class JoinGameReaction extends BotReaction {

	public JoinGameReaction(String emote, GameState required) {
		super(emote, required);
		
	}

	@Override
	public void execute(MessageReactionAddEvent event, Game current, Message message, TextChannel channel, User user) {
		
		if(gameManager.findGameByUser(user) != null) {
			// User is already in a game
			message.editMessage(user.getAsMention()+", vous êtes déjà dans une partie !").queue();


		} else {

			current.addPlayer(user);
			message.editMessage(user.getAsMention()+" a rejoint la partie !").queue();

			if(current.needOwner()) 
				current.setOwner(user);
		}
		
		event.getReaction().removeReaction(user).queue();
		
	}




}
