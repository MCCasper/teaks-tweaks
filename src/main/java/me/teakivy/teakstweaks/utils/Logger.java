package me.teakivy.teakstweaks.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
    public static void log(LogLevel level, String message, boolean toAdmins) {
        if (message == null) return;

        String prefix = "";
        switch (level) {
            case ERROR ->
                    prefix = "&8[&c&lERROR&r&8]";
            case WARNING ->
                    prefix = "&8[&6&lWARNING&r&8]";
            case INFO ->
                    prefix = "&8[&e&lINFO&r&8]";
            case SUCCESS ->
                    prefix = "&8[&a&lSUCCESS&r&8]";
            case OUTLINE ->
                    prefix = "&8[&7&lOUTLINE&r&8]";
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6&lTeaksTweaks&r&8] " + prefix + " &f" + message));

        if (!toAdmins) return;

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("teakstweaks.manage")).forEach(player -> player.sendMessage(ChatColor.YELLOW + message));
    }

    public static void log(LogLevel level, String message) {
        log(level, message, false);
    }

    public enum LogLevel { ERROR, WARNING, INFO, SUCCESS, OUTLINE }

}
