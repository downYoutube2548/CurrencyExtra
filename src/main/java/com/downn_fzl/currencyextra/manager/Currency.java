package com.downn_fzl.currencyextra.manager;

import com.downn_fzl.currencyextra.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Currency {
    private final String id;
    private final String symbol;
    private final String displayName;
    private final ItemStack displayItem;

    private final double startingBalance;
    private final double defaultMinAmount;
    private final double defaultMaxAmount;
    private final double minimumPayAmount;

    private final boolean allowPayment;

    public Currency(String id, String symbol, String displayName, String displayItem, double startingBalance, double defaultMinAmount, double defaultMaxAmount, double minimumPayAmount, boolean allowPayment) {
        this.id = id;
        this.symbol = symbol;
        this.displayName = displayName;
        this.displayItem = new ItemStack(Material.valueOf(displayItem));
        this.startingBalance = startingBalance;
        this.defaultMinAmount = defaultMinAmount;
        this.defaultMaxAmount = defaultMaxAmount;
        this.minimumPayAmount = minimumPayAmount;
        this.allowPayment = allowPayment;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return Utils.colorize(symbol);
    }

    public String getDisplayName() {
        return Utils.colorize(displayName);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public double getStartingBalance() { return startingBalance; }

    public double getDefaultMinAmount() {
        return defaultMinAmount;
    }

    public double getDefaultMaxAmount() {
        return defaultMaxAmount;
    }

    public double getMinimumPayAmount() {
        return minimumPayAmount;
    }

    public boolean isAllowPayment() {
        return allowPayment;
    }
}
