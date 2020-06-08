package ui;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class Main {

    @FXML
    private AnchorPane paneConteiner;

    @FXML
    private FlowPane startPane;

    @FXML
    Start startPaneController;

    public void initialize(){

    }

    public void startKitchenTaskManager(){
        paneConteiner.getChildren().remove(startPane);
        try{
            //CatERing.getInstance().getUserManager().fakeLogin();
            Parent node = FXMLLoader.load(getClass().getResource("kitchenTask/kitchen-task-manager.fxml"));
            paneConteiner.getChildren().add(node);
            AnchorPane.setTopAnchor(node,0.0);
            AnchorPane.setBottomAnchor(node,0.0);
            AnchorPane.setLeftAnchor(node,0.0);
            AnchorPane.setRightAnchor(node,0.0);

        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
