package net.askigh.quizz.utils;

import java.io.IOException;
import java.util.TimerTask;

import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class QuestionTimer extends TimerTask {

	private int seconds = 0;
	private Game current;
	private TextChannel channel;
	
	public QuestionTimer(Game current, TextChannel channel) {
		
		this.current = current;
		this.channel = channel;
		
	}
	
	@Override
	public void run() {
		
		if(current.getState() == GameState.END) {
			cancel();
		}
		
		if(seconds == 15) {
			
			current.getGivenAnswerPlayers().clear();
			
			try {
				QuizMain.getCore().getSender().sendQuestion(current, channel);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			seconds = 0;
			
			cancel();
		}
		
		if(seconds >= 12) {
			
			Message message = channel.getMessageById(current.getMessageID()).complete();
			int left = 15 - seconds;
			message.editMessage("Envoi d'une nouvelle question dans "+left).queue();
		};
	
		seconds++;
	}
	
	public TextChannel getChannel() {
		
		return channel;
	}

}
