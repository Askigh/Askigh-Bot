package net.askigh.quizz.commands.list;

import java.util.ArrayList;
import java.util.List;

import net.askigh.quizz.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class SignCommand extends BotCommand {

	private static final int LINE_MAX_LENGTH = 50;
	@Override
	public void execute(User sender, TextChannel channel, Message message) {

		message.delete().queue();
		
		String content = message.getContent().replace(getName()+" ", "");
		
		if(content.length() > 180) {
			
			channel.sendMessage("Ce message est trop long !").queue();
			return;
		}
		
		List<String> lines = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i<content.length();i++) {
			
			builder.append(content.charAt(i));
			
			if(i == 20 || i+1 == content.length()) {
				
				lines.add(builder.toString());
				System.out.println("Add :"+builder.toString());
				builder = new StringBuilder();
			}
		}
		
		System.out.println(lines);
		builder = new StringBuilder();
		builder.append("|￣￣￣￣￣￣￣￣￣￣￣￣|").append("\n");
		
		for(String line : lines) {
			
			int space = (LINE_MAX_LENGTH - line.length()) / 2;
			System.out.println(LINE_MAX_LENGTH);
			builder.append("|");
			
			for(int i = 0; i<space;i++)
				builder.append(" ");
			
			builder.append(line);
			
			for(int i = 0; i<space;i++)
				builder.append(" ");
			
			builder.append("|\n");
			
		}
		
		builder.append("|＿＿＿＿＿＿＿＿＿＿＿＿|\n")
			.append("            (__/)   ||\n")
			.append("            (•ㅅ•) ||\n")
			.append("           /     づ\n");
		
		channel.sendMessage(builder.toString()).queue();
	
	}

	@Override
	public String getName() {
		return "?sign";
	}

	@Override
	public String getDescription() {
		return "send a funny message in a sign";
	}

}
