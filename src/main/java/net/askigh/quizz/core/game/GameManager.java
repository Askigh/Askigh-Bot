package net.askigh.quizz.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.core.entities.User;

public class GameManager {

	private List<Game> games;

	public GameManager() {

		this.games = new ArrayList<>();
	}

	public Game findGameByUser(User player) {

		for(Game game : games) {

			if(game.getPlayers().containsKey(player))
				return game;
		}

		return null;

	}

	public Game findGameByID(String id) {

		for(Game game : games) {

			if(game.getMessageID().equals(id))
				return game;
		}

		return null;

	}

	public enum GameState {

		WAITING, CHOOSING_POINTS_TO_WIN, CHOOSING_DIFFICULTY, PLAYING, END, UNDEFINED
	}

	public Game startNewGame(User owner, Map<String, String> questions) {

		Game game = new Game(questions);
		game.setOwner(owner);

		games.add(game);

		return game;
	}

}
