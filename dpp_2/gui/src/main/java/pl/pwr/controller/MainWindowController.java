package pl.pwr.controller;

import javafx.scene.control.Tab;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import pl.pwr.model.entities.Joke;

import java.util.List;

public class MainWindowController {

    public Tab adminTab;
    @FXML private SearchTabController searchTabContentController;
    @FXML private DisplayTabController displayTabContentController;
    @FXML private AddJokeTabController addJokeTabContentController;
    @FXML private HistoryTabController historyTabContentController;
    @FXML private StatisticTabController statisticTabContentController;

    @FXML private TabPane mainTabPane;

    @FXML private Tab searchTab;
    @FXML private Tab displayTab;
    @FXML private Tab addJokeTab;
    @FXML private Tab historyTab;
    @FXML private Tab statisticTab;


    private Joke selectedJoke;
    //------------------------------------------------------------------------------------------------------------------
    private int userId;
    private String username;
    private boolean isNormalUser;
    private void setUser(int id, String username, boolean isNormalUser) {
        this.userId = id;
        this.username = username;
        this.isNormalUser = isNormalUser;
    }
    public int getUserId() {return userId;}
    public String getUsername() {return username;}
    public boolean isNormalUser() {return isNormalUser;}

    @FXML
    public void initialize() {
        if (searchTabContentController != null) {
            searchTabContentController.setMainController(this);
        }
        if (displayTabContentController != null) {
            displayTabContentController.setMainController(this);
        }
    }

    public void startup(int id, String username, boolean isNormalUser){
        setUser(id, username, isNormalUser);

        //albo usunac jezeli ma byc niewidoczna
        adminTab.setDisable(isNormalUser);
    }



    enum TabE
    {
        SEARCH(1),
        DISPLAY(2),
        ADD_JOKE_TAB(3),
        HISTORY(4),
        STATISTIC(5),
        ADMIN(6);

        TabE(int i) {
        }
    }

    public void switchTab(TabE tabN) {
        mainTabPane.getSelectionModel().select(tabN.ordinal());
    }
    public void chooseJoke(List<Joke> jList, Joke currJ) {
        selectedJoke = currJ;
        displayTabContentController.displayJokes(jList, currJ);
        switchTab(TabE.DISPLAY);
    }

}