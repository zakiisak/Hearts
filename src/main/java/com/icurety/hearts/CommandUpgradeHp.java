package com.icurety.hearts;

import org.bukkit.GameMode;
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

public class CommandUpgradeHp implements CommandExecutor {

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

    private void addItemToPlayer(Player player)
    {
        ItemStack stack = new ItemStack(Material.APPLE, 1);
        List<String> lore = new ArrayList<String>();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Heart Container");
        lore.add("+1 heart");
        meta.setLore(lore);
        meta.setCustomModelData(20);
        stack.setItemMeta(meta);
        HashMap<Integer, ItemStack> overflowingItems = player.getInventory().addItem(stack);

        //drop overflowing heart container
        for(ItemStack overflowingItemStack : overflowingItems.values())
        {
            player.getWorld().dropItem(player.getEyeLocation(), overflowingItemStack);
        }

        if(overflowingItems.size() == 0)
            player.playSound(player.getLocation(), "heart.container.get.sound", 1f, 1f);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0)
        {
            String playerName = args[0];
            Player player = sender.getServer().getPlayer(playerName);
            if(player != null)
            {
                addItemToPlayer(player);
            }
            else sender.sendMessage("That player isn't logged on to the server");
        }
        else {

            if(sender instanceof BlockCommandSender)
            {
                Player player = findClosestPlayer(sender, command);
                if(player != null) {
                    if(player.getLevel() >= 30 || player.getGameMode() == GameMode.CREATIVE)
                    {
                        player.setLevel(player.getLevel() - 30);
                        addItemToPlayer(player);
                    }
                }
            }
            else if(sender instanceof Player)
            {
                Player player = (Player) sender;
                if(player.getLevel() >= 30)
                {
                    player.setLevel(player.getLevel() - 30);
                    addItemToPlayer(player);
                }
                else {
                    sender.sendMessage("You do not have 30 levels");
                }
            }
        }
        return true;
    }
}
