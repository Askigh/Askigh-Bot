package net.askigh.quizz.commands.list;

import java.awt.Color;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class HelpCommand extends BotCommand {
	
	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.MAGENTA);
		builder.setTitle("Welcome to the help interface !");
		builder.setDescription("Here is the list of available commands, and explanations about how you can play");
		builder.addField("Comment jouer ?", "Vous trouverez plus bas la liste des commandes existantes, "
				+ "mais comment utiliser \"It's Quiz Time !\" ?\n"
				+ "\nUne fois une partie créée, vous n'avez qu'à attendre d'autres joueurs puis commencer quand vous le souhaitez. "
				+ "Pour rejoindre une partie existante, appuyez sur la réaction ▶. Pour quitter la partie, réagissez 🛑."
				+ "Ensuite, il vous faudra choisir combien de points sont nécéssaires pour remporter la partie, et quelle "
				+ "difficulté sera appliquée. Finalement, des questions apparaissent jusqu'à ce qu'un joueur obtienne "
				+ "assez de points pour gagner. Le nombre entre parenthèses à la fin de la question vous aide, il vous indique "
				+ "le nombre de mots que contient la réponse.", false);
		builder.addBlankField(false);
		
		for(BotCommand command : QuizMain.getCore().getCommands()) {
			
			builder.addField(command.getName(), command.getDescription(), false);
			builder.addBlankField(false);
		}
		
		builder.addField("Help requested by", sender.getAsMention(), false);
		builder.addBlankField(false);
		builder.addField("Nombre total de questions", ""+QuizMain.getCore().getQuestions().size(), false);
	

		Message help = channel.sendMessage(builder.build()).complete();
		help.addReaction("👁").queue();

		message.delete().queue();
		QuizMain.getCore().addAsHelpMessage(help.getId());
	}

	@Override
	public String getName() {
		return "?help";
	}

	@Override
	public String getDescription() {
		return "Obtenez de l'aide !";
	}


}
