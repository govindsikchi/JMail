package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.EmailManager;
import com.controller.services.EmailSenderService;
import com.model.EmailAccount;
import com.view.ViewFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {

    private List<File> attachments = new ArrayList<>();

    @FXML
    private HTMLEditor HtmlEditor;

    @FXML
    private Label ErrorMessage;

    @FXML
    private TextField RecipientField;

    @FXML
    private TextField SubjectField;

    @FXML
    private ChoiceBox<EmailAccount> EmailAccountChoice;

    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void onAttachAction() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            attachments.add(selectedFile);
        }
    }

    @FXML
    void sendButtonAction() {
        System.out.println(HtmlEditor.getHtmlText());
        System.out.println("Send Email Button Clicked...!");
        EmailSenderService emailSenderService = new EmailSenderService(
                EmailAccountChoice.getValue(),
                SubjectField.getText(),
                RecipientField.getText(),
                HtmlEditor.getHtmlText(),
                attachments
        );
        emailSenderService.start();
        emailSenderService.setOnSucceeded(e -> {
            EmailSendingResult emailSendingResult = emailSenderService.getValue();
            switch (emailSendingResult) {
                case SUCCESS:
                    // Stage stage = (Stage) RecipientField.getScene().getWindow();
                    // viewFactory.closeStage(stage);
                    closeWindow();
                    break;
                case FAILED_BY_PROVIDER:
                    ErrorMessage.setText("Provider Error...!");
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    ErrorMessage.setText("Unexpected Error...!");
                    break;
            }
        });
        // closeWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailAccountChoice.setItems(emailManager.getEmailAccounts());
        EmailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }

    public void closeWindow() {
        Stage stage = (Stage) ErrorMessage.getScene().getWindow();
        viewFactory.closeStage(stage);
    }
}