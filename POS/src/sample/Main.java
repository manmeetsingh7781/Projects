package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;


public class Main extends Application {

    private Stage currentStage;
    private static Parent root;
    private static String data_path = System.getProperty("user.dir")+"/Data";
    private static File setup_settings = new File(data_path +"/Settings/Pos_sys_settings.json");

    @Override
    public void start(Stage primaryStage) throws Exception{
        setCurrentStage(primaryStage);
        if(setup_settings.exists()){
            load_pos_system();
        }else{
            load_setup();
        }
        getCurrentStage().show();
    }

    private void load_pos_system() throws Exception{
        setRoot(FXMLLoader.load(getClass().getResource("Menu.fxml")));
        // reads the name from settings
        Object parser = new JSONParser().parse(new FileReader(setup_settings));
        String username =  ((JSONObject) parser).get("user").toString();
        currentStage.setTitle(username+"'s POS System");
        int m_w = 1410, m_h = 1050;
        currentStage.setX(0);
        currentStage.setY(0);
        currentStage.setFullScreen(true);
        currentStage.setResizable(true);
        currentStage.setScene(new Scene(getRoot(), m_w, m_h));
    }

    // this function loads the setup window and once its done it will open a POS System window
    private void load_setup() throws Exception{
        setRoot(FXMLLoader.load(getClass().getResource("pos_setup.fxml")));
        Parent root = getRoot();
        TextField textField = (TextField) root.getChildrenUnmodifiable().get(1);
        root.getChildrenUnmodifiable().get(3).setOnMouseClicked(mouseEvent -> {
            try {
                if(setup_settings.createNewFile()){
                    JSONObject settingsObj = new JSONObject();
                    FileWriter writer = new FileWriter(setup_settings);
                    settingsObj.put("user", textField.getText());
                    settingsObj.put("font_size", "14");
                    settingsObj.put("background_color","black");
                    settingsObj.put("text_color","white");
                    settingsObj.put("show_each_tax", true);
                    settingsObj.put("money-order-fee", 1.00);
                    settingsObj.put("money-order-fee-calculation", "automatic");
                    writer.write(settingsObj.toJSONString());
                    writer.flush();
                    load_pos_system();
                }
            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setTitle("Unable to setup Account");
                error.setContentText("There is problem occured to create a system files, Please check if the file formate is correct");
                error.showAndWait();
            }
        });

        root.getChildrenUnmodifiable().get(2).setOnMouseClicked(mouseEvent -> getCurrentStage().close());

        String window_title = "Setup POS System";
        currentStage.setTitle(window_title);
        currentStage.setResizable(false);
        currentStage.setFullScreen(false);
        currentStage.setScene(new Scene(root, 300, 200));
    }

    private  Stage getCurrentStage() {
        return currentStage;
    }

    private void setCurrentStage(Stage stage) {
        currentStage = stage;
    }

    static Parent getRoot() {
        return root;
    }

    private void setRoot(Parent r) {
        root = r;
    }

    static File getSetup_settings() {
        return setup_settings;
    }

    static String getData_path() {
        return data_path;
    }

    public static void main(String[] args) {
        launch(args);
    }




}