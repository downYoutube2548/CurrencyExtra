package com.downn_fzl.currencyextra.command.CurrencyCommand.currency;

import com.downn_fzl.currencyextra.command.CommandTreeNode;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PayCommandTreeNode extends CommandTreeNode {
    public PayCommandTreeNode(CommandTreeNode parent) {
        super(parent, "pay");
    }

    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        super.execute(sender, command, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String command, String[] args) {
        return super.tabComplete(sender, command, args);
    }
}
