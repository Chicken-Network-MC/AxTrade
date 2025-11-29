package com.artillexstudios.axtrade.listeners;

import com.artillexstudios.axapi.utils.Cooldown;
import com.artillexstudios.axtrade.request.Requests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.artillexstudios.axtrade.AxTrade.CONFIG;

public class EntityInteractListener implements Listener {
    private static final Cooldown<UUID> cooldown = Cooldown.createSynchronized();

    @EventHandler (ignoreCancelled = true)
    public void onInteract(@NotNull PlayerInteractEntityEvent event) {
        if (!CONFIG.getBoolean("shift-click-send-request", true)) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("axtrade.trade")) return;
        if (cooldown.hasCooldown(player.getUniqueId())) return;
        if (!player.isSneaking()) return;
        if (!(event.getRightClicked() instanceof Player sendTo)) return;
        if (!sendTo.isOnline()) return;

        cooldown.addCooldown(player.getUniqueId(), 100L);
        Requests.addRequest(player, sendTo);
        event.setCancelled(true);
    }
}
