package net.askigh.quizz.commands.list;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class LeaveCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		Game current = gameManager.findGameByUser(sender);

		if(current == null) 			
			channel.sendMessage(sender.getAsMention()+", Vous n'êtes dans aucune partie !").queue();

		else {

			current.removePlayer(sender);
			channel.sendMessage(sender.getAsMention()+", vous avez quitté la partie").queue();

			if(current.getPlayers().size() == 1 && current.getState() == GameState.PLAYING)
				for(User winner : current.getPlayers().keySet())
					current.end(winner, channel);
		}
	}

	@Override
	public String getName() {
		return "?leave";
	}

	@Override
	public String getDescription() {
		return "Quittez votre partie ! Attention, la partie sera considérée comme une défaite";
	}
}
