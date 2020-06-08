package ui.kitchenTask;

import businesslogic.CatERing;
import javafx.fxml.FXML;

import java.awt.*;

public class KitchenTaskManager {

    @FXML
    Label userLabel;

    public void initialize(){
        String username =CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
        userLabel.setText(username);

        //TODO: bisognerebbe caricare anche l'elenco dei menu

    }

}
