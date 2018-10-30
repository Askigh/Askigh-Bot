package net.askigh.quizz.commands.list;

import net.askigh.quizz.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CodeCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		channel.sendMessage(sender.getAsMention()+", voici le code source de It's Quiz Time ! : "
				+ "https://github.com/Askigh/Askigh-Bot").queue();
	}

	@Override
	public String getName() {
		return "?code";
	}

	@Override
	public String getDescription() {
		return "Obtenez le code source du bot !";
	}

}
