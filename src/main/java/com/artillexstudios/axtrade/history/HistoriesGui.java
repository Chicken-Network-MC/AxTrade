package com.artillexstudios.axtrade.history;

import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axtrade.models.TradeHistory;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoriesGui {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static void open(Player player, String playerName, List<TradeHistory> logs) {
        PaginatedGui gui = Gui.paginated().rows(6).title(Component.text("Takas Geçmişi - " + playerName))
                .pageSize(45).disableAllInteractions().create();

        for (int i = 0; i < logs.size(); i++) {
            TradeHistory history = logs.get(i);
            int finalI = i;
            gui.addItem(new GuiItem(ItemBuilder.create(Material.PLAYER_HEAD)
                    .setName("<yellow>%s - %s <gray>(%s)".formatted(history.getPlayer1(), history.getPlayer2(),
                            DATE_FORMAT.format(new Date(history.getCreatedAt())))).get(), event -> {
                HistoryGui.open(player, logs, finalI);
            }));
        }

        gui.setItem(48, new GuiItem(ItemBuilder.create(Material.ARROW).setName("<yellow>Önceki Sayfa").get(), event -> gui.previous()));
        gui.setItem(50, new GuiItem(ItemBuilder.create(Material.ARROW).setName("<yellow>Sonraki Sayfa").get(), event -> gui.next()));

        gui.open(player);
    }

}
