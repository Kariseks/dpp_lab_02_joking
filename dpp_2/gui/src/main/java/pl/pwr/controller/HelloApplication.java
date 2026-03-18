package pl.pwr.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.pwr.model.DBConnector;
import pl.pwr.model.entities.User;

import java.io.IOException;



public class HelloApplication extends Application {

    User user;
    boolean isNormalUser;

    public void start(Stage stage) throws IOException {
        //TODO
        //ekran logownia i potem wybor okno user albo admin
        //tutaj wywolanie okna "login-view.fxml" zarzadzana przez LoginController class
        DBConnector.setupDatabase();
        if (!startLogin()) {
            System.out.println("Logowanie nieudane lub przerwane.");
            return;
        }

        //---------------------------------------------------
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Project JOKING");
        stage.setScene(scene);

        MainWindowController mCtrl = fxmlLoader.getController();

        mCtrl.startup(user.id(), user.username(),isNormalUser);

        stage.setWidth(1200);
        stage.setHeight(800);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.show();
    }

    private boolean startLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            LoginController loginCtrl = loader.getController();
            Stage loginStage = new Stage();

            loginStage.setTitle("Panel Logowania");
            loginStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            loginStage.setScene(loginScene);

            loginStage.showAndWait();

            if (loginCtrl.isSuccess()) {
                user = loginCtrl.getUser();
                isNormalUser = loginCtrl.isNormalUser();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

