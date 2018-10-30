package net.askigh.quizz.commands.list;

import java.awt.Color;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class FilesCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Available Files");
		builder.setColor(Color.RED);
		builder.addBlankField(false);
		
		for(String file : QuizMain.getCore().getQuestionsReader().getFiles()) {
			
			builder.addField(file, "---", false);
		}
		
		builder.addBlankField(false);
		builder.addField("Command requested by", sender.getAsMention(), false);
		channel.sendMessage(builder.build()).queue();
		
	}

	@Override
	public String getName() {
		return "?files";
	}

	@Override
	public String getDescription() {
		return "Obtenez la liste des fichiers de questions disponibles !";
	}

}
