package net.askigh.quizz.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.User;

public class ConfigDecoder {

	private static final File CONFIG = new File("src/main/resources/decoder/config.txt");
	private List<String> configLines = new ArrayList<>();

	public int readScoreOf(User target) throws IOException {

		return readOf(target, ReadMode.GET_SCORE);
	}

	public int readGameCountOf(User target) throws IOException {

		return readOf(target, ReadMode.GET_GAMES);
	}

	public void addScoreOf(User target) throws IOException {

		readOf(target, ReadMode.ADD_SCORE);

	}

	public void addGameCountOf(User target) throws IOException {

		readOf(target, ReadMode.ADD_GAME);
	}

	private int readOf(User target, ReadMode mode) throws IOException {

		FileReader stream = new FileReader(CONFIG);
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(stream);

		String line;
		String newLine = null;
		int currentLine = 0;
		int lineNumber = 0;

		while((line = reader.readLine()) != null) {

			configLines.add(line);

			if(line.contains(target.getId())) {

				lineNumber = currentLine;
								
				String[] contents = line.split(" / ");

				if(mode == ReadMode.GET_SCORE) {

					configLines.clear();
					return Integer.parseInt(contents[2]);
				}

				if(mode == ReadMode.ADD_SCORE) {

					int points = Integer.parseInt(contents[2]) + 1;
					newLine = target.getName()+" / "+target.getId()+" / "+points+" / "+contents[3];

				}

				if(mode == ReadMode.ADD_GAME) {

					int games = Integer.parseInt(contents[3]) + 1;
					newLine = target.getName()+" / "+target.getId()+" / "+contents[2]+" / "+games;
				}

				if(mode == ReadMode.GET_GAMES) {
					
					configLines.clear();
					return Integer.parseInt(contents[3]);
				}
			}
			currentLine++;
		}
		

		if(newLine == null)
			newLine = target.getName() + " / "+target.getId()+" / 0 / 0";

		

		write(newLine, lineNumber);

		lineNumber = 0;
		currentLine = 0;
		newLine = null;
		configLines.clear();
		
		return 0;

	}

	private void write(String newLine, int lineNumber) throws IOException {

		if(lineNumber == 0) {

			configLines.add(newLine);
			Files.write(CONFIG.toPath(), configLines);
		}

		else {

			configLines.set(lineNumber, newLine);			
			Files.write(CONFIG.toPath(), configLines);

		}	
	}

	private enum ReadMode {

		GET_SCORE, ADD_SCORE, GET_GAMES, ADD_GAME
	}

}
