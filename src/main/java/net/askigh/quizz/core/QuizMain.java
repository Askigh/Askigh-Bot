package net.askigh.quizz.core;

import javax.security.auth.login.LoginException;

import net.askigh.quizz.commands.BotCommand;
import net.askigh.quizz.events.ReactionsListener;
import net.askigh.quizz.events.MessageReceivedListener;
import net.askigh.quizz.reactions.BotReaction;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import java.io.*;

public class QuizMain {

    private static JDA jda;
    private static BotCore core;

    public static void main(String[] args) throws LoginException, IOException, RateLimitedException {
        new QuizMain();
    }
    private QuizMain() throws IOException, LoginException, RateLimitedException {
        // All the contents are loaded in this class to avoid multiple static variables in QuizMain class
        core = new BotCore();
        BufferedReader reader = new BufferedReader(new FileReader(new File(
                "src/main/resources/token.txt")));
        String token = reader.readLine();
        reader.close();

        jda = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .buildAsync();

        jda.addEventListener(new MessageReceivedListener(), new ReactionsListener());
        jda.getPresence().setGame(Game.playing("say ?help :D"));
    }

    public static JDA getJDAInstance() {
        return jda;
    }
    public static BotCore getCore() {
        return core;
    }
}
