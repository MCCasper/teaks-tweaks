package me.teakivy.vanillatweaks;

import me.teakivy.vanillatweaks.CraftingTweaks.CraftingRegister;
import me.teakivy.vanillatweaks.Events.UpdateJoinAlert;
import me.teakivy.vanillatweaks.Packs.CoordsHud.DisplayHud;
import me.teakivy.vanillatweaks.Packs.Tag.Tag;
import me.teakivy.vanillatweaks.Utils.ConfigUpdater.ConfigUpdater;
import me.teakivy.vanillatweaks.Utils.DataManager.DataManager;
import me.teakivy.vanillatweaks.Utils.Logger.Logger;
import me.teakivy.vanillatweaks.Utils.Metrics;
import me.teakivy.vanillatweaks.Utils.Register.Register;
import me.teakivy.vanillatweaks.Utils.UpdateChecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin implements Listener {

    public static ArrayList<UUID> chEnabled = new ArrayList<>();
    public Boolean newVersionAvaliable = false;
    public String latestVTVersion;

    public DataManager data;


    public Tag tagListener;

    @Override
    public void onEnable() {

        // Metrics

        Metrics metrics = new Metrics(this, 12001);

        // Update Config.yml
        if (this.getConfig().getInt("config.version") < this.getConfig().getDefaults().getInt("config.version")) {
            try {
                ConfigUpdater.update(this, "config.yml", new File(this.getDataFolder(), "config.yml"), Collections.emptyList());
                this.reloadConfig();
                System.out.println("[VT] Updated Config to Version: " + this.getConfig().getInt("config.version"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Update Checker
        getServer().getPluginManager().registerEvents(new UpdateJoinAlert(), this);

        String latestVersion = null;
        try {
            latestVersion = new UpdateChecker(this, 94021).getLatestVersion();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            Logger.log(Logger.LogLevel.ERROR, ChatColor.RED + "Could not get latest version!");
        }
        String thisVersion = this.getDescription().getVersion();
        if (!thisVersion.equalsIgnoreCase(latestVersion)) {
            Logger.log(Logger.LogLevel.WARNING, "[VT] Vanilla Tweaks has an update!\nPlease update to the latest version (" + latestVersion + ")\n" + ChatColor.YELLOW + "https://www.spigotmc.org/resources/vanilla-tweaks.94021/");
            newVersionAvaliable = true;
            latestVTVersion = latestVersion;
        }


        // Crafting Tweaks
        CraftingRegister.register();

        // Data Manager
        this.data = new DataManager(this);
        data.saveDefaultConfig();

        // Commands
        Register.registerCommands();

        // Config
        this.saveDefaultConfig();

        boolean displayedFirstSpace = false;

        for (String pack : this.getConfig().getConfigurationSection("packs").getKeys(false)) {
            if (this.getConfig().getBoolean("packs." + pack + ".enabled")) {
                if (!displayedFirstSpace) System.out.println("");
                displayedFirstSpace = true;

                System.out.println("[VT] " + getPackName(pack) + " Enabled!");
            }
        }
        if (displayedFirstSpace) System.out.println("");

        tagListener = new Tag();

        // Packs

        Register.registerAll();






        // Coords HUD
        for (String uuid : data.getConfig().getStringList("chEnabled")) {
            chEnabled.add(UUID.fromString(uuid));
        }

        getServer().getScheduler().runTaskTimer(this, () -> {
            if (this.getConfig().getBoolean("packs.coords-hud.enabled")) {
                for (UUID uuid : chEnabled) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    if (player.isOnline())
                        DisplayHud.showHud(player);
                }
            }
        }, 0L, 1L);


        // Plugin startup logic
        System.out.println("[VT] Vanilla Tweaks Started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[VT] Vanilla Tweaks Shutting Down...");

        List<String> list = new ArrayList<>();
        for (UUID uuid : chEnabled) {
            list.add(uuid.toString());
        }
        data.getConfig().set("chEnabled", list);
        try {
            data.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getPackName(String pack) {
        if (pack.equals("player-head-drops")) {
            return "Player Head Drops";
        }
        if (pack.equals("double-shulker-shells")) {
            return "Double Shulker Shells";
        }
        if (pack.equals("dragon-drops")) {
            return "Dragon Drops";
        }
        if (pack.equals("silence-mobs")) {
            return "Silence Mobs";
        }
        if (pack.equals("anti-creeper-grief")) {
            return "Anti Creeper Grief";
        }
        if (pack.equals("anti-enderman-grief")) {
            return "Anti Enderman Grief";
        }
        if (pack.equals("anti-ghast-grief")) {
            return "Anti Ghast Grief";
        }
        if (pack.equals("nether-portal-coords")) {
            return "Nether Portal Coords";
        }
        if (pack.equals("coords-hud")) {
            return "Coords HUD";
        }
        if (pack.equals("spectator-night-vision")) {
            return "Spectator Night Vision";
        }
        if (pack.equals("spectator-conduit-power")) {
            return "Spectator Conduit Power";
        }
        if (pack.equals("kill-boats")) {
            return "Kill Boats";
        }
        if (pack.equals("more-mob-heads")) {
            return "More Mob Heads";
        }
        if (pack.equals("multiplayer-sleep")) {
            return "Multiplayer Sleep";
        }
        if (pack.equals("unlock-all-recipes")) {
            return "Unlock All Recipes";
        }
        if (pack.equals("cauldron-concrete")) {
            return "Cauldron Concrete";
        }
        if (pack.equals("real-time-clock")) {
            return "Real Time CLock";
        }
        if (pack.equals("villager-death-messages")) {
            return "Villager Death Messages";
        }
        if (pack.equals("wandering-trades")) {
            return "Wandering Trades";
        }
        if (pack.equals("tpa")) {
            return "TPA";
        }
        if (pack.equals("homes")) {
            return "Homes";
        }
        if (pack.equals("spawn")) {
            return "Spawn";
        }
        if (pack.equals("durability-ping")) {
            return "Durability Ping";
        }
        if (pack.equals("tag")) {
            return "Tag";
        }
        if (pack.equals("xp-management")) {
            return "XP Management";
        }
        if (pack.equals("back")) {
            return "Back";
        }
        if (pack.equals("confetti-creepers")) {
            return "Confetti Creepers";
        }
        if (pack.equals("afk-display")) {
            return "AFK Display";
        }
        return pack;
    }
}
