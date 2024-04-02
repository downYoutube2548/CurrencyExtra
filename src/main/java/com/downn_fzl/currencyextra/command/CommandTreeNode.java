package com.downn_fzl.currencyextra.command;

import com.downn_fzl.currencyextra.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CommandTreeNode {

    private final String id;
    private final CommandTreeNode parent;
    private final HashMap<String, CommandTreeNode> children = new HashMap<>();

    public CommandTreeNode(CommandTreeNode parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    public CommandTreeNode getParent() { return parent; }
    public String getId() { return id; }
    public void addChild(CommandTreeNode child) { children.put(child.getId(), child); }
    public Collection<CommandTreeNode> getChild() { return children.values(); }
    public boolean hasChild(String id) {
        return this.children.containsKey(id.toLowerCase());
    }
    public List<CommandTreeNode> getParents() {
        List<CommandTreeNode> output = new ArrayList<>();
        CommandTreeNode current = this;
        while (current != null) {
            output.add(current);
            current = current.getParent();
        }

        Collections.reverse(output);
        return output;
    }
    public String getCurrentCommand(String command) {
        return "/" + command + " " + getParents().stream().map(CommandTreeNode::getId).collect(Collectors.joining(" "));
    }

    public void execute(CommandSender sender, String command, String[] args) {
        if (args.length >= 1) {
            if (children.containsKey(args[0])) {
                List<String> argument = new ArrayList<>(Arrays.asList(args));
                argument.remove(0);
                children.get(args[0]).execute(sender, command, argument.toArray(String[]::new));
            } else {
                sender.sendMessage(Utils.getMessage("invalid-syntax", true));
                sender.sendMessage(Utils.getMessage("usage", true)
                        .replace("{command}", getCurrentCommand(command) + " " + String.join("/", children.keySet()))
                );
            }
        } else {
            sender.sendMessage(Utils.getMessage("invalid-syntax", true));
            sender.sendMessage(Utils.getMessage("usage", true)
                    .replace("{command}", getCurrentCommand(command) + " " + String.join("/", children.keySet()))
            );
        }
    }
    public List<String> tabComplete(CommandSender sender, String command, String[] args) {
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
    };
}
