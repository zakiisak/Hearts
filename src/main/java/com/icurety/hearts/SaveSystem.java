package com.icurety.hearts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class SaveSystem {

    public static final String MAXHP_CONFIG = "players.txt";
    public static final String LIFESTEAL_CONFIG = "lifesteal.txt";

    private static void saveConfig(String childDocumentFileName, String contents) {

        if(Hearts.instance.getDataFolder().exists() == false)
            Hearts.instance.getDataFolder().mkdirs();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(Hearts.instance.getDataFolder(), childDocumentFileName)));
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        saveMaxHpRegistry();
        saveLifeSteal();
    }

    private static void saveLifeSteal() {
        saveConfig(LIFESTEAL_CONFIG, "" + Hearts.isLifeStealEnabled());
    }

    private static void saveMaxHpRegistry() {
        Map<String, Integer> records = MaxHpRegistry.getRecords();

        String output = "";

        for(String id : records.keySet()) {
            Integer maxHp = records.get(id);
            output += id + " " + maxHp + "\n";
        }
        saveConfig(MAXHP_CONFIG, output);
    }

}
