package org.openjfx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
public class MainController {

    public ArrayList<String> championNames;
    @FXML
    public ListView<RunesCell> localRunesList;
    @FXML
    public TextField search;
    public VBox dialogVbox;
    public static Champion champion = new Champion();
    public static RunePages pages = new RunePages();
    public MainController() {
        try {
            championNames = champion.getNames();
        }catch (IOException e){
            System.out.println("DDragon data not loaded!");
        }
    }
    @FXML
    private void handleButtonAction() {
        System.out.println("You clicked me!");
        if (localRunesList != null){
            localRunesList.getItems().clear();
        }
        for (JsonElement key : pages.getClientPages()) {
            JsonObject rune = key.getAsJsonObject();
            String name = rune.get("name").getAsString();
            String page_id = rune.get("id").getAsString();
            JsonArray perks = rune.get("selectedPerkIds").getAsJsonArray();
            URL imgLocation = getClass().getResource("runes/"+perks.get(0)+".png");
            List<Image> images = new ArrayList<>();
            images.add(new Image(String.valueOf(imgLocation)));
            if (rune.get("isDeletable").getAsBoolean())
                localRunesList.getItems().add(new RunesCell(images, name, page_id));
        }

    }
    @FXML
    private void handleSearchAction() throws IOException {
        String chSelected = search.getText();
        System.out.println("Search entered: "+chSelected);
        JsonObject worldRunes = champion.getRunes(chSelected);
        Set<String> runes = worldRunes.keySet();

        ListView<RunesCell> runesList = new ListView<>();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialogVbox = new VBox(20);

        for (String position : runes) {
            String positionStr = this.getPosition(position);
            List<Entry<String, Integer>> main = Perks.getPerks(worldRunes.get(position));
            System.out.println(main);
            List<Image> images = new ArrayList<>();
            Perks perks = new Perks();
            for(Entry<String,Integer>perk : main){
                images.add(new Image(String.valueOf(getClass().getResource(perks.getImageById(perk.getKey())))));
            }
            runesList.getItems().add(new RunesCell(images, chSelected+":"+positionStr+" Wins: "+main.get(0).getValue()+" ", null));
        }
        dialogVbox.getChildren().add(runesList);
        Scene dialogScene = new Scene(dialogVbox, 410, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    public String getPosition(String positionId){
        String[] positions = {"JUNGLE","SUPPORT","ADC","TOP","MID","NONE"};
        return positions[Integer.parseInt(positionId)-1];
    }
    public static class RunesCell extends HBox {
        ImageView imageView = new ImageView();
        Label label = new Label();
        Button editButton = new Button();
        Button delButton = new Button();
        RunesCell(List<Image> images, String labelText, String page_id) {
            HBox perks = new HBox();
            for(Image image : images){
                ImageView perksImages = new ImageView(image);
                perksImages.setFitHeight(25);
                perksImages.setFitWidth(25);
                perks.getChildren().add(perksImages);
            }
            label.setText(labelText);
            label.setMinWidth(195);

            editButton.setText("+");
            editButton.setId(page_id);
            editButton.prefWidth(35.0);

            delButton.setText("X");
            delButton.onMouseClickedProperty();
            delButton.prefWidth(35.0);

            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            editButton.setOnAction(e ->{
                pages.deleteCurrent();
                String[] perkIds = {"1","2","3","4","5"};
                pages.setPage("Test",perkIds);
            });
            this.getChildren().addAll(perks, label, editButton, delButton);
        }
    }
    public void initialize() {
        TextFields.bindAutoCompletion(search, championNames);
    }    
}