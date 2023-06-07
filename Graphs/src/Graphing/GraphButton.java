package Graphing;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class GraphButton extends Button {

    private boolean graphed;

    public GraphButton(String text){
        super(text);
        super.setDisable(true);
        super.setCursor(Cursor.HAND);
        this.graphed = false;
    }

    public boolean isGraphed() {
        return graphed;
    }

    public void setGraphed(boolean graphed) {
        this.graphed = graphed;
    }
}
