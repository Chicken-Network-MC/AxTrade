package com.artillexstudios.axtrade.models;

import com.chickennw.utils.utils.ItemStackBase64;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.artillexstudios.axtrade.history.HistoryManager.GSON;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class TradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String player1;

    @Column
    private String player2;

    @Column(columnDefinition = "TEXT")
    private String player1Items;

    @Column(columnDefinition = "TEXT")
    private String player2Items;

    @Column
    private double player1Money;

    @Column
    private double player2Money;

    @Column
    private long createdAt;

    public List<ItemStack> getPlayer1ItemStacks() {
        List<String> list = GSON.fromJson(player1Items, TypeToken.get(List.class).getType());
        return list.stream().map(ItemStackBase64::fromBase64).toList();
    }

    public List<ItemStack> getPlayer2ItemStacks() {
        List<String> list = GSON.fromJson(player2Items, TypeToken.get(List.class).getType());
        return list.stream().map(ItemStackBase64::fromBase64).toList();
    }

}
