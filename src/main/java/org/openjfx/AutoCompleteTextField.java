package org.openjfx;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import static org.openjfx.MainController.champion;

public class AutoCompleteTextField extends TextField
{
    private final SortedSet<String> entries;
    private final ContextMenu entriesPopup;

    public AutoCompleteTextField() throws IOException {
        super();
        entries = champion.getNames();
        entriesPopup = new ContextMenu();
        textProperty().addListener((observableValue, s, s2) -> {
            if (getText().length() == 0)
            {
                entriesPopup.hide();
            } else
            {
                LinkedList<String> searchResult = new LinkedList<>(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                if (entries.size() > 0)
                {
                    populatePopup(searchResult);
                    if (!entriesPopup.isShowing())
                    {
                        entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                    }
                } else
                {
                    entriesPopup.hide();
                }
            }
        });
        focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> entriesPopup.hide());
    }
//    public SortedSet<String> getEntries() { return entries; }
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent -> {
                setText(result);
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }
}