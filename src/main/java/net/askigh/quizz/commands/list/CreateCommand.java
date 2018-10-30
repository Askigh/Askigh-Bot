package net.askigh.quizz.commands.list;

import java.io.IOException;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.decoder.QuestionsReader;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CreateCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {

		String[] args = message.getContent().split(" ");

		if(gameManager.findGameByUser(sender) != null) {

			Game current = gameManager.findGameByUser(sender);
			Message end = channel.sendMessage(sender.getAsMention()+", vous êtes en pleine partie !").complete();
			current.addGenerateMessages(end.getId());
			current.addGenerateMessages(message.getId());

			return;
		}
		
		QuestionsReader reader = QuizMain.getCore().getQuestionsReader();

		if(args.length > 1) {

			try {
				reader.init("src/net/askigh/quizz/decoder/files/"+args[1]);
				
			} catch (IOException e) {

				channel.sendMessage("Error ! File "+args[1]+" not found.\n"
						+ "Available files : "+reader.getFiles()).queue();
				return;
			}
			
		} else {
			
			try {
				reader.init();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Command has been sent, user able to create a new Game
		Game newGame = gameManager.startNewGame(sender, reader.getQuestions());


		// Waiting message containing join / leave reactions can be sent

		try {
			messageSender.sendWaitingMessage(channel, newGame);
		} catch (IOException e) {
			e.printStackTrace();
		}

		newGame.addGenerateMessages(message.getId());
	}

	@Override
	public String getName() {
		return "?create";
	}

	@Override
	public String getDescription() {
		return "Crée une nouvelle partie, à condition que vous ne soyez pas déja en plein milieu d'une :p";
	}


}
