package net.askigh.quizz.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsReader {

	private Map<String, String> questions = new HashMap<>();
	private static final String DEFAULT = "src/main/resources/questions/default_questions.txt";
	private List<String> files;
	
	public QuestionsReader() {
		
		files = Arrays.asList("default_questions.txt (par défaut)","capitales.txt");
	}
	 
	public void init() throws IOException {
		
		init(DEFAULT);
	}
	
	public void init(String filePath) throws IOException {

		File file = new File(filePath);

		FileReader freader = new FileReader(file);
		BufferedReader reader = new BufferedReader(freader);

		String line;
		String answer = null;
		StringBuilder question = new StringBuilder();

		questions.clear();
		
		while((line=reader.readLine())!= null) {

			if(line.equals("-")) {
				reader.close();
				break;
			}
			
			if(line.startsWith("Difficulté:"))
				continue;
			
			if(!line.isEmpty()){

				if(line.startsWith("Réponse:"))

					answer = line.replace("Réponse: ", "");

				else
					question.append(line).append("\n");
			}

			else {
				// add "number of words" help
				question.append("("+answer.split(" ").length+")");
				//question.deleteCharAt(question.length()-1);
				questions.put(question.toString(), answer);
				question = new StringBuilder();
			}
		}
		System.out.println(questions.size()+" Questions ont bien été chargées !");
		reader.close();

	}

	public Map<String, String> getQuestions() {

		return questions;
	}
	
	public List<String> getFiles() {
		
		return files;
	}

}
