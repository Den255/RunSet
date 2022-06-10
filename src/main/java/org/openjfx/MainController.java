package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.TextFields;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MainController {

    public ArrayList<String> championNames;
    @FXML
    public TextField search;
    @FXML
    private ListView localRunesList;
    public static ClientApi api = new ClientApi();

    public MainController() {
        try {
            DDragon chData = new DDragon();
            championNames = chData.getData();
        }catch (IOException e){
            System.out.println("DDragon data not loaded!");
        }
    }
    @FXML
    private void handleSearchAction(MouseEvent event) {
        System.out.println("Search clicked!");
        TextFields.bindAutoCompletion(search, championNames);
    }
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        System.out.println("You clicked me!");

        if (localRunesList != null)
            localRunesList.getItems().clear();

        String content = api.getRequest("/lol-perks/v1/pages");
        if(content!=null){
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(content);
            JsonArray array = (JsonArray)obj;

            for (JsonElement key : array) {
                JsonObject rune = key.getAsJsonObject();
                String name = rune.get("name").getAsString();
                String page_id = rune.get("id").getAsString();
                if (rune.get("isDeletable").getAsBoolean())
                    localRunesList.getItems().add(new HBoxCell(name, "X", page_id));
                System.out.println(key);
            }
        }
    }
    public static class HBoxCell extends HBox {
        Label label = new Label();
        Button editButton = new Button();
        Button delButton = new Button();
        HBoxCell(String labelText, String buttonText,String page_id) {
            label.setText(labelText);
            label.prefWidth(304.0);

            editButton.setText("E");
            editButton.prefWidth(35.0);

            delButton.setText("X");
            delButton.onMouseClickedProperty();
            delButton.prefWidth(35.0);
            delButton.setOnAction(e -> {
                System.out.println("Delete rune page!");
                try {
                    String content = api.delRequest("/lol-perks/v1/pages/" + page_id);
                    System.out.println(content);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                label.setText("Deleted");
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