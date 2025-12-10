package com.artillexstudios.axtrade.commands.subcommands;

import com.artillexstudios.axtrade.AxTrade;
import com.artillexstudios.axtrade.database.HistoryDatabase;
import com.artillexstudios.axtrade.history.HistoriesGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum History {
    INSTANCE;

    public void execute(Player player, String targetName) {
        HistoryDatabase.getInstance().getTradeLogs(targetName).whenComplete((logs, ex) -> {
            if (ex != null) {
                player.sendMessage(Component.text("Bir hata meydana geldi.", NamedTextColor.RED));
                ex.printStackTrace();
                return;
            }

            if (logs == null || logs.isEmpty()) {
                player.sendMessage(Component.text("Bu oyuncu hiç takas yapmamış.", NamedTextColor.RED));
                return;
            }

            Bukkit.getGlobalRegionScheduler().run(AxTrade.getInstance(), (task) -> HistoriesGui.open(player, targetName, logs));
        });
    }
}
