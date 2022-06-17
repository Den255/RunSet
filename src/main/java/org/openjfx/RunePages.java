package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Arrays;

public class RunePages {
    public JsonArray getClientPages() {
        System.out.println("Getting rune pages from client.");
        String content = ClientApi.getRequest("/lol-perks/v1/pages");
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(content);
        return (JsonArray)obj;
    }
    public void deleteCurrent() {
        String content = ClientApi.getRequest("/lol-perks/v1/pages");
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(content);
        JsonArray pages = (JsonArray)obj;
        for (JsonElement page : pages){
            if(page.getAsJsonObject().get("current").getAsBoolean()){
                deletePage(page.getAsJsonObject().get("id").getAsString());
            }
        }
    }
    public void deletePage(String pageId) {
        ClientApi.delRequest("/lol-perks/v1/pages/" + pageId);
    }
    public void setPage(String name, String[] perkIds){
        //Attack   5008 5005 5007 // adaptive force / speed attack / cast speed
        //Universe 5008 5002 5003 // adaptive force / def          / mag resist
        //Defense  5001 5002 5003 // health         / def          / mag resist
        String defaultMods = "5007,5008,5001";
        System.out.println(Arrays.toString(perkIds));
        String data = "{\"name\":\""+name+"\"," +
                    "\"primaryStyleId\":8300," +
                    "\"subStyleId\":8400," +
                    "\"selectedPerkIds\": ["+String.join(",", perkIds)+","+defaultMods+"]," +
                    "\"current\":true}";
        ClientApi.postRequest("/lol-perks/v1/pages",data);
    }
}
