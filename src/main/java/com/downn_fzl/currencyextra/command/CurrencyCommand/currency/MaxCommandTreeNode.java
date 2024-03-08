package com.downn_fzl.currencyextra.command.CurrencyCommand.currency;

import com.downn_fzl.currencyextra.command.CommandTreeNode;
import com.downn_fzl.currencyextra.command.CurrencyCommand.currency.max.IncreaseCommandTreeNode;
import com.downn_fzl.currencyextra.command.CurrencyCommand.currency.max.ReduceCommandTreeNode;
import com.downn_fzl.currencyextra.command.CurrencyCommand.currency.max.SetCommandTreeNode;

public class MaxCommandTreeNode extends CommandTreeNode {
    public MaxCommandTreeNode(CommandTreeNode parent) {
        super(parent, "max");

        addChild(new IncreaseCommandTreeNode(this));
        addChild(new ReduceCommandTreeNode(this));
        addChild(new SetCommandTreeNode(this));
    }
}
