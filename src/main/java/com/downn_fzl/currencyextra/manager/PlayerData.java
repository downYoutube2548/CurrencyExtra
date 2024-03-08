package com.downn_fzl.currencyextra.manager;

import com.downn_fzl.currencyextra.CurrencyExtra;
import com.downn_fzl.currencyextra.exception.DataNotLoadException;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerData {

    public static PlayerData get(UUID uuid) {
        if (CurrencyExtra.mapPlayerData.containsKey(uuid)) {
            return CurrencyExtra.mapPlayerData.get(uuid);
        } else {
            throw new DataNotLoadException("Player data has not been loaded yet!");
        }
    }

    public static void getSavedData(UUID uuid, Consumer<PlayerData> consumer) {
        CurrencyExtra.databaseManager.loadTemp(uuid, consumer);
    }

    private final UUID uuid;
    private final HashMap<Currency, Double> balance;
    private final HashMap<Currency, Double> maxBalance;

    public PlayerData(UUID uuid, HashMap<Currency, Double> balance, HashMap<Currency, Double> maxBalance) {
        this.uuid = uuid;
        this.balance = balance;
        this.maxBalance = maxBalance;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public HashMap<Currency, Double> getBalanceMap() {
        return balance;
    }

    public HashMap<Currency, Double> getMaxBalanceMap() {
        return maxBalance;
    }

    public double getBalance(Currency currency) {
        return balance.get(currency);
    }

    public void setBalance(Currency currency, double value, boolean force) {

        double finalValue = force ? value : value < currency.getDefaultMinAmount() ? currency.getDefaultMinAmount() : Math.min(value, getMaxBalance(currency));

        balance.put(currency, finalValue);
        CurrencyExtra.databaseManager.setBalance(uuid, currency, finalValue);
    }

    public double getMaxBalance(Currency currency) {
        return maxBalance.get(currency);
    }

    public void setMaxBalance(Currency currency, double value) {

        double finalValue = Math.max(currency.getDefaultMinAmount(), value);

        maxBalance.put(currency, finalValue);
        CurrencyExtra.databaseManager.setMaxBalance(uuid, currency, finalValue);
    }

    public void deposit(Currency currency, double value) {

        double current = getBalance(currency);

        double finalValue = Math.min(current + value, getMaxBalance(currency));
        setBalance(currency, finalValue, false);

    }

    public void withdraw(Currency currency, double value) {

        double current = getBalance(currency);

        double finalValue = Math.max(current - value, currency.getDefaultMinAmount());
        setBalance(currency, finalValue, false);

    }

    public void increaseMaxBalance(Currency currency, double value) {

        double current = getMaxBalance(currency);
        setMaxBalance(currency, current + value);

    }

    public void reduceMaxBalance(Currency currency, double value) {

        double current = getMaxBalance(currency);
        setMaxBalance(currency, current - value);

    }

}
