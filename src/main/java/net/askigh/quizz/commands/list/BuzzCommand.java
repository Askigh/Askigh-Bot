package net.askigh.quizz.commands.list;

import java.io.IOException;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class BuzzCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		Game current = QuizMain.getCore().getManager().findGameByUser(sender);

		if(current != null && current.getState() == GameState.PLAYING) {
			
			current.setTestAFK(0);

			if(current.getGivenAnswerPlayers().contains(sender)) {

				Message end = channel.sendMessage(sender.getAsMention()+" vous avez déjà répondu !").complete();
				current.addGenerateMessages(end.getId());
				return;
			}

			if(current.isAnswerInQueue()) {
				message.delete().queue();
				return;
			}
			
			current.getGivenAnswerPlayers().add(sender);

			if(message.getRawContent().replace("?buzz ", "").equalsIgnoreCase(current.getCurrentAnswer())) {
				
				current.getTimer().cancel();

				current.getPlayers().put(sender, current.getPlayers().get(sender) + 1);
				channel.sendMessage(sender.getAsMention()+" remporte la manche !").queue();
				current.getGivenAnswerPlayers().clear();

				if(current.getPlayers().get(sender) >= current.getQuestionWinAmount()) {
					current.end(sender, channel);
					return;
				}

				try {
					messageSender.sendQuestion(current, channel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else  {
				
				channel.getMessageById(current.getMessageID())
					.complete()
					.editMessage(sender.getAsMention()+", mauvaise réponse !").queue();
				
				// If nobody gets the right answer
				if(current.getGivenAnswerPlayers().size() >= current.getPlayers().size()) {
					
					current.getTimer().cancel();

					try {
						messageSender.sendQuestion(current, channel);
					} catch (IOException e) {
						e.printStackTrace();
					}
					current.getGivenAnswerPlayers().clear();
					
				}
			}
		}	
		message.delete().queue();

	}

	@Override
	public String getName() {
		return "?buzz";
	}

	@Override
	public String getDescription() {
		return "Donnez votre réponse ! Attention vous n'avez qu'un essai !";
	}


}
