package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Perks {
    public String getImageById(String perkId) throws IOException {
        String champions = Files.readString(Path.of("./runesReforged.json"));
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(champions);
        JsonArray arr = (JsonArray)obj;
        for (JsonElement item : arr){
            JsonArray runesArr = item.getAsJsonObject().get("slots").getAsJsonArray();
            for(JsonElement slot : runesArr){
                for (JsonElement rune : slot.getAsJsonObject().get("runes").getAsJsonArray()){
                    if(perkId.equals(rune.getAsJsonObject().get("id").getAsString())){
                        return rune.getAsJsonObject().get("icon").getAsString();
                    }
                }
            }
        }
        return "perk-images/Styles/RunesIcon.png";
    }
    public static List<Map.Entry<String, Integer>> getPerks(JsonElement page) {
        JsonArray arr = page.getAsJsonArray();
        List<Map.Entry<String, Integer>> main = chooseBest(arr.get(0),1);
        List<Map.Entry<String, Integer>> subMain =chooseBest(arr.get(1),3);
        List<Map.Entry<String, Integer>> primary = chooseBest(arr.get(2),2);
        main.addAll(subMain);
        main.addAll(primary);

        return main;
    }
    public static List<Map.Entry<String, Integer>> chooseBest(JsonElement perks, int num){
        Map<String, Integer> map = new HashMap<>();
        perks.getAsJsonObject().keySet();
        for (String perk : perks.getAsJsonObject().keySet()){
            Integer wins = perks.getAsJsonObject().get(perk).getAsJsonArray().get(0).getAsInt();
            map.put(perk,wins);
        }

        List<Map.Entry<String, Integer>> sortedPerks = new ArrayList<>(map.entrySet());
        sortedPerks.sort(Map.Entry.comparingByValue());

        int size = sortedPerks.size();
        return sortedPerks.subList(size-num, size);
    }
}
