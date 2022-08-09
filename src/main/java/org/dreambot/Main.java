package org.dreambot;

import org.dreambot.api.methods.emotes.Emote;
import org.dreambot.api.methods.emotes.Emotes;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.Player;

public class Main extends AbstractScript {
    GlobalState state = GlobalState.getGlobalState();

    @Override
    public void onStart(String... params) {
        Keyboard.setWordsPerMinute(200);
    }

    @Override
    public int onLoop() {
        if (state.isSendMsg()) {
            Keyboard.type(state.getMessage(), true);
            state.setSendMsg(false);
            return 600;
        }

        if (state.isDance()) {
            Emotes.doEmote(Emote.DANCE);
            state.setDance(false);
            return 600;
        }

        if (state.isTravel() && Walking.shouldWalk()) {
            Tile tile = new Tile(state.getDestX(), state.getDestY());
            Walking.walkExact(tile);
            if (Players.localPlayer().getTile().equals(tile)) {
                state.setTravel(false);
            }
            return 600;
        }

        if (state.isFollow()) {
            Player admin = Players.closest(state.getAdminUser());
            if (admin != null && Walking.shouldWalk()) {
                Walking.walk(admin.getSurroundingArea(4).getRandomTile());
                return 600;
            }
        }
        return 600;
    }
}
