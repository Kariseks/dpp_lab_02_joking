package pl.pwr.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.pwr.model.DAO.JokeDao;
import pl.pwr.model.DAO.TagDAO;
import pl.pwr.model.DAO.UserDAO;
import pl.pwr.model.entities.Joke;
import pl.pwr.model.entities.Tag;
import pl.pwr.model.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SearchTabController {
    public TableColumn<Joke, String> titleSrchTabCol;
    public TableColumn<Joke, Double> rateSrchTabCol;
    public TableColumn<Joke, String> authorSrchTabCol;
    public TableColumn<Joke, String> dateSrchTabCol;
    public TableColumn<Joke, String> tagsSrchTabCol;
    public TableView<Joke> jokeTableSrchTabTbl;
    public TextField titleSrchTabTextArea;
    public MenuButton tagsSrchTab;
    public ComboBox<String> userSrchTabComBox;
    public ComboBox<Integer> rateSrchTabComBox;
    public ComboBox<Integer> dateSrchTabComBox;
    private MainWindowController mainController;

    List<Joke> selectedJokes;

    public void setMainController(MainWindowController mainController) {this.mainController = mainController;}

    public void action_resetSrchTabBtn(ActionEvent actionEvent) {
        userSrchTabComBox.getSelectionModel().clearSelection();
        rateSrchTabComBox.getSelectionModel().clearSelection();
        dateSrchTabComBox.getSelectionModel().clearSelection();
        //todo sprawic zeby prompt text nie znikal, ponizszy sposob nie dziala
        /*
        userSrchTabComBox.setPromptText("Użytkownik");
        rateSrchTabComBox.setPromptText("Ocena");
        dateSrchTabComBox.setPromptText("Data");
        userSrchTabComBox.setValue(null);
        rateSrchTabComBox.setValue(null);
        dateSrchTabComBox.setValue(null);
         */
        for (MenuItem item : tagsSrchTab.getItems()) {
            if (item instanceof CheckMenuItem checkItem) {
                checkItem.setSelected(false);
            }
        }

        titleSrchTabTextArea.setText("");
    }

    public void action_searchSrchTabBtn(ActionEvent actionEvent) {

        var days = dateSrchTabComBox.getValue();
        var user = userSrchTabComBox.getValue();
        var rate = rateSrchTabComBox.getValue();
        var tags = getSelectedTags();
        var title =  titleSrchTabTextArea.getText();

        selectedJokes = new JokeDao().findJokes(tags, title, user, days);

        jokeTableSrchTabTbl.getItems().setAll(selectedJokes);

    }

    public void tableCick()
    {
        //todo przekaz liste do wyswietlaj i przej do tej tabeli
        mainController.switchTab(MainWindowController.TabE.DISPLAY);
    }

    @FXML
    public void initialize() {
        rateSrchTabComBox.getItems().addAll(1, 2, 3, 4, 5);
        dateSrchTabComBox.getItems().addAll(7, 30, 90);
        initTags();
        initUserComboBox();
        initColumns();
        initDoubleCLickTable();

    }

    private void initTags(){
        tagsSrchTab.getItems().clear();
        List<Tag> allTags = new TagDAO().getAllTags();
        for (Tag tag : allTags) {
            CheckMenuItem item = new CheckMenuItem(tag.name());
            tagsSrchTab.getItems().add(item);
        }
    }

    private void initUserComboBox() {
        var userList = new UserDAO().getAllUsers();
        ArrayList<String> userNames = new ArrayList<>();
        for (User user : userList) {userNames.add(user.username());}
        userSrchTabComBox.getItems().addAll(userNames);

    }

    public List<String> getSelectedTags() {
        List<String> selectedTags = new ArrayList<>();

        for (MenuItem item : tagsSrchTab.getItems()) {
            if (item instanceof CheckMenuItem checkItem) {
                if (checkItem.isSelected()) {
                    selectedTags.add(checkItem.getText());
                }
            }
        }

        return selectedTags;
    }

    public void initColumns(){
        titleSrchTabCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().title()));

        authorSrchTabCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().author()));

        dateSrchTabCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().creationDate()));

        tagsSrchTabCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().tags()));

        rateSrchTabCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().averageRating()));
    }
    private void initDoubleCLickTable() {
        jokeTableSrchTabTbl.setRowFactory(tv -> {
            TableRow<Joke> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Joke rowData = row.getItem();
                    mainController.chooseJoke(selectedJokes,rowData);
                }
            });
            return row;

        });
    }
}
