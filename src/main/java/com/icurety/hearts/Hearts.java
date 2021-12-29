package com.icurety.hearts;

import org.bukkit.plugin.java.JavaPlugin;

public final class Hearts extends JavaPlugin {

    public static Hearts instance;
    private static boolean lifeStealEnabled = false;

    public Hearts() {
        instance = this;
    }

    @Override
    public void onEnable() {
        LoadHandler.load();
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.getCommand("setmaxhp").setExecutor(new CommandSetMaxHp());
        this.getCommand("sethp").setExecutor(new CommandSetHp());
        this.getCommand("saveallmaxhp").setExecutor(new CommandSaveAllMaxHp());
        this.getCommand("setlifesteal").setExecutor(new CommandSetLifeSteal());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SaveHandler.save();
    }

    public static boolean isLifeStealEnabled() {
        return lifeStealEnabled;
    }

    public static void setLifeStealEnabled(boolean lifeStealEnabled) {
        Hearts.lifeStealEnabled = lifeStealEnabled;
    }
}
