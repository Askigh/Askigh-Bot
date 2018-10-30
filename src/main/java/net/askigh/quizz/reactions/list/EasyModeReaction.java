package net.askigh.quizz.reactions.list;

import java.io.IOException;

import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.reactions.BotReaction;
import net.askigh.quizz.reactions.LeaderUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class EasyModeReaction extends BotReaction {

	public EasyModeReaction(String emote, GameState required) {
		super(emote, required);
		
	}

	@Override
	public void execute(MessageReactionAddEvent event, Game current, Message message, TextChannel channel, User user) {
		
		if(LeaderUtil.hasLead(current, user, channel)) {
			
			current.setDifficulty(1);
			
			try {
				messageSender.sendQuestion(current, channel);
			} catch (IOException e) {
				e.printStackTrace();
			}
			current.setState(GameState.PLAYING);
			
		} else event.getReaction().removeReaction(user).queue();
	}



}
