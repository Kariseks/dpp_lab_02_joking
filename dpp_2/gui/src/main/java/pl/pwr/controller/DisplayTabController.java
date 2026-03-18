package pl.pwr.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import pl.pwr.model.entities.Joke;

import java.util.List;

public class DisplayTabController {
    public ListView jokesDispTabListView;
    public Label authorDispTabLbl;
    public Label ratitingDispTabLbl;
    public Label dateDispTabLbl;
    public Label tagsDispTabLbl;
    public Label dispCountDispTabLbl;
    public Label commentsCountDispTabLbl;
    public Label stateDispTabLbl;
    public TextArea jokeTxtDispTabLbl;
    public ComboBox dateDispTabComBOx;
    public ComboBox raitingDispTabComBox;
    public ComboBox sortDispTabComBox;
    public ListView commentsDispTabListView;
    public TextArea commentDispTabTxtArea;
    private MainWindowController mainController;
    void setMainController(MainWindowController mainController) {this.mainController = mainController;}

    public void action_reportJokeDispTabBtn(ActionEvent actionEvent) {
        mainController.switchTab(MainWindowController.TabE.SEARCH);}

    public void action_addCommentDispTabBtn(ActionEvent actionEvent) {
    }

    public void action_resetDispTabBtn(ActionEvent actionEvent) {
    }


    public void displayJokes(List<Joke> jokeList, Joke currJ){

    }
}
