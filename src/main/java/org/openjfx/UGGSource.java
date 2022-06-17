package org.openjfx;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class UGGSource {
    private static final String UGG_VERSION = "1.1";
    private static final String BASE_URL = "https://stats2.u.gg/lol/" + UGG_VERSION + "/table/runes/10_14/ranked_solo_5x5/%championId%/1.4.0.json";
    public JsonObject getRunes(int championId) {
        //Example: https://stats2.u.gg/lol/1.1/table/runes/10_14/ranked_solo_5x5/103/1.4.0.json
        String runesStr = "";
        try {
            URL url = new URL(BASE_URL.replace("%championId%", Integer.toString(championId)));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            InputStreamReader streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            runesStr = new BufferedReader(streamReader).lines().collect(Collectors.joining("\n"));
            con.disconnect();
        } catch (IOException e) {
            System.out.println("No connection!");
        }
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(runesStr);
        JsonObject jsonObj = (JsonObject)obj;
        JsonObject plRunes = jsonObj.get("10").getAsJsonObject(); // PLATINUM_PLUS

        return plRunes.get("12").getAsJsonObject(); // World
    }
}
