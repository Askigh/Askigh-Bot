package net.askigh.quizz.commands.list;

import java.awt.Color;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class PingCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.WHITE);
		builder.setTitle("Ping");
		builder.setDescription(QuizMain.getJDAInstance().getPing()+"ms");
		builder.addField("Information requested by", sender.getAsMention(), false);
		
		channel.sendMessage(builder.build()).queue();
	}

	@Override
	public String getName() {
		return "?ping";
	}

	@Override
	public String getDescription() {
		return "Un coup de lag ? C'est peut être la faute du bot, checkez son ping !";
	}

}
