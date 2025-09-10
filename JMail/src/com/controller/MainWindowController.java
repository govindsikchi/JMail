package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import com.EmailManager;
import com.controller.services.MessageRendererService;
import com.model.EmailMessage;
import com.model.EmailTreeItem;
import com.model.SizeInteger;
import com.view.ViewFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    private MenuItem markUnreadMenuItem = new MenuItem("mark as unread");
    private MenuItem deleteMessageMenuItem = new MenuItem("delete message");
    private MenuItem showEmailDetailsMenuItem = new MenuItem("view details");

    @FXML
    private TreeView<String> EmailTreeView;

    @FXML
    private TableView<EmailMessage> EmailTableView;

    @FXML
    private TableColumn<EmailMessage, String> senderColumnId;

    @FXML
    private TableColumn<EmailMessage, String> subjectColumnId;

    @FXML
    private TableColumn<EmailMessage, String> recipientColumnId;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeColumnId;

    @FXML
    private TableColumn<EmailMessage, Date> dateColumnId;

    @FXML
    private WebView EmailWebView;

    private MessageRendererService messageRendererService;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void addAccountAction() {
        System.out.println("Add Account Button Clicked...!");
        viewFactory.showLoginWindow();
    }

    @FXML
    void composeMessageAction() {
        System.out.println("Compose Message Button Clicked...!");
        viewFactory.showComposeMessageWindow();
    }

    @FXML
    void optionsAction() {
        System.out.println("Clicked on the Options Button...!");
        viewFactory.showOptionsWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailTreeView();
        setUpEmailTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();

        setUpContextMenu();
    }

    private void setUpContextMenu() {
        markUnreadMenuItem.setOnAction(event -> {
            emailManager.setUnread();
        });
        deleteMessageMenuItem.setOnAction(event -> {
            emailManager.deleteSelectedMessage();
            EmailWebView.getEngine().loadContent("");
        });
        showEmailDetailsMenuItem.setOnAction(event -> {
            viewFactory.showEmailDetailsWindow();
        });
    }

    private void setUpMessageSelection() {
        EmailTableView.setOnMouseClicked(event -> {
            EmailMessage emailMessage = EmailTableView.getSelectionModel().getSelectedItem();
            if(emailMessage != null) {
                emailManager.setSelectedMessage(emailMessage);
                if(!emailMessage.isRead()) {
                    emailManager.setRead();
                }
                emailManager.setSelectedMessage(emailMessage);
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart(); // use of restart() method because start() method can only be used once
            }
        });
    }

    private void setUpMessageRendererService() {
        messageRendererService = new MessageRendererService(EmailWebView.getEngine());
    }

    private void setUpBoldRows() {
        EmailTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> emailMessageTableView) {
                return new TableRow<EmailMessage>() {
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            if(item.isRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setUpFolderSelection() {
        EmailTreeView.setOnMouseClicked(e -> {
            EmailTreeItem<String> item = (EmailTreeItem<String>) EmailTreeView.getSelectionModel().getSelectedItem();
            if(item != null) {
                emailManager.setSelectedFolder(item);
                EmailTableView.setItems(item.getEmailMessages());
            }
        });
    }

    private void setUpEmailTableView() {
        senderColumnId.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("sender"));
        subjectColumnId.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("subject"));
        recipientColumnId.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("recipient"));
        sizeColumnId.setCellValueFactory(new PropertyValueFactory<EmailMessage, SizeInteger>("size"));
        dateColumnId.setCellValueFactory(new PropertyValueFactory<EmailMessage, Date>("date"));

        EmailTableView.setContextMenu(new ContextMenu(markUnreadMenuItem, deleteMessageMenuItem, showEmailDetailsMenuItem));
    }

    private void setUpEmailTreeView() {
        EmailTreeView.setRoot(emailManager.getFoldersRoot());
        EmailTreeView.setShowRoot(false);
    }
}
