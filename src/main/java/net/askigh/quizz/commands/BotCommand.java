package net.askigh.quizz.commands;

import net.askigh.quizz.core.game.GameManager;
import net.askigh.quizz.utils.MessageSender;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class BotCommand {
	
	protected static MessageSender messageSender;
	protected static GameManager gameManager;

	public abstract void execute(User sender, TextChannel channel, Message message);
	public abstract String getName();
	public abstract String getDescription();

	public static void setSender(MessageSender sender) { BotCommand.messageSender = sender; }
	public static void setManager(GameManager manager) { BotCommand.gameManager = manager; }
	
}
