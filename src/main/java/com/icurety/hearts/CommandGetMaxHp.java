package com.icurety.hearts;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandGetMaxHp implements CommandExecutor {

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1)
        {
            return false;
        }
        else {
            String playerName = args[0];

            if(playerName.startsWith("@p"))
            {
                Player closest = findClosestPlayer(sender, command);
                if(closest != null)
                    playerName = closest.getName();
            }

            Player player = sender.getServer().getPlayer(playerName);
            if(player != null)
            {
                sender.sendMessage("" + (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            }
            else sender.sendMessage("That player isn't on the server");
            return true;
        }
    }
}
