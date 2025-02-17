package me.teakivy.teakstweaks.packs.teakstweaks.sudoku;

import me.teakivy.teakstweaks.packs.BasePack;
import me.teakivy.teakstweaks.packs.PackType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Sudoku extends BasePack {

    public Sudoku() {
        super("Sudoku", "sudoku", PackType.TEAKSTWEAKS, Material.GOLDEN_SWORD, "Allow players to commit sudoku", "Command: /sudoku");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getEntity().getScoreboardTags().contains("sudoku-message")) return;
        event.setDeathMessage(event.getEntity().getName() + " committed Sudoku");
        event.getEntity().getScoreboardTags().remove("sudoku-message");
    }
}
