package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.Iterator;

public class MainController {
    
    @FXML
    private Label label;
    @FXML
    private ListView localRunesList;
    @FXML
    private HBox runePage;
    public static ClientApi api = new ClientApi();
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        System.out.println("You clicked me!");


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
                String page_id = rune.get("id").getAsString();
                if (rune.get("isDeletable").getAsBoolean())
                    //localRunesList.getItems().add(name);
                    //HBox page = new runePage;
                    localRunesList.getItems().add(new HBoxCell(name,"X",page_id));
                System.out.println(key);
            }
        }
        label.setText(content);

    }
    public static class HBoxCell extends HBox {
        Label label = new Label();
        //Button button = new Button();
        Button editButton = new Button();
        Button delButton = new Button();
        HBoxCell(String labelText, String buttonText,String page_id) {
            //super();

            label.setText(labelText);
            label.prefWidth(304.0);
            //label.setMaxWidth(Double.MAX_VALUE);
            //HBox.setHgrow(label, Priority.ALWAYS);

            editButton.setText("E");
            //button.setId("delete");
            editButton.prefWidth(35.0);
            delButton.setText("X");
            delButton.onMouseClickedProperty();
            delButton.prefWidth(35.0);
            delButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    System.out.println("Delete runepage!");
                    try {
                        String content = api.delRequest("/lol-perks/v1/pages/"+page_id);
                        System.out.println(content);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    label.setText("Accepted");

                }
            });
            this.getChildren().addAll(label, editButton, delButton);
        }
    }
    @FXML
    public void onDelete(MouseEvent mouseEvent) {

    }
    public void initialize() {
        // TODO
    }    
}