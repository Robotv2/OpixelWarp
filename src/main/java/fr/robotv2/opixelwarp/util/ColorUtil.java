package fr.robotv2.opixelwarp.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ColorUtil {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(message));
    }
}
