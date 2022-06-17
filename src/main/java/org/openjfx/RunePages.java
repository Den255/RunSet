package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.Map;

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
    public void setPage(String name, List<Map.Entry<String, Integer>> perkIds){
        //Attack   5008 5005 5007 // adaptive force / speed attack / cast speed
        //Universe 5008 5002 5003 // adaptive force / def          / mag resist
        //Defense  5001 5002 5003 // health         / def          / mag resist
        StringBuilder perkIdsStr = new StringBuilder();
        for(Map.Entry<String, Integer> perk : perkIds){
            perkIdsStr.append(perk.getKey()).append(",");
        }

        String style = Perks.getPerkStyle(perkIds.get(0).getKey()); // get style
        String subStyle = Perks.getPerkStyle(perkIds.get(4).getKey()); // get style // get sub-style

        String defaultMods = "5007,5008,5001";
        System.out.println(perkIdsStr);
        String data = "{\"name\":\""+name+"\"," +
                    "\"primaryStyleId\":"+style+"," +
                    "\"subStyleId\":"+subStyle+"," +
                    "\"selectedPerkIds\": ["+perkIdsStr+defaultMods+"]," +
                    "\"current\":true}";
        ClientApi.postRequest("/lol-perks/v1/pages",data);
    }
}
