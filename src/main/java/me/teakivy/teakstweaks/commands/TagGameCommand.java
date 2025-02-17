package me.teakivy.teakstweaks.commands;

import me.teakivy.teakstweaks.Main;
import me.teakivy.teakstweaks.utils.AbstractCommand;
import me.teakivy.teakstweaks.utils.ErrorType;
import me.teakivy.teakstweaks.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TagGameCommand extends AbstractCommand {

    Main main = Main.getPlugin(Main.class);

    public TagGameCommand() {
        super("tag", MessageHandler.getCmdName("taggame"), MessageHandler.getCmdUsage("taggame"), MessageHandler.getCmdDescription("taggame"), MessageHandler.getCmdAliases("taggame"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ErrorType.MISSING_COMMAND_PERMISSION.m());
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ErrorType.NOT_PLAYER.m());
                return true;
            }
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("uninstall")) {
                if (!sender.hasPermission(permission+".uninstall")) {
                    sender.sendMessage(ErrorType.MISSING_COMMAND_PERMISSION.m());
                    return true;
                }
                player.sendMessage(MessageHandler.getCmdMessage("taggame", "uninstalled"));
                main.getRegister().unregisterPack("tag");
                return true;
            }
        }

        if (!main.getConfig().getBoolean("packs.tag.enabled")) {
            sender.sendMessage(ErrorType.PACK_NOT_ENABLED.m());
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ErrorType.NOT_PLAYER.m());
            return true;
        }
        Player player = (Player) sender;


        if (args.length < 1) {
            if (!sender.hasPermission(permission+".give")) {
                sender.sendMessage(ErrorType.MISSING_COMMAND_PERMISSION.m());
                return true;
            }
            main.getRegister().unregisterPack("tag");
            ItemStack tag = new ItemStack(Material.NAME_TAG);
            ItemMeta tagMeta = tag.getItemMeta();
            tagMeta.setDisplayName(ChatColor.YELLOW + "Tag!");
            tagMeta.setUnbreakable(true);
            tag.setItemMeta(tagMeta);

            player.getInventory().addItem(tag);

            player.addScoreboardTag("tag_it");

            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
            if (sb.getTeam("TaggedTeam") == null) {
                Team taggedTeam = sb.registerNewTeam("TaggedTeam");
                taggedTeam.setColor(ChatColor.RED);
            }
            Team taggedTeam = sb.getTeam("TaggedTeam");
            taggedTeam.addEntry(player.getName());
            player.sendMessage(MessageHandler.getCmdMessage("taggame", "begun"));
            return true;
        }
        return false;
    }

    List<String> arguments1 = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (arguments1.isEmpty() && sender.isOp()) {
            arguments1.add("uninstall");
        }

        List<String> result = new ArrayList<String>();
        if (args.length == 1) {
            for (String a : arguments1) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }
}
