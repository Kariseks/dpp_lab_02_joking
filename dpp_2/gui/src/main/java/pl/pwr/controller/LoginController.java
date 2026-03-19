package pl.pwr.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.pwr.model.DAO.UserDAO;
import pl.pwr.model.entities.User;


public class LoginController {

    public TextField usernameField;
    public TextField passwordField;
    private boolean success = false;
    User user;
    private boolean normalUser;

    @FXML
    private void action_login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserDAO userDAO = new UserDAO();
        user = userDAO.getUser(username);

        if(user == null){
            success = false;
            showAlert();
            return;}

        //todo hashownie hasla
        if (user.passwordHash().equals(password)) {
            this.success = true;
            normalUser = user.id() == 1 ? false : true; //zakladam ze id 1 ma admin
            // Zamykamy okno logowania
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        }
        else {
            success = false;
            showAlert();
        }
    }
    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd autoryzacji");
        alert.setHeaderText("Nieprawidłowe dane");
        alert.setContentText("Sprawdź czy login i hasło są poprawne.");

        alert.showAndWait();
    }

    public boolean isSuccess() {return success;}
    public User getUser() {return user;}
    public boolean isNormalUser() { return normalUser; }
}
