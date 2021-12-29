package com.icurety.hearts;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSetLifeSteal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp())
        {
            if(args.length > 0)
            {
                String on = args[0].toLowerCase();
                if(on.equals("on"))
                {
                    Hearts.setLifeStealEnabled(true);
                    sender.sendMessage("Set life steal to " + ChatColor.GREEN + on);
                }
                else if(on.equals("off"))
                {
                    Hearts.setLifeStealEnabled(false);
                    sender.sendMessage("Set life steal to " + ChatColor.RED + on);
                }
                else sender.sendMessage("You must specify either on or off");

            }
            else return false;
        }
        else sender.sendMessage("You are not authorized to use this command");
        return true;
    }
}
