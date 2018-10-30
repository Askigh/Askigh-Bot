package net.askigh.quizz.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.commands.list.AddCommand;
import net.askigh.quizz.commands.list.BuzzCommand;
import net.askigh.quizz.commands.list.CodeCommand;
import net.askigh.quizz.commands.list.CreateCommand;
import net.askigh.quizz.commands.list.FilesCommand;
import net.askigh.quizz.commands.list.HelpCommand;
import net.askigh.quizz.commands.list.InfoCommand;
import net.askigh.quizz.commands.list.LeaveCommand;
import net.askigh.quizz.commands.list.PingCommand;
import net.askigh.quizz.commands.list.SignCommand;
import net.askigh.quizz.commands.list.StartCommand;
import net.askigh.quizz.commands.list.StatsCommand;
import net.askigh.quizz.core.game.GameManager;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.decoder.ConfigDecoder;
import net.askigh.quizz.decoder.QuestionsReader;
import net.askigh.quizz.reactions.BotReaction;
import net.askigh.quizz.reactions.list.DeleteHelpReaction;
import net.askigh.quizz.reactions.list.EasyModeReaction;
import net.askigh.quizz.reactions.list.FivePointsReaction;
import net.askigh.quizz.reactions.list.HardModeReaction;
import net.askigh.quizz.reactions.list.HardcoreModeReaction;
import net.askigh.quizz.reactions.list.JoinGameReaction;
import net.askigh.quizz.reactions.list.LeaveGameReaction;
import net.askigh.quizz.reactions.list.OnePointReaction;
import net.askigh.quizz.reactions.list.TwoPointsReaction;
import net.askigh.quizz.utils.MessageSender;

public class BotCore {

	private QuestionsReader questions;
	private ConfigDecoder config;
	private GameManager gameManager;
	private MessageSender sender;
	private List<String> helpIDMessages; 
	private List<BotCommand> commands;
	private List<BotReaction> reactions;

	public BotCore() {

		questions = new QuestionsReader();
		gameManager = new GameManager();
		helpIDMessages = new ArrayList<>();
		config = new ConfigDecoder();
		commands = new ArrayList<>();
		sender = new MessageSender();
		BotCommand.setSender(sender);
		BotCommand.setManager(gameManager);
		BotReaction.setSender(sender);
		BotReaction.setManager(gameManager);

		initCommands();
		initEmotes();
	}

	public Map<String, String> getQuestions() { return questions.getQuestions(); }
	public QuestionsReader getQuestionsReader() { return questions; }
	public ConfigDecoder getConfigDecoder() {
		return config;
	}
	public void addAsHelpMessage(String id) { helpIDMessages.add(id); }
	public boolean isHelpMessage(String id) { return helpIDMessages.contains(id); }

	private void initCommands() {

		commands = Arrays.asList(

				new AddCommand(),
				new BuzzCommand(),
				new CreateCommand(),
				new HelpCommand(),
				new InfoCommand(),
				new LeaveCommand(),
				new StartCommand(),
				new StatsCommand(),
				new CodeCommand(),
				new PingCommand(),
				new FilesCommand()

				);
	}

	private void initEmotes() {

		reactions = Arrays.asList(

				new DeleteHelpReaction("👁", null),
				new EasyModeReaction("😅", GameState.CHOOSING_DIFFICULTY),
				new FivePointsReaction("🖐", GameState.CHOOSING_POINTS_TO_WIN),
				new HardcoreModeReaction("😡", GameState.CHOOSING_DIFFICULTY),
				new HardModeReaction("😰", GameState.CHOOSING_DIFFICULTY),
				new JoinGameReaction("▶", GameState.WAITING),
				new LeaveGameReaction("🛑", GameState.WAITING),
				new OnePointReaction("☝", GameState.CHOOSING_POINTS_TO_WIN),
				new TwoPointsReaction("✌", GameState.CHOOSING_POINTS_TO_WIN)
				);
	}

	public List<BotCommand> getCommands() {

		return commands;
	}

	public BotCommand findCommandByName(String name) {

		for(BotCommand c : commands)
			if(c.getName().equals(name))
				return c;

		return null;
	}

	public GameManager getManager() {

		return gameManager;
	}

	public List<BotReaction> getEmotes() {
		return reactions;
	}

	public MessageSender getSender() {
		return sender;
	}
}
