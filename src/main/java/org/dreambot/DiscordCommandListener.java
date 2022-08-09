package org.dreambot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordCommandListener extends ListenerAdapter {
    GlobalState state = GlobalState.getGlobalState();
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");
        switch (command[0]) {
            case "follow":
                state.setFollow(!state.isFollow());
                if (command.length >= 2 && command[1] != null && !command[1].equals("")) {
                    state.setAdminUser(command[1]);
                }
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
        }
    }
}
