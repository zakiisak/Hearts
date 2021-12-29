package com.icurety.hearts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSaveAllMaxHp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp())
        {
            SaveHandler.save();
            sender.sendMessage("Done.");
        }
        else sender.sendMessage("You are not authorized to use this command");
        return true;
    }
}
