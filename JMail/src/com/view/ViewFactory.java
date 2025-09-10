package com.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.EmailManager;
import com.controller.*;

import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {
    private EmailManager emailManager;
    private ArrayList<Stage> activeStages;
    private boolean mainViewInitialized = false;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<Stage>();
    }

    public boolean isMainViewInitialized() {
        return mainViewInitialized;
    }

    /* View Options Handling */
    private ColorTheme colorTheme = ColorTheme.LIGHT;
    private FontSize fontSize = FontSize.SMALL;

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public void showLoginWindow() {
        System.out.println("Login Window displayed...!");
        BaseController baseController = new LoginWindowController(emailManager, this, "LoginWindow.fxml");
        initializeStage(baseController, 420, 365);
    }

    public void showMainWindow() {
        System.out.println("Main Window displayed...!");
        BaseController baseController = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeStage(baseController, 0, 0);
        mainViewInitialized = true;
    }

    public void showOptionsWindow() {
        System.out.println("Options Window displayed...!");
        BaseController baseController = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");
        initializeStage(baseController, 415, 315);
    }

    public void showComposeMessageWindow() {
        System.out.println("Compose Message Window displayed...!");
        BaseController baseController = new ComposeMessageController(emailManager, this, "ComposeMessageWindow.fxml");
        initializeStage(baseController, 0, 0);
    }

    public void showEmailDetailsWindow() {
        System.out.println("Email Details Window displayed...!");
        BaseController baseController = new EmailDetailsController(emailManager, this, "EmailDetailsWindow.fxml");
        initializeStage(baseController, 0, 0);
    }

    private void initializeStage(BaseController baseController, int Width, int Height) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene;
        if(Height == 0 && Width == 0) {
            scene = new Scene(parent);
        } else {
            scene = new Scene(parent, Width, Height);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        activeStages.add(stage);
    }

    public void closeStage(Stage stageToClose) {
        stageToClose.close();
        activeStages.remove(stageToClose);
    }

    public void updateStyles() {
        for(Stage stage : activeStages) {
            Scene scene = stage.getScene();
            /* Handle the CSS */
            scene.getStylesheets().clear();
            ColorTheme.getThemeStyle(colorTheme);

            System.out.println(FontSize.getFontSize(fontSize));
            System.out.println(ColorTheme.getThemeStyle(colorTheme));

//            if (FontSize.getFontSize(fontSize) != null) {
//                scene.getStylesheets().add(getClass().getResource(FontSize.getFontSize(fontSize)).toExternalForm());
//            }
//            if (ColorTheme.getThemeStyle(colorTheme) != null) {
//                scene.getStylesheets().add(getClass().getResource(ColorTheme.getThemeStyle(colorTheme)).toExternalForm());
//            }

            scene.getStylesheets().add(getClass().getResource(FontSize.getFontSize(fontSize)).toExternalForm());
            scene.getStylesheets().add(getClass().getResource(ColorTheme.getThemeStyle(colorTheme)).toExternalForm());
        }
    }
}
