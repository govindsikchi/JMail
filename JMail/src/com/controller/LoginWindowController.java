package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.EmailManager;
import com.controller.services.LoginService;
import com.model.EmailAccount;
import com.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController implements Initializable {

    @FXML
    private Label ErrorField;

    @FXML
    private TextField EmailField;

    @FXML
    private PasswordField PasswordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void LoginAction() {
        System.out.println("Login Button Clicked...!");
        if(AllFieldsAreValid()) {
            EmailAccount emailAccount = new EmailAccount(EmailField.getText(), PasswordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event -> {
                EmailLoginResult emailLoginResult = loginService.getValue();
                switch (emailLoginResult) {
                    case SUCCESS:
                        System.out.println("Login Success: " + emailAccount);
                        if(!viewFactory.isMainViewInitialized()) {
                            viewFactory.showMainWindow();
                        }
                        Stage stage = (Stage) ErrorField.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        System.out.println("Invalid Credentials");
                        return;
                    case FAILED_BY_NETWORK_ERROR:
                        System.out.println("Network Error");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        System.out.println("Unexpected Error");
                        return;
                    default:
                }
            });
        }
    }

    private boolean AllFieldsAreValid() {
        if(EmailField.getText().isEmpty()) {
            ErrorField.setText("Please fill Email Address Field...!");
            return false;
        }
        if(PasswordField.getText().isEmpty()) {
            ErrorField.setText("Please enter Password...!");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailField.setText("Err404o404R@gmail.com");
        PasswordField.setText("404Error@5thGmail");
    }
}

// Name this different than it's fxml file name. (append 'controller' word in name)