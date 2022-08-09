package org.dreambot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.dreambot.api.Client;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DiscordUtils {
    private static final String BOT_TOKEN = "";
    static GlobalState state = GlobalState.getGlobalState();

    /**
     *  finds and then requests focus on dreambot window
     */
    private static void focusDreambot() {
        Frame frame = null;
        for (Frame f : Frame.getFrames()) {
            if (f.getTitle().toLowerCase().contains("dreambot")) {
                frame = f;
            }
        }
        if (frame != null) {
            MethodProvider.log(Color.CYAN, "Requesting focus...");
            frame.requestFocus();
            frame.transferFocus();
            frame.toFront();
        }
    }

    /**
     *
     * @return an instance of JDA discord bot
     */
    public static JDA initDiscordBot() {
        try {
            MethodProvider.log(Color.CYAN, "Init discord bot...");
            return JDABuilder.createDefault(BOT_TOKEN).build();
        } catch (LoginException e) {
            MethodProvider.logError("DISCORD BOT LOGIN FAILURE");
        }
        return null;
    }

    public static void sendUpdate(JDA bot, String channel) {
        List<TextChannel> channels = bot.getTextChannelsByName(channel, true);
        for (TextChannel ch : channels) {
            ch.sendMessage(getUpdateMessage()).queue();
        }
    }

    /**
     * this one is for sending periodic screenshots, it does not autofocus the window
     * @param bot the bot used to take the screenshot
     * @param channel the channel to send it to
     */
    public static void sendScreenShot(JDA bot, String channel) {
        takeScreenshot();

        File scFile = new File(System.getProperty("scripts.path") + "/osDruids/"
                + "proggieSc" + Client.getPlayerHash().replaceAll("/", "") + ".png");

        if (!scFile.exists()) {
            MethodProvider.log(Color.CYAN, "screenshot file not found.");
        }
        List<TextChannel> channels = bot.getTextChannelsByName(channel, true);
        for (TextChannel ch : channels) {
            ch.sendFile(scFile).append(getUpdateMessage()).queue();
        }
        MethodProvider.log(Color.CYAN, "Sent screenshot");
    }

    /**
     * this one is for sending periodic screenshots, it does autofocus the window
     * @param bot the bot used to take the screenshot
     * @param channel the channel to send it to
     */
    public static void focusAndSendScreenShot(JDA bot, String channel) {
        focusDreambot();
        MethodProvider.sleep(500);
        takeScreenshot();

        File scFile = new File(System.getProperty("scripts.path") + "/osDruids/"
                + "proggieSc" + Client.getPlayerHash().replaceAll("/", "") + ".png");

        if (!scFile.exists()) {
            MethodProvider.log(Color.CYAN, "screenshot file not found.");
        }
        List<TextChannel> channels = bot.getTextChannelsByName(channel, true);
        for (TextChannel ch : channels) {
            ch.sendFile(scFile).append(getUpdateMessage()).queue();
        }
        MethodProvider.log(Color.CYAN, "Sent screenshot");
    }

    /**
     *
     * @return the message all the methods will send with screenshots or on update
     */
    public static String getUpdateMessage() {
        return "```"
                + "\nIsTravel: " + state.isTravel()
                + "\nIsFollow: " + state.isFollow()
                + "\nIsSendMsg: " + state.isSendMsg()
                + "\nIsDance: " + state.isDance()
                + "\nAdmin: " + state.getAdminUser()
                + "\nTile: " + state.getDestX() + ", " + state.getDestY()
                + "```";
    }

    /**
     * takes screnshot and saves it to scripts/osDruids/[player hash].png
     */
    private static void takeScreenshot() {
        Frame frame = null;
        for (Frame f : Frame.getFrames()) {
            if (f.getTitle().toLowerCase().contains("dreambot")) {
                MethodProvider.log(Color.CYAN, "Found dreambot frame");
                frame = f;
            }
        }
        if (frame == null) {
            MethodProvider.logError("Couldn't find dreambot frame");
        }

        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        Rectangle wholeScreen = new Rectangle(5000, 5000);
        BufferedImage wholeScreenShot = robot.createScreenCapture(wholeScreen);
        BufferedImage croppedScreenShot = wholeScreenShot.getSubimage(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        File scFile = new File(System.getProperty("scripts.path") + "/osDruids/"
                + "proggieSc" + Client.getPlayerHash().replaceAll("/", "") + ".png");
        try {
            scFile.getParentFile().mkdirs();
            scFile.createNewFile();
            ImageIO.write(croppedScreenShot, "jpg", scFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
