package com;

import com.controller.persistence.PersistenceAccess;
import com.controller.persistence.ValidAccount;
import com.controller.services.LoginService;
import com.model.EmailAccount;
import javafx.application.Application;
import javafx.stage.Stage;
import com.view.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();

    @Override
    public void start(Stage stage) throws Exception {
        // Parent parent = FXMLLoader.load(getClass().getResource("view/LoginWindow.fxml"));
        // Scene scene = new Scene(parent, 420, 365); // 420 - width, 365 - height

        // Parent parent = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        // Scene scene = new Scene(parent);
        // stage.setScene(scene);
        // stage.show();

        // Older Method - without persistence
        // ViewFactory viewFactory = new ViewFactory(new EmailManager());
        // viewFactory.showLoginWindow();

        // Check in Persistence
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if (validAccountList.size() > 0) {
            viewFactory.showMainWindow();
            for (ValidAccount validAccount : validAccountList) {
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<>();
        for (EmailAccount emailAccount : emailManager.getEmailAccounts()) {
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
    }
}
