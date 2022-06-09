package org.openjfx;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class JsonDecode {
    public static JsonArray decode(String raw_data) {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(raw_data);
        return (JsonArray)obj;
    }
}