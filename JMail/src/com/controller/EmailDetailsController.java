package com.controller;

import com.EmailManager;
import com.controller.services.MessageRendererService;
import com.model.EmailMessage;
import com.view.ViewFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {

    String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/Downloads/";

    @FXML
    private Label AttachmentsLabel;

    @FXML
    private Label SubjectLabel;

    @FXML
    private Label SenderLabel;

    @FXML
    private HBox HBoxDownloads;

    @FXML
    private WebView WebView;

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        SubjectLabel.setText(emailMessage.getSubject());
        SenderLabel.setText(emailMessage.getSender());
        loadAttachments(emailMessage);

        MessageRendererService messageRendererService = new MessageRendererService(WebView.getEngine());
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();
    }

    private void loadAttachments(EmailMessage emailMessage) {
        if(emailMessage.isHasAttachments()) {
            for (MimeBodyPart mimeBodyPart : emailMessage.getAttachmentList()) {
                try {
                    AttachmentButton button = new AttachmentButton(mimeBodyPart);
                    HBoxDownloads.getChildren().add(button);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            AttachmentsLabel.setText("");
        }
    }

    private class AttachmentButton extends Button {
        private MimeBodyPart mimeBodyPart;
        private String downloadFilePath;

        public AttachmentButton(MimeBodyPart mimeBodyPart) throws MessagingException {
            this.mimeBodyPart = mimeBodyPart;
            this.setText(mimeBodyPart.getFileName());
            this.downloadFilePath = LOCATION_OF_DOWNLOADS + mimeBodyPart.getFileName();

            this.setOnAction(e -> downloadAttachment());
        }

        private void downloadAttachment() {
            // Color the attachment blue when clicked once for download and download it
            colorBlue();
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            mimeBodyPart.saveFile(downloadFilePath);
                            return null;
                        }
                    };
                }
            };
            service.restart();
            service.setOnSucceeded(e -> {
                // Color the attachment green when clicked again and open it
                colorGreen();
                this.setOnAction(e2 -> {
                    File file = new File(downloadFilePath);
                    // This desktop implementation is works on Windows, but not on Linux or Mac
                    Desktop desktop = Desktop.getDesktop();
                    if (file.exists()) {
                        try {
                            desktop.open(file);
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                });
            });
        }

        private void colorBlue() {
            this.setStyle("-fx-background-color: Blue");
        }

        private void colorGreen() {
            this.setStyle("-fx-background-color: Green");
        }
    }
}
