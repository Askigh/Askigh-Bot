package net.askigh.quizz.commands.list;

import java.io.IOException;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class StartCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		Game game = gameManager.findGameByUser(sender);

		// Ignored if user doesn't belong to any game
		if(game == null) {

			message.delete().complete();	
			return;
		}
		
		Message bot = channel.getMessageById(game.getMessageID()).complete();
		
		if(game.getState() != GameState.WAITING || !game.getOwner().equals(sender)) {

			
			bot.editMessage(sender.getAsMention()+", impossible de faire cela...").complete();
			return;
		}
		
		/*if(game.getPlayers().size() < 2) {
			bot.editMessage(sender.getAsMention()+", vous ne pouvez pas commencer une partie seul(e) !").queue();
			message.delete().queue();
			return;
		}*/

		game.addGenerateMessages(message.getId());

		prepareChoosingMode(game, channel);

		for(User player : game.getPlayers().keySet())
			try {
				QuizMain.getCore().getConfigDecoder().readGameCountOf(player);
				QuizMain.getCore().getConfigDecoder().addGameCountOf(player);
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void prepareChoosingMode(Game game, TextChannel channel) {

		game.setState(GameState.CHOOSING_POINTS_TO_WIN);

		try {
			messageSender.sendChoosingModeMessage(channel, game);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getName() {
		return "?start";
	}

	@Override
	public String getDescription() {
		return "Démarre la partie, vous devez être le chef de partie pour faire cela !";
	}
}
