package com.icurety.hearts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadHandler {

    private static List<String> getConfig(String childDocument) {
        File file = new File(Hearts.instance.getDataFolder(), childDocument);
        if(file.exists())
        {
            try {
                return Files.readAllLines(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void load() {
        loadMaxHpRegistry();
        loadLifeSteal();
    }

    private static void loadLifeSteal() {
        List<String> lines = getConfig(SaveHandler.MAXHP_CONFIG);
        if(lines != null) {
            boolean lifeStealEnabled = false;
            try {
                lifeStealEnabled = Boolean.parseBoolean(lines.get(0));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            Hearts.setLifeStealEnabled(lifeStealEnabled);
        }
    }

    private static void loadMaxHpRegistry() {
        List<String> lines = getConfig(SaveHandler.MAXHP_CONFIG);
        if(lines != null) {
            Map<String, Integer> maxHps = new HashMap<String, Integer>();
            try {
                for (String playerLine : lines) {
                    String[] split = playerLine.split(" ");
                    String uuid = split[0];
                    int maxHp = Integer.parseInt(split[1]);
                    maxHps.put(uuid, maxHp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MaxHpRegistry.loadRecords(maxHps);
        }
    }

}
