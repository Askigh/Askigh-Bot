package net.askigh.quizz.commands.list;

import java.awt.Color;
import java.util.Set;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class InfoCommand extends BotCommand {

	@Override
	public void execute(User sender, TextChannel channel, Message message) {
		
		Game current = gameManager.findGameByUser(sender);

		if(current == null) {

			channel.sendMessage(sender.getAsMention()+" vous n'êtes dans aucune partie !").queue();
			return;
		}

		StringBuilder playersByName = new StringBuilder();

		current.getPlayers()
		.keySet()
		.stream()
		.forEach(x -> playersByName.append(x.getName()).append(", "));

		StringBuilder needResponsePlayersByName = new StringBuilder();

		Set<User> allPlayers = current.getPlayers().keySet();

		current.getGivenAnswerPlayers().forEach(x -> allPlayers.remove(x));

		if(allPlayers.isEmpty() || current.getState() != GameState.PLAYING)
			needResponsePlayersByName.append("personne en particulier :D");

		else {

			allPlayers.forEach(x -> needResponsePlayersByName.append(x.getName()).append(", "));
		}

		channel.sendMessage(createEmbed(current.getOwner().getName(), playersByName, needResponsePlayersByName).build()).queue();
	}
	
	private EmbedBuilder createEmbed(String owner, StringBuilder players, StringBuilder needResponsePlayers) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.RED);
		builder.setDescription("-------");
		builder.setTitle("Informations");
		builder.addField("Owner", owner, false);
		builder.addField("Joueurs", players.toString(), false);
		builder.addField("En attente de la réponse de", needResponsePlayers.toString(), false);
		
		return builder;
	}

	@Override
	public String getName() {
		return "?info";
	}

	@Override
	public String getDescription() {
		return "Obtenez des informations concernant votre partie !";
	}

}
