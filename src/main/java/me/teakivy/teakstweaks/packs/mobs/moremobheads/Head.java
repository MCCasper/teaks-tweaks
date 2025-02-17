package me.teakivy.teakstweaks.packs.mobs.moremobheads;

import me.teakivy.teakstweaks.Main;
import me.teakivy.teakstweaks.utils.datamanager.DataManager;

public class Head {
    static Main main = Main.getPlugin(Main.class);
    static DataManager data = main.data;

    public static double[] getChance(String path) {
        double[] chances = {data.getConfig().getDouble("mob-heads." + path + ".chance"), data.getConfig().getDouble("mob-heads." + path + ".looting-bonus")};
        return chances;
    }

    public static double[] getPlayerChance() {
        double[] chances = {data.getConfig().getDouble("player-heads.chance"), data.getConfig().getDouble("player-heads.looting-bonus")};
        return chances;
    }

}
