package org.dreambot;

import net.dv8tion.jda.api.JDA;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.emotes.Emote;
import org.dreambot.api.methods.emotes.Emotes;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.Player;

@ScriptManifest(category = Category.MISC, name = "Trans", author = "", version = 0.0)
public class Main extends AbstractScript {
    GlobalState state = GlobalState.getGlobalState();
    JDA discord;
    @Override
    public void onStart(String... params) {
        discord = DiscordUtils.initDiscordBot();
        sleep(5000);
        if (discord != null) {
            discord.addEventListener(new DiscordCommandListener(discord));
        }
        Keyboard.setWordsPerMinute(200);
    }

    @Override
    public void onStart() {
        discord = DiscordUtils.initDiscordBot();
        sleep(5000);
        if (discord != null) {
            discord.addEventListener(new DiscordCommandListener(discord));
        }
        Keyboard.setWordsPerMinute(200);
        Walking.setRunThreshold(30);
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
            log("Traveling");
            Tile tile = new Tile(state.getDestX(), state.getDestY());
            log(tile.toString());
            Walking.walk(tile);
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

    @Override
    public void onExit() {
        discord.shutdownNow();
    }
}
