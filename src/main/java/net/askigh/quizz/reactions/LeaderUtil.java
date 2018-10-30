package net.askigh.quizz.reactions;

import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class LeaderUtil {

	public static boolean hasLead(Game current, User target, TextChannel channel) {
		
		Game game = QuizMain.getCore().getManager().findGameByUser(target);

		if(game == null)
			return false;
		
		if(!game.equals(current)) 		
			return false;

		if(!game.getOwner().equals(target)) {
			channel.getMessageById(current.getMessageID()).complete()
				.editMessage(target.getAsMention()+" impossible de faire cela...");
			return false;
		}
		
		return true;
	}
}
