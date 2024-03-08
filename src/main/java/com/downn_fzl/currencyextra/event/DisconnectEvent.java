package com.downn_fzl.currencyextra.event;

import com.downn_fzl.currencyextra.CurrencyExtra;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectEvent implements Listener {

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        CurrencyExtra.mapPlayerData.remove(event.getPlayer().getUniqueId());
    }
}
