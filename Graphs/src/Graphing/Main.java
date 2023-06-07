package Graphing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;




public class Main extends Application implements Dimensions {


    @Override
    public void start(Stage primaryStage) throws Exception{



        Parent root = FXMLLoader.load(getClass().getResource("GraphScene.fxml"));

        primaryStage.setTitle("Graph");

        Scene scene = new Scene(root, screen_width, screen_height);

        scene.getStylesheets().add("sample.css");
        primaryStage.setMaxHeight(screen_height);
        primaryStage.setMinHeight(screen_height);
        primaryStage.setMaxWidth(screen_width);
        primaryStage.setMinWidth(screen_width);
        primaryStage.setMaximized(false);
        scene.setFill(Color.WHITE);
        primaryStage.setScene(scene);

        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }

}
