package com.downn_fzl.currencyextra.command.CurrencyCommand;

import com.downn_fzl.currencyextra.command.CommandTreeNode;
import com.downn_fzl.currencyextra.command.CurrencyCommand.currency.*;
import com.downn_fzl.currencyextra.manager.Currency;
import com.downn_fzl.currencyextra.manager.CurrencyManager;
import com.downn_fzl.currencyextra.manager.PlayerData;
import com.downn_fzl.currencyextra.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CurrencyCommand extends BukkitCommand {

    private final String command;
    private final Currency currency;

    private final HashMap<String, CommandTreeNode> children = new HashMap<>();

    public CurrencyCommand(@NotNull String name) {
        super(name);
        this.command = name;
        this.currency = CurrencyManager.get(name.toUpperCase());

        addChild(new GiveCommandTreeNode(null));
        addChild(new CheckCommandTreeNode(null));
        addChild(new TakeCommandTreeNode(null));
        addChild(new MaxCommandTreeNode(null));
        addChild(new SetCommandTreeNode(null));
        addChild(new PayCommandTreeNode(null));
    }

    public void addChild(CommandTreeNode child) { children.put(child.getId(), child); }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {

        if (args.length >= 1) {
            if (children.containsKey(args[0])) {
                List<String> argument = new ArrayList<>(Arrays.asList(args));
                argument.remove(0);
                children.get(args[0]).execute(sender, command, argument.toArray(String[]::new));
            } else {
                sender.sendMessage(Utils.getMessage("invalid-syntax", true));
                sender.sendMessage(Utils.getMessage("usage", true)
                        .replace("{command}", "/" + command + " " + String.join("/", children.keySet()))
                );
            }
        } else {
            if (sender instanceof Player player) {
                PlayerData playerData = PlayerData.get(player.getUniqueId());

                sender.sendMessage(Utils.getMessage("balance", true)
                        .replace("{amount}", Utils.formatNumber(playerData.getBalance(currency)))
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", currency.getDisplayName())
                );
            }
        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {

        List<String> output = new ArrayList<>();

        if (args.length == 1) {
            List<String> arg1 = new ArrayList<>(children.keySet());
            output = Utils.tabComplete(args[0], arg1);
        } else {
            if (children.containsKey(args[0])) {
                List<String> argument = new ArrayList<>(Arrays.asList(args));
                argument.remove(0);
                output = children.get(args[0]).tabComplete(sender, command, argument.toArray(String[]::new));
            }
        }

        return output;
    }


}
