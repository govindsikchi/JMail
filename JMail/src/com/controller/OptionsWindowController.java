package com.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.EmailManager;
import com.view.ColorTheme;
import com.view.FontSize;
import com.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsWindowController extends BaseController implements Initializable {
    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Slider FontSizePicker;

    @FXML
    private ChoiceBox<ColorTheme> ThemePicker;

    @FXML
    void OptionsApplyAction() {
        System.out.println("Apply Options Button Clicked...");
        viewFactory.setColorTheme(ThemePicker.getValue());
        viewFactory.setFontSize(FontSize.values()[(int)(FontSizePicker.getValue())]);

        System.out.println("Current Font Size: " + viewFactory.getFontSize());
        System.out.println("Current Color Theme: " + viewFactory.getColorTheme());
        viewFactory.updateStyles();
        closeWindow();
    }

    @FXML
    void OptionsCancelAction() {
        System.out.println("Cancel Options Button Clicked...");
        closeWindow();
    }

    public void closeWindow() {
        Stage stage = (Stage) FontSizePicker.getScene().getWindow();
        viewFactory.closeStage(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpThemePicker();
        setUpFontSizePicker();
    }

    private void setUpFontSizePicker() {
        FontSizePicker.setMin(0);
        FontSizePicker.setMax(FontSize.values().length - 1);
        FontSizePicker.setValue(viewFactory.getFontSize().ordinal());
        FontSizePicker.setMajorTickUnit(1);
        FontSizePicker.setMinorTickCount(0);
        FontSizePicker.setBlockIncrement(1);
        FontSizePicker.setSnapToTicks(true);
        FontSizePicker.setShowTickMarks(true);
        FontSizePicker.setShowTickLabels(true);
        FontSizePicker.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return FontSize.values()[object.intValue()].toString();
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
        FontSizePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            FontSizePicker.setValue(newVal.intValue());
        });
    }

    private void setUpThemePicker() {
        ThemePicker.setItems(FXCollections.observableArrayList(ColorTheme.values()));
        ThemePicker.setValue(viewFactory.getColorTheme());
    }
}
