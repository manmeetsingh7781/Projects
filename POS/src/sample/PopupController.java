package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PopupController {

    @FXML private Label title2, title;
    @FXML private GridPane box;



    public void setTitle(String myTitle, String style){
        title.setText(myTitle);
        title.setStyle(style.strip());
    }

    public void setMainText(String text, String style){
        title2.setText(text);
        title2.setStyle(style.strip());
    }

    public void addNode(Node node){
        box.getChildren().add(node);
    }


}
