package com.artillexstudios.axtrade.safety;

import com.artillexstudios.axtrade.AxTrade;
import com.artillexstudios.axtrade.api.events.AxTradeCompleteEvent;
import com.artillexstudios.axtrade.trade.Trade;
import com.artillexstudios.axtrade.trade.TradePlayer;
import com.eduardomcb.discord.webhook.WebhookClient;
import com.eduardomcb.discord.webhook.WebhookManager;
import com.eduardomcb.discord.webhook.models.Embed;
import com.eduardomcb.discord.webhook.models.Field;
import com.eduardomcb.discord.webhook.models.Message;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SusTradeListener implements Listener {

    @EventHandler
    public void onTrade(AxTradeCompleteEvent event) {
        TradePlayer player1 = event.getTrade().getPlayer1();
        TradePlayer player2 = event.getTrade().getPlayer2();
        if ((getAddedMoney(player1) > 1_000_000 && hasAddedNoItemsOrMoney(player2))
                || (getAddedMoney(player2) > 1_000_000 && hasAddedNoItemsOrMoney(player1))) {
            flagTrade(event.getTrade(), FlagReason.MONEY);
        } else if ((player1.getTradeGui().getItems(false).stream().anyMatch(i -> Tag.SHULKER_BOXES.isTagged(i.getType())) && hasAddedNoItemsOrMoney(player2))
                || (player2.getTradeGui().getItems(false).stream().anyMatch(i -> Tag.SHULKER_BOXES.isTagged(i.getType())) && hasAddedNoItemsOrMoney(player1))) {
            flagTrade(event.getTrade(), FlagReason.SHULKER_BOX);
        } else if ((getSpawnerCount(player1) >= 5 && hasAddedNoItemsOrMoney(player2))
                || (getSpawnerCount(player2) >= 5 && hasAddedNoItemsOrMoney(player1))) {
            flagTrade(event.getTrade(), FlagReason.SPAWNER);
        }
    }

    private double getAddedMoney(TradePlayer player) {
        return player.getCurrencies().values().stream().filter(value -> value > 0).findFirst().orElse(0d);
    }

    private boolean hasAddedNoItemsOrMoney(TradePlayer player) {
        boolean hasItems = !player.getTradeGui().getItems(false).isEmpty();
        boolean hasMoney = player.getCurrencies().values().stream().anyMatch(value -> value > 0);
        return !hasItems && !hasMoney;
    }

    private int getSpawnerCount(TradePlayer player) {
        return player.getTradeGui().getItems(false).stream()
                .filter(item -> item.getType() == Material.SPAWNER)
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    private void flagTrade(Trade trade, FlagReason reason) {
        AxTrade.getInstance().getLogger().warning("Flagged trade between " + trade.getPlayer1().getPlayer().getName() + " and " + trade.getPlayer2().getPlayer().getName() + " for reason: " + reason.name());
        Message message = new Message().setContent("Şüpheli Takas Tespit Edildi!");
        Field player1Field = new Field("1. Oyuncu", "```%s```".formatted(trade.getPlayer1().getPlayer().getName()), true);
        Field player2Field = new Field("2. Oyuncu", "```%s```".formatted(trade.getPlayer2().getPlayer().getName()), true);
        String reasonText = switch (reason) {
            case MONEY -> "```Karşılıksız şekilde yüksek miktarda para verme```";
            case SHULKER_BOX -> "```Karşılıksız şekilde shulker verme```";
            case SPAWNER -> "```Karşılıksız şekilde çok sayıda spawner verme```";
        };
        Field reasonField = new Field("Sebep", reasonText, false);
        Embed embed = new Embed()
                .setTitle("Takas Detayları")
                .setColor(16711680) // Red color
                .setFields(new Field[]{player1Field, player2Field, reasonField});

        new WebhookManager().setChannelUrl(AxTrade.CONFIG.getString("flagged-webhook-url"))
                .setEmbeds(new Embed[]{embed}).setMessage(message)
                .setListener(new WebhookClient.Callback() {
                    @Override
                    public void onSuccess(String s) {
                    }

                    @Override
                    public void onFailure(int i, String s) {
                    }
                })
                .exec();
    }

}
