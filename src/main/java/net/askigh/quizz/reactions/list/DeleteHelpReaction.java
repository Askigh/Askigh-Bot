package net.askigh.quizz.reactions.list;

import java.util.List;

import net.askigh.quizz.core.QuizMain;
import net.askigh.quizz.core.game.Game;
import net.askigh.quizz.core.game.GameManager.GameState;
import net.askigh.quizz.reactions.BotReaction;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class DeleteHelpReaction extends BotReaction {

    public DeleteHelpReaction(String emote, GameState required) {
        super(emote, required);
    }

    @Override
    public void execute(MessageReactionAddEvent event, Game current, Message message, TextChannel channel, User user) {

        if (!message.getEmbeds().isEmpty()) {

            List<Field> fields = message.getEmbeds().get(0).getFields();

            if (QuizMain.getCore().isHelpMessage(message.getId())) {
                for (Field f : fields) {
                    if (f.getValue().contains(user.getAsMention())) {
                        message.delete().queue();
                    }
                }
            }
        }
    }

}
