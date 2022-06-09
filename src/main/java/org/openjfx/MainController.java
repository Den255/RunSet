package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Iterator;

public class MainController {
    
    @FXML
    private Label label;
    public ListView localRunesList;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        System.out.println("You clicked me!");

        ClientApi api = new ClientApi();
        String content = api.getRequest("/lol-perks/v1/pages");
        if(content!=null){
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(content);
            JsonArray array = (JsonArray)obj;

            Iterator<JsonElement> keys = array.iterator();
            while(keys.hasNext()) {
                JsonElement key = keys.next();

                JsonObject rune = key.getAsJsonObject();
                String name = rune.get("name").getAsString();

                if (rune.get("isDeletable").getAsBoolean())
                    localRunesList.getItems().add(name);
                System.out.println(key);
            }
        }
        label.setText(content);

    }

    public void initialize() {
        // TODO
    }    
}