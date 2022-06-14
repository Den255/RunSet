package org.openjfx;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;

public class Champion {
    public ArrayList<String> getNames() throws IOException {
        String champions = Files.readString(Path.of("./champions.json"));
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(champions);
        JsonObject jsonObj = (JsonObject)obj;
        JsonElement jsonData = jsonObj.get("data");
        JsonObject championsData = jsonData.getAsJsonObject();
        Set<String> chNames = championsData.keySet();

        return new ArrayList<>(chNames);
    }
    public int getIdByName(String name) throws IOException {
        String champions = Files.readString(Path.of("./champions.json"));
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(champions);
        JsonObject jsonObj = (JsonObject)obj;
        JsonElement jsonData = jsonObj.get("data");
        JsonObject championsData = jsonData.getAsJsonObject();
        JsonObject champion = championsData.get(name).getAsJsonObject();

        return champion.get("key").getAsInt();
    }

    public JsonObject getRunes(String name) throws IOException {
        UGGSource uggSource = new UGGSource();
        return uggSource.getRunes(getIdByName(name));
    }
}
