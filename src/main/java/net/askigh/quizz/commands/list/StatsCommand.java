package net.askigh.quizz.commands.list;

import java.awt.Color;
import java.io.IOException;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.decoder.ConfigDecoder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class StatsCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		String[] args = message.getRawContent().split(" ");
		
		if(args.length == 1)
			sendStatsOf(sender, channel);
		
		else {
			
			User target = QuizMain.getJDAInstance().getUserById(args[1]
					.replace("<", "")
					.replace(">", "")
					.replace("@", ""));
			
			if(target == null) {
				channel.sendMessage("Impossible de trouver ce joueur...").queue();
				return;
			}
			
			sendStatsOf(target, channel);
		}
		
	}
	
	private void sendStatsOf(User target, TextChannel channel) {
		
		ConfigDecoder config = QuizMain.getCore().getConfigDecoder();

		try {
			float score = config.readScoreOf(target);
			float gameCount = config.readGameCountOf(target);
			
			String ratio = ""+ score / (gameCount - score);
			ratio = ratio.length() <= 3 ? ratio : ratio.substring(0, 4);
		
			if(gameCount - score == 0)
				ratio = ""+score;
			
			if(gameCount == 0)
				ratio = "Aucune partie répertoriée...";
			
			channel.sendMessage(createStats(score, gameCount, ratio, target).build()).queue();

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private EmbedBuilder createStats(float score, float gameCount, String ratio, User sender) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.ORANGE);
		builder.setTitle("-|| Statistiques ||-");
		builder.addBlankField(false);
		builder.addField("Victoires : ", ""+(int)score, false);
		builder.addField("Parties jouées : ", ""+(int)gameCount, false);
		builder.addField("Ratio (Win / Défaites) : ", ratio, false);
		builder.addBlankField(false);
		builder.addField("Statistiques de", sender.getAsMention(), false);
		
		return builder;
	}

	@Override
	public String getName() {
		return "?stats";
	}

	@Override
	public String getDescription() {
		return "Affiche vos statistiques personnelles";
	}
}
