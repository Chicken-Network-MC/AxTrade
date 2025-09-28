package com.artillexstudios.axtrade.history;

import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axtrade.models.TradeHistory;
import com.chickennw.utils.utils.ChatUtils;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HistoryGui {

    private static final int[] PLAYER_1_SLOTS = {
            9, 10, 11, 12,
            18, 19, 20, 21,
            27, 28, 29, 30,
            36, 37, 38, 39,
            45, 46, 47, 48
    };

    private static final int[] PLAYER_2_SLOTS = {
            14, 15, 16, 17,
            23, 24, 25, 26,
            32, 33, 34, 35,
            41, 42, 43, 44,
            50, 51, 52, 53
    };

    private static final int[] SEPARATOR_SLOTS = {
            4, 13, 22, 31, 40, 49
    };

    public static void open(Player player, TradeHistory history) {
        Gui gui = Gui.gui().rows(6).title(Component.text("Takas Geçmişi")).disableAllInteractions().create();

        gui.setItem(0, new GuiItem(ItemBuilder.create(Material.PLAYER_HEAD)
                .setName("<yellow>%s eşyaları".formatted(history.getPlayer1())).get()));

        gui.setItem(3, new GuiItem(ItemBuilder.create(Material.PAPER)
                .setName("<yellow>%s para".formatted(history.getPlayer1()))
                .setLore(List.of("<gray>Miktar: <yellow>%s".formatted(history.getPlayer1Money()))).get()));

        gui.setItem(5, new GuiItem(ItemBuilder.create(Material.PLAYER_HEAD)
                .setName("<yellow>%s eşyaları".formatted(history.getPlayer2())).get()));

        gui.setItem(8, new GuiItem(ItemBuilder.create(Material.PAPER)
                .setName("<yellow>%s para".formatted(history.getPlayer2()))
                .setLore(List.of("<gray>Miktar: <yellow>%s".formatted(history.getPlayer2Money()))).get()));

        for (int slot : SEPARATOR_SLOTS) {
            gui.setItem(slot, new GuiItem(ItemBuilder.create(Material.GRAY_STAINED_GLASS_PANE).setName("").get()));
        }

        for (int i = 0; i < history.getPlayer1ItemStacks().size() && i < PLAYER_1_SLOTS.length; i++) {
            ItemStack item = history.getPlayer1ItemStacks().get(i);
            gui.setItem(PLAYER_1_SLOTS[i], new GuiItem(ItemBuilder.create(item).get()));
        }

        for (int i = 0; i < history.getPlayer2ItemStacks().size() && i < PLAYER_2_SLOTS.length; i++) {
            ItemStack item = history.getPlayer2ItemStacks().get(i);
            gui.setItem(PLAYER_2_SLOTS[i], new GuiItem(ItemBuilder.create(item).get()));
        }

        gui.open(player);
    }

}
