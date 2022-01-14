package com.icurety.hearts;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandGiveHeartPiece implements CommandExecutor {

    private Integer tryParse(String num) {
        try {
            return Integer.parseInt(num);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }

    private Player findClosestPlayer(CommandSender sender, Command command) {
        if(sender instanceof BlockCommandSender)
        {
            Block b = ((BlockCommandSender) sender).getBlock();
            List<Player> players = b.getLocation().getWorld().getPlayers();
            Player closest = null;
            double closestDistance = Double.MAX_VALUE;
            for(Player p : players) {
                double distance = p.getLocation().distance(b.getLocation());
                if(distance < closestDistance)
                {
                    closestDistance = distance;
                    closest = p;
                }
            }

            return closest;
        }
        return null;
    }

    private void addItemToPlayer(Player player, int amount)
    {
        ItemStack stack = Recipes.getHeartPieceItemStack();
        stack.setAmount(amount);
        HashMap<Integer, ItemStack> overflowingItems = player.getInventory().addItem(stack);

        //drop overflowing heart container
        for(ItemStack overflowingItemStack : overflowingItems.values())
        {
            player.getWorld().dropItem(player.getEyeLocation(), overflowingItemStack);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()) {
            if (args.length < 1) {
                return false;
            } else if (args.length == 2) {
                String playerName = args[0];

                if (playerName.startsWith("@p")) {
                    Player closest = findClosestPlayer(sender, command);
                    if (closest != null)
                        playerName = closest.getName();
                }

                Player player = sender.getServer().getPlayer(playerName);
                if (player != null) {
                    Integer amount = tryParse(args[1]);
                    if (amount != null) {
                        addItemToPlayer(player, amount);
                    } else sender.sendMessage("The amount must be a whole number");
                } else sender.sendMessage("That player isn't on the server");
                return true;
            } else return false;
        }
        else sender.sendMessage("You aren't authorized to give heart pieces");
    }
}
