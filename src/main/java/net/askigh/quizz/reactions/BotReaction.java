package net.askigh.quizz.reactions;

import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.utils.MessageSender;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public abstract class BotReaction {
	
	protected static MessageSender messageSender;
	protected static GameManager gameManager;
	private String emote;
	private GameState required;
	
	public BotReaction(String emote, GameState required) {
		
		this.emote = emote;
		this.required = required;
	}

	public abstract void execute(MessageReactionAddEvent event, Game current, Message message,
			TextChannel channel, User user);
	
	public String getEmote() {
		return emote;
	}
	
	public GameState getRequiredState() {
		return required;
	}

	public static void setSender(MessageSender sender) {
		
		BotReaction.messageSender = sender;
	}
	
	public static void setManager(GameManager manager) {
		
		BotReaction.gameManager = manager;
	}

}
