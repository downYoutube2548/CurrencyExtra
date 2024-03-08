package com.downn_fzl.currencyextra.event;

import com.downn_fzl.currencyextra.CurrencyExtra;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CurrencyExtra.databaseManager.load(event.getPlayer());
    }
}
