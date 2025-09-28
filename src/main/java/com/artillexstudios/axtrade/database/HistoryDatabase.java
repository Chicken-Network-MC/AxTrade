package com.artillexstudios.axtrade.database;

import com.artillexstudios.axtrade.AxTrade;
import com.artillexstudios.axtrade.models.TradeHistory;
import com.chickennw.utils.database.sql.Database;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class HistoryDatabase extends Database {

    private static HistoryDatabase instance;

    public static HistoryDatabase getInstance() {
        if (instance == null) instance = new HistoryDatabase();
        return instance;
    }

    private HistoryDatabase() {
        super(AxTrade.getInstance());
    }

    public CompletableFuture<List<TradeHistory>> getTradeLogs(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = sessionFactory.openSession()) {
                Query<TradeHistory> query = session.createQuery("from TradeHistory where player1=:playerName or player2=:playerName", TradeHistory.class);
                query.setParameter("playerName", playerName);
                return query.getResultList();
            }
        }, executor);
    }

    public CompletableFuture<TradeHistory> getTradeLog(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = sessionFactory.openSession()) {
                Query<TradeHistory> query = session.createQuery("from TradeHistory where id=:id", TradeHistory.class);
                query.setParameter("id", id);
                return query.getSingleResultOrNull();
            }
        }, executor);
    }

}
