package net.askigh.quizz.core.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.utils.QuestionTimer;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;

public class Game {

	private GameState state;
	private QuestionTimer timer;

	private int questionWinAmount;
	private int difficulty;

	private User owner;
	private Map<User, Integer> players;
	private List<User> givenAnswerUsers;
	private List<String> questionAlreadyDone;
	private Set<String> generatedMessages;

	private Map<String, String> gameQuestions;
	private String messageID;
	private String currentAnswer;

	private boolean needOwner = false;
	private boolean isAnswerInQueue = false;
	private int testAFK = 0;

	public Game(Map<String, String> questions) {

		this.players = new LinkedHashMap<>();
		this.givenAnswerUsers = new ArrayList<>();
		this.questionAlreadyDone = new ArrayList<>();
		this.gameQuestions = questions;
		this.timer = new QuestionTimer(this, null);
		
		generatedMessages = new HashSet<>();
		setState(GameState.WAITING);
	}

	public Map<User, Integer> getPlayers() {
		return players;
	}

	public void addPlayer(User player) { players.put(player, 0); }

	public void removePlayer(User user) {

		if(owner.equals(user)) {
			if(players.size() > 1) {
				
				for(User target : players.keySet()) {
					this.setOwner(target);
					break;
				}
			}
			else setNeedOwner(true);
		}
		players.remove(user);
	}

	public void deleteMessages(TextChannel channel) {

		for(String id : generatedMessages) {

			try {
				Message m = channel.getMessageById(id).complete();
				m.delete().complete();
			} catch(ErrorResponseException e){
				System.out.println("An ErrorResponseException has occurred");
			}

		}
		generatedMessages.clear();
	}

	public void addGenerateMessages(String m) { generatedMessages.add(m); }

	public void setOwner(User owner) {
		this.owner = owner;
		players.put(owner, 0);
		needOwner = false;
	}

	public void end(User sender, TextChannel channel) {
		
		this.deleteMessages(channel);
		channel.sendMessage(sender.getAsMention()+" remporte la partie !\n"
				+ "Game information :\nDifficulté : "+difficulty
				+"\nGame mode : "+questionWinAmount+" question(s) gagnante(s)").queue();
		clear();

		try {
			QuizMain.getCore().getConfigDecoder().readScoreOf(sender);
			QuizMain.getCore().getConfigDecoder().addScoreOf(sender);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void clear() {
		setState(GameState.END);
		// We absolutely need to clear the player list, otherwise they won't be allowed to play other games
		players.clear();
	}

	public User getOwner() { return owner; }
	
	public List<User> getGivenAnswerPlayers() { return givenAnswerUsers; }

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public Set<String> getGeneratedMessages() {
		return generatedMessages;
	}
	public boolean needOwner() {
		return needOwner;
	}

	public void setNeedOwner(boolean needOwner) {
		this.needOwner = needOwner;
	}

	public int getQuestionWinAmount() {
		return questionWinAmount;
	}

	public void setQuestionWinAmount(int questionWinAmount) {
		this.questionWinAmount = questionWinAmount;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getCurrentAnswer() {
		return currentAnswer;
	}

	public void setCurrentAnswer(String currentAnswer) {
		this.currentAnswer = currentAnswer;
	}

	public boolean isAnswerInQueue() {
		return isAnswerInQueue;
	}

	public void setAnswerInQueue(boolean isAnswerInQueue) {
		this.isAnswerInQueue = isAnswerInQueue;
	}

	public List<String> getQuestionsAlreadyDone() {
		return questionAlreadyDone;
	}

	public void addQuestionsAlreadyDone(String question) {
		this.questionAlreadyDone.add(question);
	}

	public QuestionTimer getTimer() {
		return timer;
	}

	public TimerTask createTimer(Game current, TextChannel channel) {
		
		testAFK++;
		if(testAFK >= 4) {
			channel.sendMessage("Game stopped, all players seem afk...").queue();
			clear();
			return null;
		}
		this.timer = new QuestionTimer(current, channel);
		return timer;
	}

	public void setTestAFK(int testAFK) { this.testAFK = testAFK; }
	public Map<String, String> getGameQuestions() {
		return gameQuestions;
	}
}
