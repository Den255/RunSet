package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
}
