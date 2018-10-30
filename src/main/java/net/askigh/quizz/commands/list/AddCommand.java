package net.askigh.quizz.commands.list;

import net.askigh.quizz.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class AddCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		channel.sendMessage("Vous voulez m'ajouter à votre serveur ? "
				+ "Voici le lien pour ajouter It's Quizz Time ! :\n"
				+ "https://discordapp.com/api/oauth2/authorize?client_id=387335088196681739&permissions=1073817664&scope=bot")
			.queue();
	}

	@Override
	public String getName() {
		return "?add";
	}

	@Override
	public String getDescription() {
		return "Vous permet d'ajouter \"It's Quizz Time !\" à votre serveur";
	}
	

}
