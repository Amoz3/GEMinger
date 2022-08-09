package org.dreambot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.dreambot.api.methods.MethodProvider;
import org.jetbrains.annotations.NotNull;

public class DiscordCommandListener extends ListenerAdapter {
    GlobalState state = GlobalState.getGlobalState();
    JDA discordBot = null;

    public DiscordCommandListener(JDA bot) {
        discordBot = bot;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        MethodProvider.log("Command received " + event.getMessage().getContentRaw());
        String[] command = event.getMessage().getContentRaw().split(" ");
        switch (command[0]) {
            case "follow":
                state.setFollow(!state.isFollow());
                state.setAdminUser(event.getMessage().getContentRaw().replace("follow ", ""));
                break;
            case "travel":
                if (command.length >= 3) {
                    state.setTravel(true);
                    state.setDestX(Integer.parseInt(command[1]));
                    state.setDestY(Integer.parseInt(command[2]));
                }
                break;
            case "dance":
                state.setDance(true);
                break;
            case "say":
                state.setMessage(event.getMessage().getContentRaw().replace("say ", ""));
                state.setSendMsg(true);
                break;
            case "marco":
                DiscordUtils.sendUpdate(discordBot, "general");
                break;
            case "stopTravel":
                state.setTravel(false);
                break;
        }
    }
}
