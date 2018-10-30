package net.askigh.quizz.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class MessageSender {

	private BufferedImage image;
	private Graphics g;
	private FontMetrics metrics;

	public void sendWaitingMessage(TextChannel channel, Game game) throws IOException {

		String text = new String("En attente de joueurs...");
		drawString(text, 140, 350);

		Message endMessage = channel.sendFile(toInputStream(image), "res/picture.png", null).complete();
		String id = endMessage.getId();
		endMessage.addReaction("▶").complete();
		endMessage.addReaction("🛑").complete();

		game.addGenerateMessages(id);
		game.setMessageID(id);		

	}

	public void sendChoosingModeMessage(TextChannel channel, Game game) throws IOException {

		game.deleteMessages(channel);

		String text = new String("Choisissez votre\nmode de jeu !");
		drawString(text, 140, 220);

		Message chooseMode = channel.sendFile(toInputStream(image), "res/picture.png", null).complete();

		chooseMode.addReaction("☝").complete();
		chooseMode.addReaction("✌").complete();
		chooseMode.addReaction("🖐").complete();

		game.setMessageID(chooseMode.getId());
		game.addGenerateMessages(chooseMode.getId());

	}

	public void sendChooseDifficultyMessage(Game current, TextChannel channel) throws IOException {

		current.deleteMessages(channel);

		String text = new String("Avec quelle difficulté\nsouhaitez-vous jouer ?");

		drawString(text, 140, 220);

		Message endMessage = channel.sendFile(toInputStream(image), "picture.png", null).complete();
		endMessage.editMessage("Info : Nombre de questions insuffisant, le choix de la difficulté ne sera pas pris en compte").queue();
		endMessage.addReaction("😅").queue();
		endMessage.addReaction("😰").queue();
		endMessage.addReaction("😡").queue();

		current.setMessageID(endMessage.getId());
		current.addGenerateMessages(endMessage.getId());

		current.setState(GameState.CHOOSING_DIFFICULTY);

	}

	public void sendQuestion(Game current, TextChannel channel) throws IOException {

		Map<String, String> questions = current.getGameQuestions();
		
		Timer timer = new Timer();
		QuestionTimer task = (QuestionTimer) current.createTimer(current, channel);
		
		if(task == null) return;
		
		timer.schedule(task, 0, 2000);
		
		boolean found = false;

		current.setAnswerInQueue(true);

		new Thread(() -> {

			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			current.setAnswerInQueue(false);
		}).start();

		while(!found) {
			
			int random = new Random().nextInt(questions.size());
			int pointer = 0;

			for(String question : questions.keySet()) {

				if(pointer == random) {

					if(current.getQuestionsAlreadyDone().contains(question)) 
						break;
					
					else {
						
						found = true;
						current.addQuestionsAlreadyDone(question);
					}
					
					String answer = questions.get(question);
					current.deleteMessages(channel);

					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setTitle("Here is the question");

					builder.addField("Question", question, false);

					drawString(question, 80, 250);

					//Message sent = channel.sendMessage(builder.build()).completeAfter(1500, TimeUnit.MILLISECONDS);
					channel.sendTyping().queue();
					Message sent = channel.sendFile(toInputStream(image), "res/picture.png", null).completeAfter(1000, TimeUnit.MILLISECONDS);

					current.setMessageID(sent.getId());
					current.addGenerateMessages(sent.getId());

					current.setCurrentAnswer(answer);
					break;
				}

				pointer++;
			}
		}

	}

	private void drawString(String text, int size, int yBase) {

		init(size);

		float y = yBase;

		for(String current : text.split("\n")) {

			y+= size * 1.4;
			int x = (image.getWidth() - metrics.stringWidth(current)) / 2;
			g.drawString(current, x, (int) y);

		}
	}

	private InputStream toInputStream(BufferedImage image) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "png", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());

		return is;
	}
	/**
	 * @param size : The Size of the string
	 * Do not call drawString() before a call of this method
	 * Do not call again init between the first call and the drawString() method otherwise the size will be changed
	 * TODO : Fix font bug, the writting style is not working
	 */
	private void init(int size) {

		try {
			this.image = ImageIO.read(new File("res/picture.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g = image.getGraphics();
		g.setColor(new Color(0.9f, 0.4f, 0f));

		Font font = new Font("Times New Roman", Font.PLAIN, size);

		metrics = g.getFontMetrics(font);
		g.setFont(font);
	}
	
	// static method that could be useful while choosing a writting style
	
	public static void printWrittingStyles() {

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		Font[] allFonts = ge.getAllFonts();

		for (Font font : allFonts) {
			System.out.println(font.getFontName(Locale.US));
		}
	}
}
