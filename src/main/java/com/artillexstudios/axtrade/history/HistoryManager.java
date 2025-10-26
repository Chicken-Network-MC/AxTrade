package com.artillexstudios.axtrade.history;

import com.artillexstudios.axtrade.hooks.currency.CurrencyHook;
import com.artillexstudios.axtrade.models.TradeHistory;
import com.artillexstudios.axtrade.trade.Trade;
import com.chickennw.utils.utils.ItemStackBase64;
import com.chickennw.utils.utils.ItemStackSerializer;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class HistoryManager {

    public static final Gson GSON = new Gson();

    public static TradeHistory createHistory(Trade trade) {
        TradeHistory history = new TradeHistory();

        history.setPlayer1(trade.getPlayer1().getPlayer().getName());
        history.setPlayer2(trade.getPlayer2().getPlayer().getName());

        for (Map.Entry<CurrencyHook, Double> entry : trade.getPlayer1().getCurrencies().entrySet())
            history.setPlayer1Money(entry.getValue());

        for (Map.Entry<CurrencyHook, Double> entry : trade.getPlayer2().getCurrencies().entrySet())
            history.setPlayer2Money(entry.getValue());

        List<String> player1Items = trade.getPlayer1().getTradeGui().getItems(false).stream().map(ItemStackBase64::toBase64).toList();
        List<String> player2Items = trade.getPlayer2().getTradeGui().getItems(false).stream().map(ItemStackBase64::toBase64).toList();

        history.setPlayer1Items(GSON.toJson(player1Items));
        history.setPlayer2Items(GSON.toJson(player2Items));

        history.setCreatedAt(System.currentTimeMillis());

        return history;
    }

}
