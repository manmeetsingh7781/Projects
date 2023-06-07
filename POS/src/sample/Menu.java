package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;


public class Menu {
    // Settings varaibles
    private String background_color, text_color;
    private int font_size;

    // FXML Declarations
    @FXML private ListView<Node> items_display, transcitionList;
    @FXML private Label totalAmount, taxesAmount, numberOfItems, feesAmount, currentUser, todaysDate;
    @FXML private BorderPane borderPane;
    @FXML private GridPane gridPane, keyPadGrid;
    @FXML private TextField keyPadText;
    @FXML private VBox mainPanel;
    @FXML private TilePane items_panel;
    @FXML private Button cancel_trans,timesItem , orders_trans, hold_trans, bkSpaceBtn, rand_amount1, rand_amount2, rand_amount3, receipts, rand_amount4, clearBtn, logoutBtn, calculatorBtn, appsBtn ,profilesBtn ;
    @FXML private ImageView  settingsIco, bkSpaceIco, logoutIco, calculatorIco, appsIco , profilesIco, receiptsIco;
    @FXML private ScrollPane itemScrollPane;



    // functional declarations
    private BigDecimal  total_taxes = BigDecimal.ZERO, total_money = BigDecimal.ZERO, total_fees = BigDecimal.ZERO, money_order_fee;
    private int itemCount = 0, itemMultiplier = 1;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyy");
    private Random random;
    private boolean show_each_tax, age_confirmed;
    private DecimalFormat format;
    private Label emptyCartLabel;
    private String money_order_fee_calculation;
    private Functions myFunctions;
    private ObservableList<Node> all_departments_items, in_store_items, other_items;
    private String current_room;

    public void initialize() {
        all_departments_items = FXCollections.observableArrayList();
        in_store_items = FXCollections.observableArrayList();
        other_items = FXCollections.observableArrayList();

        myFunctions = new Functions();
        random = new Random();
        emptyCartLabel = new Label();
        format = new DecimalFormat("0.00");

        load_settings();
        setUpStyles();
        loadImages();
        initKeyPad();
        disable_trans_buttons();
        showTransLabel();
        clearKeyPadText();


        current_room = Main.getData_path() + "/Settings/all_departments.json";
        clearTrans();

        transcitionList.setEditable(true);
        transcitionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        items_display.getSelectionModel().selectFirst();
        items_panel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        itemScrollPane.setBackground(javafx.scene.layout.Background.EMPTY);
        gridPane.add(emptyCartLabel, 0, 0);



        items_display.setOnMouseClicked(mouseEvent -> {
            int selected_item = items_display.getSelectionModel().getSelectedIndex();
            items_panel.getChildren().removeAll();
            items_panel.getChildren().clear();
            switch (selected_item) {
                case 0:
                    items_panel.getChildren().addAll(all_departments_items);
                    current_room = Main.getData_path() + "/Settings/all_departments.json";
                    break;
                case 1:
                    items_panel.getChildren().addAll(in_store_items);
                    current_room = Main.getData_path() + "/Settings/in_stores.json";
                    break;
                case 2:
                    items_panel.getChildren().addAll(other_items);
                    current_room = Main.getData_path() + "/Settings/other_items.json";
                    break;
            }
        });

    }

    // Loads the settings of System
    private void load_settings() {
        if (Main.getSetup_settings().exists()) {
            try {
                Object parser = new JSONParser().parse(new FileReader(Main.getSetup_settings()));
                String user = ((JSONObject) parser).get("user").toString();
                currentUser.setText(user);
                todaysDate.setText(formatter.format(LocalDate.now()));
                font_size = parseInt(((JSONObject) parser).get("font_size").toString());
                background_color = ((JSONObject) parser).get("background_color").toString();
                text_color = ((JSONObject) parser).get("text_color").toString();
                show_each_tax = ((JSONObject) parser).get("show_each_tax").equals(true);
                money_order_fee =  myFunctions.getDecimalForm(((JSONObject) parser).get("money-order-fee").toString());
                money_order_fee_calculation =(((JSONObject) parser).get("money-order-fee-calculation").toString());
            } catch (Exception err) {
                FXMLLoader loader = new FXMLLoader();
                Parent root = null;
                try {
                    root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PopupController controller = loader.getController();
                controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
                controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
                showPopup(root, new ButtonType("OK"), null, null);
            }
        }
    }


    // If there is no items in list then disable all the buttons
    void disable_trans_buttons(){
        hold_trans.setDisable(true);
        cancel_trans.setDisable(true);

    }

    // If there is no items in list then disable all the buttons
    void enable_trans_buttons(){
        hold_trans.setDisable(false);
        cancel_trans.setDisable(false);
    }



    // Loads the Main items on System
    private ObservableList<Node> getLoadedPanel(String file_path) {
        ObservableList<Node> nodes = FXCollections.observableArrayList();
        try {
            FileReader reader = new FileReader(file_path);
            JSONParser parser = new JSONParser();
            JSONArray jsonObject = (JSONArray) parser.parse(reader);
            for (Object i : jsonObject) {
                JSONObject eachJsonObject = (JSONObject) i;
                myButton each_btn = new myButton();

                each_btn.setDefaultFees(eachJsonObject.get("fees").toString());
                each_btn.setDefaultPrice(eachJsonObject.get("price").toString());
                each_btn.setName(eachJsonObject.get("name").toString());
                each_btn.setCount(eachJsonObject.get("count").toString());
                each_btn.setAge_restriction(Boolean.parseBoolean(eachJsonObject.get("age_restriction").toString()));
                each_btn.setTaxable(Boolean.parseBoolean(eachJsonObject.get("tax").toString()));
                each_btn.setAge(Integer.parseInt(eachJsonObject.get("age").toString()));


                each_btn.setOnAction(actionEvent -> action(each_btn));

                each_btn.setWrapText(true);

                // settings width
                each_btn.setPrefWidth(120);
                each_btn.setMinWidth(120);
                each_btn.setMaxWidth(120);

                // setting height
                each_btn.setMinHeight(80);
                each_btn.setPrefHeight(80);
                each_btn.setMaxHeight(80);

                each_btn.setStyle("-fx-font-size: " + font_size);
                each_btn.getStyleClass().add("item_buttons_panel");

                myFunctions.updateButtonApperance(each_btn, font_size);
                each_btn.setTextAlignment(TextAlignment.CENTER);
                nodes.add(each_btn);
            }
        } catch (Exception err) {
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PopupController controller = loader.getController();
            controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
            controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
            showPopup(root, new ButtonType("OK"), null, null);
        }
        return nodes;
    }

    private boolean ageConformation(int minimum_age){
        LocalDate date = LocalDate.now().minusYears(minimum_age);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Popup.fxml").openStream());
            PopupController controller = fxmlLoader.getController();

            controller.setMainText(("Is the Customer born before this Date: " + formatter.format(date) + "?"), "-fx-text-fill:orange; -fx-font-size: " + font_size);
            controller.setTitle("Please confirm the age before you proceed","-fx-text-fill:"+text_color+"; -fx-font-size: " + font_size);
            age_confirmed = showPopup(root, new ButtonType("Confirm"), new ButtonType("Decline"), null) == 1;
            return  age_confirmed;
        }catch (Exception err){
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PopupController controller = loader.getController();
            controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
            controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
            showPopup(root, new ButtonType("OK"), null, null);
        }
        return false;
    }


    private void transactionSuccess(String change){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Popup.fxml").openStream());
            PopupController controller = fxmlLoader.getController();
            controller.setMainText(change, "-fx-text-fill:orange; -fx-font-size: " + font_size);
            controller.setTitle("Transaction Completed", "-fx-text-fill:"+text_color+"; -fx-font-size: " + font_size);
            showPopup(root, new ButtonType("OKAY"), new ButtonType("PRINT RECEIPT"), null);
        }catch (Exception err){
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PopupController controller = loader.getController();
            controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
            controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
            showPopup(root, new ButtonType("OK"), null, null);
        }
    }


    private void loadCalculator(){
        try{
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("MiniCalc.fxml").openStream());
            root.getStylesheets().add("sample/Calc.css");
            showPopup(root, new ButtonType("CLOSE"), null, null);
        }catch (Exception err){
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PopupController controller = loader.getController();
            controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
            controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
            showPopup(root, new ButtonType("OK"), null, null);
        }
    }




    private void showItemsOptions(myButton button, myLabel itemInList){

        if(transcitionList.getSelectionModel().getSelectedItem() != null) {

            try {
                String item_price,
                        item_tax, item_fees = itemInList.getFees(), item_name = itemInList.getName();

                if(itemInList.getCustomPrice() != null){
                    item_price  = itemInList.getCustomPrice();
                }else{
                    item_price = itemInList.getPrice();
                }

                if(itemInList.getTimes() > 1){
                    item_price = itemInList.getPrice();
                }

                item_tax = format.format(Double.parseDouble(itemInList.getTax().replace(",", "")));

                if(item_tax.equals("0")){
                    item_tax = "0.00";
                }

                item_price = myFunctions.getFormatedValue(item_price);

                button.setItemInListName(itemInList.getName());

                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("ItemOptions.fxml").openStream());
                ItemOptions optionsController = fxmlLoader.getController();

                String finalItem_tax = item_tax;
                String finalItem_price = item_price;
                class SetupOptions{

                    Label itemHeading = new Label("More About item: " + item_name);
                    Label itemPrice = new Label(myFunctions.getFormatedValue(finalItem_price));
                    Label itemTaxLabel = new Label(myFunctions.getFormatedValue(finalItem_tax));

                    // add item Tax label too
                    Label itemavaiability = new Label(button.getCount());
                    Label itemFees = new Label(myFunctions.getFormatedValue(item_fees));
                    Label itemTax = new Label(button.isTaxable() + "");
                    Label itemAgeNumber = new Label(button.getAge() + "");
                    Label itemAgeRes = new Label("" + button.isAge_restriction());
                    Button openINVBTN = new Button("Open in inventory");
                    int number_of_items = myFunctions.checkItemCounts(transcitionList, itemInList);
                    Label itemCounter = new Label("Adjust item in Cart: " + number_of_items);

                    SetupOptions() {

                        optionsController.getRemoveAmount().setDisable(number_of_items <= 0);

                        setupComponents();

                        updateButtons();
                    }



                    void updateButtons(){
                        boolean containsItem = myFunctions.containsItem(transcitionList, itemInList);
                        optionsController.getRemoveAmount().setDisable(!containsItem);
                        optionsController.getAddAmount().setDisable(!containsItem);
                        optionsController.getRemoveAll().setDisable(itemInList.getTimes() < 2);
                        if (!button.getCount().equals("unavailable")) {
                            int stockCount = Integer.parseInt(button.getCount());
                            optionsController.getAddAmount().setDisable(stockCount <= 0);
                        }
                    }

                    void updateInfo(myLabel res){



                        // update cart count
                        int ItemInCart = myFunctions.checkItemCounts(transcitionList, itemInList);
                        itemCounter.setText("Adjust item in Cart: " + ItemInCart + "");

                        itemavaiability.setText(button.getCount() + "");
                        itemHeading.setText("More About item: " + itemInList.getName());
                        String price;
                        if(itemInList.getCustomPrice() == null || itemInList.getTimes() > 1){
                            price = res.getPrice();
                        }else{
                            price = res.getCustomPrice();
                        }

                        itemPrice.setText(myFunctions.getFormatedValue(price));
                        itemFees.setText(myFunctions.getFormatedValue(res.getFees()));
                        itemTaxLabel.setText(myFunctions.getFormatedValue(res.getTax()));

                        // updates components
                        setupComponents();

                        updateButtons();
                    }

                    void setupComponents(){

                        optionsController.setNumberOfItems(itemCounter);
                        optionsController.setItemTaxPrice(itemTaxLabel);
                        optionsController.setItemPrice(itemPrice);
                        optionsController.setItemCount(itemavaiability);
                        optionsController.setItemFee(itemFees);
                        optionsController.setItemTax(itemTax);
                        optionsController.setItemAgeRes(itemAgeRes);
                        optionsController.setitemAgeNum(itemAgeNumber);
                        optionsController.setItemLabel(itemHeading);


                        for (Node n : optionsController.getMain_container().getChildren()) {
                            n.setStyle("-fx-font-size: " + font_size + "; -fx-text-fill: " + text_color + ";");
                        }

                        optionsController.getMain_container().getStylesheets().add("sample/menu.css");
                        optionsController.setOpenINV(openINVBTN);
                    }

                }


                SetupOptions options = new SetupOptions();


                optionsController.getAddAmount().setOnAction(mouseEvent -> {
                    int index = transcitionList.getSelectionModel().getSelectedIndex();
                    if(index >= 0 && transcitionList.getItems().get(index) instanceof HBox && show_each_tax){
                        index++;
                    }
                    optionsController.getRemoveAmount().setDisable(index < 0);
                    options.updateInfo(add_removeItem(index, itemInList, button, '+'));
                });

                optionsController.getRemoveAmount().setOnAction(mouseEvent -> {
                    int index = transcitionList.getSelectionModel().getSelectedIndex();
                    if(index >= 0 && transcitionList.getItems().get(index) instanceof HBox && show_each_tax){
                        index--;
                    }
                    optionsController.getAddAmount().setDisable(index < 0);
                    options.updateInfo(add_removeItem(index, itemInList, button, '-'));
                });

                optionsController.getRemoveAll().setOnAction(e->{
                    int index = transcitionList.getSelectionModel().getSelectedIndex();
                    if(index >= 0 && transcitionList.getItems().get(index) instanceof HBox && show_each_tax){
                        index--;
                    }
                    options.updateInfo(add_removeItem(index, itemInList, button, 'X'));

                });

                showPopup(root, new ButtonType("OK"), null, null);
            } catch (Exception err) {
                err.getLocalizedMessage();
                err.printStackTrace();
            }
        }
    }



    private int showPopup(Parent root, ButtonType btn1, ButtonType btn2, ButtonType btn3){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        try{
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().setStyle("-fx-background-color: "+background_color+"; -fx-border-color: grey; -fx-border-radius: 10");
            ButtonBar buttonBar = (ButtonBar)dialog.getDialogPane().lookup(".button-bar");
            buttonBar.setStyle("-fx-font-size: "+font_size+";" + "-fx-background-color: "+background_color+";");
            dialog.initStyle(StageStyle.UNDECORATED);

            if(btn1 != null) {
                dialog.getDialogPane().getButtonTypes().add(btn1);
                buttonBar.getButtons().get(0).setStyle("-fx-background-color: green; -fx-text-fill: " + text_color + "; -fx-cursor: hand;");
            }
            if(btn2 != null) {
                dialog.getDialogPane().getButtonTypes().add(btn2);
                buttonBar.getButtons().get(1).setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-cursor: hand;");
            }

            if(btn3 != null) {
                dialog.getDialogPane().getButtonTypes().add(btn3);
                buttonBar.getButtons().get(2).setStyle("-fx-background-color: red; -fx-text-fill: " + text_color + "; -fx-cursor: hand;");
            }

            Optional<ButtonType> res = dialog.showAndWait();
            if(res.isPresent()){
                if(btn1 != null && res.get() == btn1) {
                    return 1;
                }else if(btn2 != null &&res.get() == btn2){
                    return 2;
                }else if(btn3 != null &&res.get() == btn3){
                    return 3;
                }else{
                    return -1;
                }
            }
        }catch (Exception err){
            err.printStackTrace();
        }
        return -1;
    }


    private myButton getItem(String name){

        String [] paths = {"/Settings/all_departments.json", "/Settings/in_stores.json", "/Settings/other_items.json"};

        // first we will check in All departments for the item
        if(getItemHelper(Main.getData_path() + paths[0], name) != null){
            return getItemHelper(Main.getData_path() + paths[0], name);
        }else{
            if(getItemHelper(Main.getData_path() + paths[1], name) != null){
                return getItemHelper(Main.getData_path() + paths[1], name);
            }else{
                return getItemHelper(Main.getData_path() + paths[2], name);
            }
        }
    }


    private myButton getItemHelper(String path, String name){

        for(Node n: getLoadedPanel(path)){
            myButton btn = (myButton) n;
            if(btn.getName().equals(name)){
                return btn;
            }
        }
        return null;

    }

    private JSONObject getItemFromFile(String name){

        String [] paths = {"/Settings/all_departments.json", "/Settings/in_stores.json", "/Settings/other_items.json"};


        // first we will check in All departments for the item
        if(getItemHelperFromFile(Main.getData_path() + paths[0], name) != null){
            return getItemHelperFromFile(Main.getData_path() + paths[0], name);
        }else if(getItemHelperFromFile(Main.getData_path() + paths[1], name) != null){
                return getItemHelperFromFile(Main.getData_path() + paths[1], name);
        }else if(getItemHelperFromFile(Main.getData_path() + paths[2], name) != null){
            return getItemHelperFromFile(Main.getData_path() + paths[2], name);
        }else{
            return null;
        }
    }


    private JSONObject getItemHelperFromFile(String path, String name){
        FileReader reader;
        try {
            JSONParser parser = new JSONParser();
            reader = new FileReader(path);
            JSONArray jsonObject =  (JSONArray) parser.parse(reader);
            for (Object i : jsonObject) {
                JSONObject eachJsonObject = (JSONObject) i;
                String object_name = eachJsonObject.get("name").toString().toLowerCase();
                if(name.contains(" x ")){
                    name = name.split("x")[0].strip();
                }

                if (object_name.equals(name.toLowerCase())) {
                    return eachJsonObject;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    // Item buttons action listener
    @FXML private void action(myButton btn){
        // adds item to the transection list
        addItemToList(btn);
        if(total_money.doubleValue() <= 0 && transcitionList.getItems().size() == 0){
            disable_trans_buttons();
            showTransLabel();
        }else if(transcitionList.getItems().size() > 0  || total_money.doubleValue() > 0){
            enable_trans_buttons();
            emptyCartLabel.setText("");
        }

    }

    Node[] getItemFromList(ListView<Node> items, myButton button){
        Node[] data = new Node[2];
        for(int i = 0; i < items.getItems().size(); i++){
            Node eachItem = items.getItems().get(i);
            boolean stackPaneType = eachItem instanceof StackPane;
            if(stackPaneType) {
                String item_name = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(0)).getChildren().get(0)).getText();
                String item_price = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(1)).getChildren().get(0)).getText();
                if(item_name.equals(button.getName()) && (item_price.equals(button.getOutTheDoorPrice()) || item_price.equals(button.getPrice()))){
                    data[0] = eachItem;
                }
            }
            if(eachItem instanceof HBox){
                data[1] = eachItem;
            }
        }
        return data;
    }


    // This function takes name of button user clicked on and adds the name of it to the transcation list
    private void addItemToList(myButton button) {
        emptyCartLabel.setText("");
        enable_trans_buttons();
        boolean wentThrought = false, passed = false;
        BigDecimal tax, amount, final_amount, fees;
        String item_amount = "";

        StackPane transItem = new StackPane();

        Label item_amount_label = new Label();
        HBox itemNameBox = new HBox(), itemAmountBox = new HBox();
        String itemCost = button.getDefaultPrice();
        String item_name = button.getText();
        myLabel itemInList = new myLabel("");

        // Setting Item name in list on left and price on the right side
        itemNameBox.setAlignment(Pos.CENTER_LEFT);
        itemAmountBox.setAlignment(Pos.CENTER_RIGHT);


        // check if the item is already in the list then just increase the quantity



        // ===============================ABOUT ITEM PRICE ========================================
        /*
         * This block of statements is all about if the item have price in database or not
         * if its in data base then simply collect and process it
         * if not then it will check several conditions and get input from the keypad and then process the data and feeses based on the item
         *
         * */


        // add amount if the item already exists with same out the door price

        // this means we have item in list


        if (itemCost != null) {
                String text = keyPadText.getText().strip();

                // if the itemCost is saved means items with tax and non tax
                if (!itemCost.equals("ask")) {
                    if (text.length() > 0) {
                        item_amount = multiplyItems(button);
                        String[] parts = text.split("x");
                        if (parts.length > 1) {
                            itemCost = myFunctions.getFormatedValue(parts[1]);
                            itemInList.setPrice(item_amount);
                            itemInList.setCustomPrice(itemCost);
                        } else {
                            if (text.endsWith("x")) {
                                itemInList.setPrice(item_amount);
                                itemInList.setCustomPrice(itemCost);
                            } else {
                                itemInList.setPrice(itemCost);
                                itemInList.setCustomPrice(item_amount);
                            }
                        }
                    } else {
                        item_amount = button.getDefaultPrice();
                        itemInList.setPrice(itemCost);
                        if (item_amount.equals(itemCost)) {
                            itemInList.setCustomPrice(null);
                        } else
                            itemInList.setCustomPrice(item_amount);
                    }

                    passed = true;
                }

                // if the itemCost is ask
                if (itemCost.equals("ask")) {
                    String error_style = "-fx-background-color: " + background_color + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size*1.5  + ";-fx-border-color: red; -fx-border-radius: 5;-fx-border-width: 2;";
                    String remove_error_style = "-fx-background-color: " + background_color + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size*1.5  + ";-fx-border-color: grey; -fx-border-radius: 5;-fx-border-width: 2;";

                    if (text.isEmpty() || text.isBlank()) {
                        myFunctions.showAnimation(keyPadText, error_style, remove_error_style);
                    } else {
                        if (text.endsWith("x") || text.equals("0.00")) {
                            myFunctions.showAnimation(keyPadText, error_style, remove_error_style);
                        } else {
                            item_amount = multiplyItems(button);
                            String[] parts = text.split("x");

                            if (button.getName().equals("money-order")) {
                                String moneyOrderFee = myFunctions.getDecimalForm(money_order_fee.toString()).toString();
                                itemInList.setEachItemFees(moneyOrderFee);
                                if (money_order_fee_calculation.equals("automatic")) {
                                    String itemFiltered = item_amount != null ? item_amount.replace("$", "").replace(",", "") : moneyOrderFee;
                                    BigDecimal money = myFunctions.getDecimalForm(itemFiltered);
                                    if (money.doubleValue() > 500) {
                                        money = new BigDecimal(money.toString().replace(",", ""));
                                        moneyOrderFee = myFunctions.getFormatedValue(new BigDecimal(("" + (money.doubleValue() / 500) * myFunctions.getDecimalForm(money_order_fee.toString()).doubleValue() * 10)).toString());
                                        itemInList.setEachItemFees(moneyOrderFee);
                                    }
                                }
                                button.setDefaultFees(moneyOrderFee);
                            }

                            if (parts.length > 1) {
                                itemCost = myFunctions.getFormatedValue(parts[1]);
                                itemInList.setPrice(item_amount);
                                itemInList.setCustomPrice(itemCost);
                            } else {
                                itemCost = item_amount;
                                if (text.endsWith("x")) {
                                    itemInList.setPrice(item_amount);
                                    itemInList.setCustomPrice(itemCost);
                                } else {
                                    itemInList.setPrice(item_amount);
                                    itemInList.setCustomPrice(null);
                                }
                            }
                            passed = true;

                        }
                    }
                }
            }


        /*

                passed if we got item amount and fees to continue from here

        */

            if (passed) {
                if (button.isAge_restriction()) {
                    if (age_confirmed) {
                        wentThrought = true;
                    } else {
                        int age = button.getAge();
                        if (age > 0) {
                            wentThrought = ageConformation(age);
                        } else {
                            wentThrought = true;
                        }
                    }
                } else {
                    wentThrought = true;
                }
            }


        //============================================================================

        /*
         * If the Item has been went through and all the data formation is correct it will process it to next stage
         * it will add the item on the screen, calculate the taxes and fees and add it to the transection panel
         * */



        if (wentThrought && item_amount != null && button.getName() != null) {
            itemInList.setPrice(itemInList.getPrice().replace("$", ""));

            if (itemMultiplier > 1) {
                itemInList.setName(item_name + " x " + itemMultiplier + "(" + itemCost + ")");

            } else {
                itemInList.setName(item_name);
            }


            // money-order automaticaly calculates the fees so we dont have to worry about it here
            fees = myFunctions.getDecimalForm(myFunctions.getFormatedValue(button.getDefaultFees().replace(",", "")));

            if (!button.getName().equals("money-order")) {
                itemInList.setEachItemFees(format.format(Double.parseDouble(fees.toString().replace("," , ""))));
                fees = fees.multiply(BigDecimal.valueOf(itemMultiplier));
                itemInList.setFees(format.format(Double.parseDouble(fees.toString().replace("," , ""))));
            } else {
                fees = myFunctions.getDecimalForm(myFunctions.getFormatedValue(button.getDefaultFees().replace(",", "")));
                itemInList.setFees(fees.toString());
            }

            if (button.isTaxable()) {
                itemInList.setEachItemTax(format.format(Double.parseDouble(myFunctions.getDecimalForm(myFunctions.getFormatedValue((itemCost)).replace(",", "")).multiply(new BigDecimal("0.08")).toString().replace(",",""))));
                tax = myFunctions.getDecimalForm(myFunctions.getFormatedValue((item_amount)).replace(",", "")).multiply(new BigDecimal("0.08"));
                itemInList.setTax(format.format(Double.parseDouble(tax.toString().replace(",",""))));

            } else {
                // if item is non taxable
                tax = BigDecimal.valueOf(0);
                itemInList.setTax("0.00");
                itemInList.setEachItemTax("0.00");
            }


            button.setDefaultTax(itemInList.getTax());
            button.setTaxPrice(itemInList.getTax());
            itemInList.setTimes(itemMultiplier);
            itemNameBox.getChildren().add(itemInList);


            // converting each value to BigDecimal class so we dont lose any leading numbers
            amount = myFunctions.getDecimalForm((myFunctions.getFormatedValue(item_amount)).replace(",", ""));
            button.setTempPriceHolder(amount.toString());

            // adding price, tax, fees
            final_amount = BigDecimal.valueOf(amount.doubleValue()).add(fees).add(tax);
            button.setOutTheDoorPrice(final_amount.toString());



            // Rounding the final amount to the nearest tenth
            final_amount = new BigDecimal(format.format(Math.round(Double.parseDouble(String.valueOf(final_amount.doubleValue())) * 100.0) / 100.0));



            button.setPrice(itemInList.getPrice());
            button.setCustomPrice(itemInList.getCustomPrice());
            button.setFees(itemInList.getFees());
            button.setCustomFees(itemInList.getCustomPrice());

            item_amount_label.setAlignment(Pos.CENTER_RIGHT);
            item_amount_label.setText(myFunctions.getFormatedValue(format.format(final_amount.doubleValue())));
            itemInList.setTotalPrice(myFunctions.getFormatedValue(final_amount.toString()));


            // updates the total money of the item and add it to final amount
            updateTransInfo(final_amount, fees, tax);

            // updates the transection labels
            updateTransLabels();


            transItem.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    showItemsOptions(button, itemInList);
                }
            });


            if (!button.getCount().equals("unavailable")) {
                int count = Integer.parseInt(button.getCount());
                count -= itemMultiplier;
                button.setCount(count + "");
                myFunctions.updateButtonApperance(button, font_size);
            }

            itemMultiplier = 1;
            clearKeyPadText();
            generateRandMoney();
            transcitionList.scrollTo(transcitionList.getItems().size() - 1);


            itemAmountBox.getChildren().add(item_amount_label);
            transItem.getChildren().addAll(itemNameBox, itemAmountBox);
            transcitionList.getItems().add(transItem);

            item_amount_label.setStyle("-fx-font-weight: bolder; -fx-font-size: " + font_size  + ";");
            itemNameBox.setStyle("-fx-font-weight: bolder; -fx-font-size: " + font_size  + ";");
            String starting_animation, end_animation;
            starting_animation = " -fx-background-color: orange;";
            end_animation = "      -fx-background-color: transparent;";
            myFunctions.showAnimation(transItem, starting_animation, end_animation);

            if (show_each_tax) {
                HBox tax_box = new HBox();
                Label tax_label = new Label("" + myFunctions.getFormatedValue(itemInList.getTax()));

                tax_label.setAlignment(Pos.CENTER_RIGHT);
                tax_box.setAlignment(Pos.CENTER_RIGHT);

                tax_box.setStyle("-fx-border-color: black; -fx-border-width: 0 0 3 0; -fx-border-style: dotted;");
                tax_label.setStyle("-fx-text-fill: green");
                tax_box.getChildren().addAll(tax_label);
                transcitionList.getItems().add(tax_box);
            }
        }
    }



    private void updateTransInfo(BigDecimal final_amount, BigDecimal fees, BigDecimal tax){
        itemCount  += itemMultiplier;
        total_fees  = total_fees.add(BigDecimal.valueOf(Math.round(fees.doubleValue() * 100.0) / 100.0));
        total_taxes = total_taxes.add(tax);
        total_money = total_money.add(BigDecimal.valueOf(Math.round(final_amount.doubleValue() * 100.0) / 100.0));

//        total_money = myFunctions.calculateTotal(transcition_list, total_money, itemCount);
    }

    private void updateTransLabels(){

        // settings transaction labels
        numberOfItems.setText("Items: " + itemCount);
        taxesAmount.setText("Tax: " + myFunctions.getFormatedValue(format.format(total_taxes)));
        feesAmount.setText("Fees: " + myFunctions.getFormatedValue(format.format(total_fees)));
        totalAmount.setText("Total: " + myFunctions.getFormatedValue(format.format(total_money)));
    }




    private myLabel add_removeItem(int index, myLabel itemInList, myButton button, char operation){

        if(index != -1) {
            itemInList.setTotalPrice(itemInList.getTotalPrice().replace(",", ""));
            String name;
            int button_multi = 1;
            BigDecimal outTheDoorAmount;
            BigDecimal item_fee, item_tax, item_price, newTax, newFees, currentitemPrice;
            String price = itemInList.getPrice(), tax = itemInList.getTax(), fees = itemInList.getFees();

            if(itemInList.getCustomPrice() != null){
                price = itemInList.getCustomPrice();
            }else{
                itemInList.setCustomPrice(itemInList.getPrice());
            }

            String eachFees = itemInList.getEachItemFees();
            if(eachFees == null){
                eachFees = itemInList.getFees();
            }

            item_fee = myFunctions.getDecimalForm(eachFees.replace("," ,""));


            button_multi = itemInList.getTimes();

            if(operation == '-'){

                item_price = myFunctions.getDecimalForm(price);
                if(button_multi == 1){
                    item_tax = myFunctions.getDecimalForm(itemInList.getTax().replace("," ,""));
                    outTheDoorAmount = item_price.add(item_fee).add(item_tax);
                    transcitionList.getItems().remove(transcitionList.getItems().get(index));

                    if (show_each_tax) {
                        transcitionList.getItems().remove(transcitionList.getItems().get(index));
                    }

                }else{

                    if(button_multi <= 2){
                        name = itemInList.getName().split(" x ")[0];
                        button_multi--;
                    }else{
                        name = itemInList.getName().replaceFirst(button_multi + "", (--button_multi) + "");
                    }


                    String tax_price;

                    if(button.isTaxable()){
                        tax_price = format.format(Double.parseDouble(item_price.multiply(new BigDecimal(format.format(Double.parseDouble("0.08")))).toString()));
                    }else{
                        tax_price = "0.00";
                    }

                    item_tax = myFunctions.getDecimalForm(tax_price.replace("," ,""));

                    outTheDoorAmount = item_price.add(item_fee).add(item_tax);


                    newTax = myFunctions.getDecimalForm(tax).subtract(item_tax);
                    newFees = myFunctions.getDecimalForm(fees).subtract(item_fee);

                    currentitemPrice = new BigDecimal(itemInList.getTotalPrice()).subtract(outTheDoorAmount);
                    updateItemInList(index, name, myFunctions.getFormatedValue(currentitemPrice.toString()), itemInList);
                    if(show_each_tax) {
                        updateItemInList(index + 1, "", myFunctions.getFormatedValue(newTax.toString()), itemInList);
                    }

                    button.setOutTheDoorPrice(myFunctions.getFormatedValue(itemInList.getTotalPrice().replace(",","")));
                    itemInList.setName(name);
                    itemInList.setPrice(myFunctions.getFormatedValue(currentitemPrice.toString()));
                    itemInList.setTax(format.format(Double.parseDouble(newTax.toString().replace(",", ""))));
                    itemInList.setFees(format.format(Double.parseDouble(newFees.toString().replace(",", ""))));
                    itemInList.setTotalPrice(myFunctions.getFormatedValue(currentitemPrice.toString()));
                    itemInList.setTimes(button_multi);
                }



                if (!button.getCount().equals("unavailable")) {
                    int count = Integer.parseInt(button.getCount());
                    count++;
                    button.setCount(count + "");
                    myFunctions.updateButtonApperance(button, font_size);
                }


                total_money = myFunctions.getDecimalForm("" + (total_money.doubleValue() - outTheDoorAmount.doubleValue()));
                total_fees = total_fees.subtract(item_fee);
                total_taxes = total_taxes.subtract(item_tax);
                itemCount--;


            }else if (operation == '+'){
                item_price = myFunctions.getDecimalForm(price);

                String tax_price;

                if(button.isTaxable()){
                    tax_price = format.format(Double.parseDouble(item_price.multiply(new BigDecimal(format.format(Double.parseDouble("0.08")))).toString()));
                }else{
                    tax_price = "0.00";
                }

                item_tax = myFunctions.getDecimalForm(tax_price.replace("," ,""));
                outTheDoorAmount = item_price.add(item_fee).add(item_tax);
                newFees = myFunctions.getDecimalForm(fees).add(item_fee);
                newTax = myFunctions.getDecimalForm(tax).add(item_tax);
                currentitemPrice = new BigDecimal(itemInList.getTotalPrice()).add(outTheDoorAmount);


                if(itemInList.getName().contains(" x ")){
                    name = itemInList.getName().replaceFirst(button_multi + "", (++button_multi) + "");
                }else{
                    button_multi++;
                    name = itemInList.getName() + " x " + button_multi + "("+item_price.toString()+")";
                }

                button.setOutTheDoorPrice(itemInList.getTotalPrice());
                itemInList.setPrice(myFunctions.getFormatedValue(currentitemPrice.toString()));
                updateItemInList(index, name, myFunctions.getFormatedValue(currentitemPrice.toString()), itemInList);
                if(show_each_tax) {
                    updateItemInList(index + 1, "", myFunctions.getFormatedValue(newTax.toString()), itemInList);
                }


                itemInList.setName(name);
                button.setItemInListName(name);
                itemInList.setTax(myFunctions.getFormatedValue(newTax.toString().replace(",", "")));
                itemInList.setFees(myFunctions.getFormatedValue(newFees.toString().replace(",", "")));
                itemInList.setTotalPrice(myFunctions.getFormatedValue(currentitemPrice.toString()));
                itemInList.setTimes(button_multi);


                if (!button.getCount().equals("unavailable")) {
                    int count = Integer.parseInt(button.getCount());
                    count--;
                    button.setCount(count + "");
                    myFunctions.updateButtonApperance(button, font_size);
                }

                total_money = myFunctions.getDecimalForm("" + (total_money.doubleValue() + outTheDoorAmount.doubleValue()));
                total_fees = total_fees.add(item_fee);
                total_taxes = total_taxes.add(item_tax);
                itemCount++;
            }else {

                // this block of statements is for 'Remove All' items

                item_price = myFunctions.getDecimalForm(itemInList.getPrice());
                item_fee = myFunctions.getDecimalForm(itemInList.getFees().replace("," ,""));
                item_tax = myFunctions.getDecimalForm(itemInList.getTax().replace(",", ""));

                outTheDoorAmount = item_price.add(item_fee).add(item_tax);
                button_multi++;

                itemInList.setTimes(button_multi);
                
                transcitionList.getItems().remove(transcitionList.getItems().get(index));
                if (show_each_tax) {
                    transcitionList.getItems().remove(transcitionList.getItems().get(index));
                }

                total_money = myFunctions.getDecimalForm("" + (total_money.doubleValue() - outTheDoorAmount.doubleValue()));

                total_fees = total_fees.subtract(item_fee);
                total_taxes = total_taxes.subtract(item_tax);

                int currentCount = 1;
                if(itemInList.getName().contains(" x ") && !button.getCount().equals("unavailable") ){
                    currentCount  = Integer.parseInt(itemInList.getName().split("x")[1].split("\\(")[0].strip());
                }

                if (!button.getCount().equals("unavailable")) {
                    int count = Integer.parseInt(button.getCount());
                    count+=currentCount;
                    button.setCount(count + "");
                    myFunctions.updateButtonApperance(button, font_size);
                }
                itemCount-=currentCount;
            }
        }

        if(itemCount <= 0  ||  total_money.doubleValue() <= 0){
            clearTrans();
        }


        generateRandMoney();
        showTransLabel();
        updateTransLabels();
        return itemInList;
    }

    private void updateItemInList(int index, String newName, String newPrice, myLabel itemInList){

        // stackpane have the item price and name of the item
        Node all_contents = transcitionList.getItems().get(index);
        if (all_contents instanceof StackPane) {
            StackPane pane = (StackPane) all_contents;
            Label itemName = (Label) ((HBox) pane.getChildren().get(0)).getChildren().get(0);
            Label itemPrice = (Label) ((HBox) pane.getChildren().get(1)).getChildren().get(0);
            itemInList.setTotalPrice(myFunctions.getFormatedValue(itemInList.getTotalPrice().replace(",","")));
            if (itemInList.getName().equals(itemName.getText()) && itemInList.getTotalPrice().equals(myFunctions.getFormatedValue(itemPrice.getText()))) {
                itemName.setText(newName);
                itemPrice.setText(newPrice);
            }
        }

        if (all_contents instanceof HBox && show_each_tax) {
            HBox pane = (HBox) all_contents;
            Label taxPrice = (Label) pane.getChildren().get(0);
            if (myFunctions.getFormatedValue(itemInList.getTax()).equals(myFunctions.getFormatedValue(taxPrice.getText()))) {
                taxPrice.setText(newPrice);
            }
        }

    }

    // this function multiplies the item for ex: 0.55 * 2 = 1.00 it will return this format
    private String multiplyItems(myButton button){
        String item_amount;
        BigDecimal times , money = null;
        String keyboard_val = keyPadText.getText().strip();
        if(keyboard_val.endsWith("x")){
            BigInteger n = BigInteger.valueOf(parseInt(myFunctions.filterKeyPad(keyboard_val)));
            itemMultiplier = n.intValue();
            money = myFunctions.getDecimalForm(myFunctions.getFormatedValue(button.getDefaultPrice()).replace(",",""));
        }else {

            if (keyboard_val.contains("x")) {
                String money_set = myFunctions.filterKeyPad(keyboard_val.split("x")[1]);

                // Using BigDecimal so we don't lose any leading zeros
                money = myFunctions.getDecimalForm(myFunctions.getFormatedValue(money_set).replace(",",""));

                String[] value = keyboard_val.split("x");
                if (value.length > 1) {
                    times = BigDecimal.valueOf(parseInt(value[0].strip()));
                    itemMultiplier = times.intValue();
                }
            }
        }

        if(money != null) {

            if (!button.getCount().equals("unavailable")) {
                int currentCount = Integer.parseInt(button.getCount());
                if (itemMultiplier > currentCount) {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
                        PopupController controller = loader.getController();
                        controller.setTitle("Item information Alert", "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
                        controller.setMainText("Only " + currentCount + " items left in stock, requested were " + itemMultiplier +", what would you like to do?", "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
                        int res = showPopup(root, new ButtonType("Add MAX"),  new ButtonType("Continue"), new ButtonType("Cancel"));
                        if(res == 1){
                            itemMultiplier = currentCount;
                        }else if(res == 3){
                            return null;
                        }
                    }catch ( Exception err){
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = null;
                        try {
                            root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PopupController controller = loader.getController();
                        controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
                        controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
                        showPopup(root, new ButtonType("OK"), null, null);
                    }

                }
            }
            item_amount = "$" + (money.multiply(new BigDecimal(itemMultiplier)));
        }else{
            item_amount = myFunctions.getFormatedValue(keyboard_val);
        }
        return item_amount;
    }


    @FXML private void clearKeyPadText(){
        keyPadText.clear();
        clearBtn.setDisable(true);
    }


    // this function deletes the last digit of keypad
    @FXML private void backSpaceText() {

        if (keyPadText.getText().length() > 0) {
            BigInteger keyPadVal;
            int secondPart;
            if(keyPadText.getText().contains("x")){
                String part;
                String[] parts = keyPadText.getText().strip().split("x");
                part = parts[0].strip()
                        .replace(".", "").replace(" ", "")
                        .replace(",", "");
                if (part.endsWith("x") || part.endsWith("0") || part.endsWith(" x ") || part.endsWith("$")) {
                    keyPadText.setText("$"+myFunctions.getFormatedValue(keyPadText.getText().split("x")[0].strip().replace("x", "").replace("$", "")));
                }else {
                    part = part.strip();

                    if(parts.length > 1) {
                        String anotherpart = myFunctions.filterKeyPad(parts[1]);
                        if(!anotherpart.equals("")) {
                            secondPart = parseInt(anotherpart);
                            secondPart /= 10;
                            if (secondPart <= 0) {
                                anotherpart = null;
                            } else {
                                anotherpart = myFunctions.getDecimalForm(myFunctions.getFormatedValue(secondPart + "").replace(",", "")).toString();
                            }
                        }else{
                            anotherpart = null;
                        }
                        if(anotherpart !=null) {
                            keyPadText.setText(part + " x $" + anotherpart);
                        }else {
                            keyPadText.setText("$"+myFunctions.getFormatedValue(part));
                        }

                    }else{
                        keyPadText.setText("$"+myFunctions.getFormatedValue(keyPadText.getText().split("x")[0].strip().replace("x", "")));
                    }

                }
            }else {
                // if keypad does not have multiplication in it simply clear the last digit
                keyPadVal = new BigInteger(myFunctions.filterKeyPad(keyPadText.getText()));
                keyPadVal = keyPadVal.divide(BigInteger.valueOf(10));
                keyPadText.setText("$" + myFunctions.getFormatedValue(String.valueOf(keyPadVal)));
            }
        }
        if(keyPadText.getText().equals("$0.00")) {
            clearKeyPadText();
        }
    }




    // when user cancles the transcetions it should go back to default
    @FXML private void clearTrans(){
        itemCount = 0;
        total_fees = BigDecimal.ZERO;
        total_taxes = BigDecimal.ZERO;
        total_money = BigDecimal.ZERO;
        age_confirmed = false;
        totalAmount.setText("Total: 0.00");
        feesAmount.setText("Fees: 0.00");
        taxesAmount.setText("Taxes: 0.00");
        numberOfItems.setText("Items: 0");
        transcitionList.getItems().clear();

        items_panel.getChildren().removeAll();
        items_panel.getChildren().clear();

        all_departments_items = getLoadedPanel(Main.getData_path() + "/Settings/all_departments.json");
        in_store_items = getLoadedPanel(Main.getData_path() + "/Settings/in_stores.json");
        other_items = getLoadedPanel(Main.getData_path() + "/Settings/other_items.json");

        if(current_room.endsWith("all_departments.json")) {
            items_panel.getChildren().addAll(all_departments_items);
        }else if(current_room.endsWith("in_stores.json")){
            items_panel.getChildren().addAll(in_store_items);
        }else if(current_room.endsWith("other_items.json")){
            items_panel.getChildren().addAll(other_items);
        }

        rand_amount1.setText("$5");
        rand_amount2.setText("$10");
        rand_amount3.setText("$15");
        rand_amount4.setText("$20");
        showTransLabel();
        disable_trans_buttons();
    }


    // if there is no items in the list then show the label "NO ITEMS"
    private void showTransLabel(){
        if(total_money.doubleValue() <= 0 || transcitionList.getItems().size() <= 0 || itemCount <= 0){
            emptyCartLabel.setText("No Items");
            emptyCartLabel.setStyle("-fx-font-size: 36;-fx-padding: 0 0 0 100;");
            transcitionList.setStyle("-fx-background-color: " + background_color  + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size+ ";-fx-border-color: grey; -fx-border-radius: 5;-fx-border-width: 2;");
            disable_trans_buttons();
        }
    }



    // initialize the styles of the application
    private void setUpStyles(){
        itemScrollPane.setStyle("-fx-background-color: " + background_color);
        items_panel.setStyle("-fx-background-color: " + background_color);

        itemScrollPane.setFitToWidth(true);
        itemScrollPane.setFitToHeight(true);


        keyPadText.setStyle("-fx-background-color: " + background_color  + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size+ ";-fx-border-color: grey; -fx-border-radius: 5; -fx-border-width: 2;");
        gridPane.setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        mainPanel.setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        borderPane.setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        transcitionList.setStyle("-fx-background-color: " + background_color  + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size+ ";-fx-border-color: grey; -fx-border-radius: 5;-fx-border-width: 2;");
        numberOfItems.setStyle("-fx-padding: 0 0 0 20; -fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        taxesAmount.setStyle("-fx-padding: 0 0 0 20; -fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        feesAmount.setStyle("-fx-padding: 0 0 0 20; -fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);

        totalAmount.setStyle("-fx-padding: 0 0 0 20; -fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);


        hold_trans.setStyle("-fx-font-size: " + font_size + ";-fx-cursor: hand;");
        cancel_trans.setStyle("-fx-font-size: " + font_size + ";-fx-cursor: hand;");
        orders_trans.setStyle("-fx-font-size: " + font_size + ";-fx-cursor: hand;");
        clearBtn.setStyle("-fx-font-size: " + font_size + ";-fx-cursor: hand;-fx-background-color: red; -fx-text-fill: white;");
        bkSpaceBtn.setStyle("-fx-font-size: " + font_size + ";-fx-cursor: hand;-fx-background-color: grey;");

        calculatorBtn.setStyle("-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        appsBtn.setStyle("-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        profilesBtn.setStyle("-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        receipts.setStyle("-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        logoutBtn.setStyle("-fx-border-color: grey;-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        currentUser.setStyle("-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        todaysDate.setStyle("-fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);

    }


    // loads the System image icons
    private void loadImages(){
        myFunctions.attach_Image(settingsIco, "Data/Images/Settings_icon.png");
        myFunctions.attach_Image(bkSpaceIco, "Data/Images/bkSpaceIco.png");
        myFunctions.attach_Image(calculatorIco, "Data/Images/calc_icon.png");
        myFunctions.attach_Image(appsIco, "Data/Images/apps_icon.jpg");
        myFunctions.attach_Image(profilesIco, "Data/Images/profiles_icon.png");
        myFunctions.attach_Image(logoutIco, "Data/Images/logoutIco.png");
        myFunctions.attach_Image(receiptsIco, "Data/Images/reciept_icon.png");
    }




    // this function initialize the keypad and attach events to it
    private void initKeyPad() {

        bkSpaceBtn.setOnAction(actionEvent -> backSpaceText());
        ObservableList<Node> key_pad_btns = keyPadGrid.getChildrenUnmodifiable();
        generateRandMoney();

        bkSpaceBtn.setMaxWidth(Double.MAX_VALUE);
        bkSpaceBtn.setMaxHeight(Double.MAX_VALUE);

        calculatorBtn.setOnAction(mouseEvent-> loadCalculator());


        bkSpaceBtn.setPrefWidth(80);
        clearBtn.setPrefWidth(80);

        clearBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // i=2 where keypad btns starts from
        for (int i = 3; i < key_pad_btns.size(); i++) {

            Button each_btn = (Button) key_pad_btns.get(i);


            // attaching keyPad events to the text field

            if(i < key_pad_btns.size()-4) {
                // -11 because just to avoid adding event to last four random amount buttons
                each_btn.setOnAction(actionEvent -> getKeys(each_btn));
            }

            if(each_btn.getAccessibleText() != null){
                if(each_btn.getAccessibleText().equals("cash"))
                {
                    each_btn.setOnAction(actionEvent -> cash_Item(each_btn));
                }

                if(each_btn.getAccessibleText().equals("system_function"))
                {
                    each_btn.setOnAction(actionEvent -> system_functions(each_btn));
                }
            }
            each_btn.setStyle("-fx-cursor:hand; -fx-font-size: " + font_size +"; -fx-text-fill: " + text_color+";");
            each_btn.setWrapText(true);
        }

        timesItem.setOnAction(actionEvent -> {
            if(!keyPadText.getText().contains("x") && keyPadText.getText().length() > 0) {
                keyPadText.setText(Integer.parseInt(myFunctions.filterKeyPad(keyPadText.getText())) + " x ");
            }
        });
    }




    private void system_functions(Button button){
        System.out.println("System Functions: " + button.getText());
    }



    private void cash_Item(Button button){
        BigDecimal inputDouble = null;
        if(total_money.doubleValue() <= 0){
            String error_style = "-fx-background-color: " + background_color  + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size * 1.2 + ";-fx-border-color: red; -fx-border-radius: 5;-fx-border-width: 2;";
            String remove_error_style = "-fx-background-color: " + background_color  + "; -fx-text-fill:" + text_color + "; -fx-font-size: " + font_size * 1.2+ ";-fx-border-color: grey; -fx-border-radius: 5;-fx-border-width: 2;";
            myFunctions.showAnimation(transcitionList, error_style, remove_error_style);
            if(keyPadText.getText().isEmpty() || keyPadText.getText().isBlank()) {
                myFunctions.showAnimation(keyPadText, error_style, remove_error_style);
            }

        }else{
            boolean success  = false;
            Label user_amout = null;
            String type = button.getText();

            try {

                if ("Cash".equals(type)) {
                    if (keyPadText.getText().isBlank() || keyPadText.getText().isEmpty()) {
                        success = true;
                        transactionSuccess("Change Due: $0.00");
                    } else {
                        inputDouble = new BigDecimal(myFunctions.getFormatedValue(keyPadText.getText()).replace(",", ""));
                        user_amout = new Label("-"+ inputDouble);
                    }
                    clearKeyPadText();
                }else {
                    inputDouble = new BigDecimal(myFunctions.filterKeyPad(type));
                    user_amout = new Label("-" + myFunctions.getFormatedValue("" + inputDouble.doubleValue() * 10));
                }

                if(inputDouble != null) {
                    HBox amount_box = new HBox();
                    amount_box.setAlignment(Pos.CENTER_RIGHT);
                    user_amout.setStyle("-fx-text-fill: red;-fx-font-size: " + font_size+";-fx-font-weight: bolder;");
                    amount_box.getChildren().add(user_amout);
                    transcitionList.getItems().add(amount_box);
                    success = Calculate(inputDouble, total_money);
                }
            }catch (Exception err){
                // catch errors if amount is higher then expected
                FXMLLoader loader = new FXMLLoader();
                Parent root = null;
                try {
                    root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PopupController controller = loader.getController();
                controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
                controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
                showPopup(root, new ButtonType("OK"), null, null);
            }

            if(success){
                for(Object node: transcitionList.getItems()){
                    boolean stackPaneType = node instanceof StackPane;
                    if(stackPaneType) {
                        String item_name = ((Label) ((HBox) ((StackPane) node).getChildren().get(0)).getChildren().get(0)).getText();
                        JSONObject item_btn = getItemFromFile(item_name);
                        if (item_btn != null &&!item_btn.get("count").equals("unavailable")) {
                            int count = 1;
                            if(item_name.contains(" x ")){
                                count = Integer.parseInt(item_name.split("x")[1].split("\\(")[0].strip());
                            }
                            int thisItemCount = Integer.parseInt(item_btn.get("count").toString());
                            thisItemCount-= count;
                            if(thisItemCount < 0){
                                thisItemCount = 0;
                            }
                            updateItem(item_btn.get("name").toString(), "count", thisItemCount + "");
                        }
                    }
                }
                clearTrans();
            }
        }
    }


    private boolean Calculate(BigDecimal customerAmount, BigDecimal amountNeeded){
        BigDecimal result;
        if(customerAmount.doubleValue() < amountNeeded.doubleValue()){
            total_money =  myFunctions.getDecimalForm(""+ (amountNeeded.doubleValue()-customerAmount.doubleValue()));
            totalAmount.setText("Customer Owns: " + myFunctions.getFormatedValue(total_money.toString()));
            generateRandMoney();
            return false;
        }else{
            result =  myFunctions.getDecimalForm(""+ (customerAmount.doubleValue()-amountNeeded.doubleValue()));
            transactionSuccess("Change Due: " + myFunctions.getFormatedValue(result.toString()));
            total_money = BigDecimal.ZERO;
            return true;
        }
    }



    private void generateRandMoney(){
        ArrayList<Integer> rands = new ArrayList<>();
        BigInteger current_money = new BigInteger(String.valueOf((int)total_money.doubleValue() + 1));
        if(current_money.intValueExact() < MAX_VALUE) {
            if (current_money.intValueExact() > 20) {
                while (rands.size() != 4) {
                    int r = (random.nextInt(5) + current_money.intValueExact());
                    if (!rands.contains(r)) {
                        rands.add(r);
                    }
                }
                rand_amount1.setText("$" + myFunctions.getFormatedValue(rands.get(0) * 100 + "").replace(".00", ""));
                rand_amount2.setText("$" + myFunctions.getFormatedValue(rands.get(1) * 100 + "").replace(".00", ""));
                rand_amount3.setText("$" + myFunctions.getFormatedValue(rands.get(2) * 100 + "").replace(".00", ""));
                rand_amount4.setText("$" + myFunctions.getFormatedValue(rands.get(3) * 100 + "").replace(".00", ""));

            } else {
                rand_amount1.setText("$5");
                rand_amount2.setText("$10");
                rand_amount3.setText("$15");
                rand_amount4.setText("$20");
            }
        }
    }

    // this function gets the text of keypad buttons and sets the text to textField
    private void getKeys(Button each_btn){
        // Generating Random Numbers
        if(clearBtn.isDisable()){
            clearBtn.setDisable(false);
        }

        generateRandMoney();
        String number = keyPadText.getText() + each_btn.getText();
//        if(number.length() > )
        try {
            if (number.contains("x")) {
                String[] parts = number.split("x");
                itemMultiplier = BigInteger.valueOf(Integer.parseInt(parts[0].strip())).intValueExact();
                keyPadText.setText(itemMultiplier + " x $"+  myFunctions.getFormatedValue(parts[1]));
            }else {
                keyPadText.setText("$"+ myFunctions.getFormatedValue(number));
            }
        }catch (Exception err) {
            // catch errors if the data type in textField is NAN
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResourceAsStream("Popup.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PopupController controller = loader.getController();
            controller.setTitle(err.getLocalizedMessage(), "-fx-text-fill: orange; -fx-font-size:"+font_size+";");
            controller.setMainText(err.getMessage(), "-fx-text-fill: "+ text_color+"; -fx-font-size:"+font_size+";");
            showPopup(root, new ButtonType("OK"), null, null);
        }
    }




    private String getItemLocationHelper(String path, String name) {
        try {
            FileReader reader = new FileReader(path);
            JSONParser parser = new JSONParser();
            JSONArray jsonObject = (JSONArray) parser.parse(reader);
            for (Object i : jsonObject) {
                JSONObject eachJsonObject = (JSONObject) i;
                myButton each_btn = new myButton();
                each_btn.setName(eachJsonObject.get("name").toString());
                if (each_btn.getName().equals(name)) {
                    return path;
                }
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getItemLocation(String name){
        String [] paths = {"/Settings/all_departments.json", "/Settings/in_stores.json", "/Settings/other_items.json"};

        // first we will check in All departments for the item
        if(getItemLocationHelper(Main.getData_path() + paths[0], name) != null){
            return getItemLocationHelper(Main.getData_path() + paths[0], name);
        }else{
            if(getItemLocationHelper(Main.getData_path() + paths[1], name) != null){
                return getItemLocationHelper(Main.getData_path() + paths[1], name);
            }else{
                return getItemLocationHelper(Main.getData_path() + paths[2], name);
            }
        }
    }



    void updateItem(String item_name, String key, String value){
        JSONObject newObj = getItemFromFile(item_name);
        newObj.replace(key, value);
        editItemHelper(item_name, newObj);
    }

    void updateItem(String item_name, String key, boolean value){
        JSONObject newObj = getItemFromFile(item_name);
        newObj.replace(key, value);
        editItemHelper(item_name, newObj);
    }


    private void editItemHelper(String item_name, JSONObject newJson){
        try{
            String fileLocation = getItemLocation(item_name);
            FileReader reader = new FileReader(fileLocation);
            JSONParser parser = new JSONParser();
            JSONArray updatedJson = new JSONArray();
            JSONArray jsonObject = (JSONArray) parser.parse(reader);
            FileWriter writer = new FileWriter(fileLocation);
            for(Object ob: jsonObject) {
                JSONObject json = (JSONObject) ob;
                if (json.get("name").toString().equals(item_name)) {
                    json.remove(item_name);
                    updatedJson.add(newJson);
                } else {
                    updatedJson.add(json);
                }
            }
            writer.write(updatedJson.toJSONString());
            writer.flush();
        }catch (Exception err){
            err.printStackTrace();
        }

    }

}


// ========================== ROUGH WORK ===============
//    private int ItemsCounter(ListView<Object> listView, String item){
//       int counter = 0;
//        if(listView != null){
//            for(int i = 0; i < listView.getItems().size(); i++){
//                HBox box = ((HBox) ((StackPane)listView.getItems().get(i)).getChildren().get(0));
//                String label = ((Label) box.getChildren().get(0)).getText();
//                if(label.equals(item)){
//                    counter++;
//                }
//            }
//        }
//        return counter;
//    }
//    private void collapseItems(ListView<Object> listView, String item){
//        if(listView != null){
//            int count = ItemsCounter(listView, item);
//            if(count > 1){
//                System.out.println("Item has been repated more then once: " + count);
//            }
//        }
//    }


//    private String getFormatedValue(String number) {
//        number = filterKeyPad(number);
//        try {
//
//            BigInteger original = new BigInteger(number);
//            String final_text;
//            BigInteger cents, dollars;
////            if ((original.intValueExact() % 10) == 0 && original.intValueExact() > 10000) {
////                System.out.println("Ending with Zero: " + original.intValueExact() / 10);
////                BigInteger original2 = new BigInteger(original + "0");
////                return getFormatedValue(original2.toString());
////            }
//            if (original.longValueExact() < 100000) {
//                    cents = original.mod(BigInteger.valueOf(100));
//                    dollars = original.divide(BigInteger.valueOf(100));
//                    if (cents.longValueExact() < 10) {
//                        final_text = dollars + ".0" + cents;
//                    } else {
//                        final_text = dollars + "." + cents;
//                    }
//                } else {
//                    BigInteger leading_cents;
//                    cents = original.mod(BigInteger.valueOf(100000));
//                    dollars = original.divide(BigInteger.valueOf(100000));
//                    leading_cents = cents.mod(BigInteger.valueOf(100));
//                    if (leading_cents.longValueExact() < 10) {
//                        final_text = dollars + "," + cents.divide(BigInteger.valueOf(100)) + ".0" + leading_cents;
//                    } else {
//                        final_text = dollars + "," + cents.divide(BigInteger.valueOf(100)) + "." + leading_cents;
//                    }
//                }
//                return final_text;
//            } catch (Exception err) {
//                return "0.00";
//            }
//    }

//            // if keyPad have any entry
//            if(!(keyPadText.getText().isBlank() || keyPadText.getText().isEmpty())) {
//                amountText = new Label(getFormatedValue(keyPadText.getText()));
//
//                // if the itemCost is not saved and its taxable
//                if(itemCost == null){
//                    if(keyPadText.getText().isBlank() || keyPadText.getText().isEmpty()){
//                        setError(keyPadText);
//                    }
//                }else if (itemCost.equals("*") || itemCost.contains("*")) {
//                   // take values from textpad and put tax on it and then add it to the amount text
//                    item_amount = Double.parseDouble(amountText.getText());
//                    tax = Math.round(item_amount * 0.08);
//                    final_amount =  tax + item_amount;
//                    amountText = new Label(getFormatedValue((""+final_amount)));
//                }
//            }else if(itemCost != null && itemCost.length() > 1 &&  itemCost.endsWith("*") && (!item_name.getText().equals("non-taxable"))){
//
//                // if the item have its own price then put tax and add it to the amount text
//                 item_amount = Double.parseDouble(getFormatedValue(itemCost));
//                tax = Math.round(item_amount * 0.08);
//                 final_amount =  tax + item_amount;
//                amountText = new Label(getFormatedValue((""+final_amount)));
//
//            }else if((keyPadText.getText().isBlank() || keyPadText.getText().isEmpty()) &&  (itemCost == null || itemCost.equals("*"))) {
//                // if items required entry but entry is not filled
//                if(transcition_list.getItems().size() < 1) {
//                    showTransLabel();
//                    disable_trans_buttons();
//                }
//                setError(keyPadText);
//                wentThrought = false;
//            }else if(itemCost!=null && itemCost.length() > 1 && (!itemCost.contains("*"))){
//                // if item have its own price
//                item_amount = Double.parseDouble(getFormatedValue(itemCost));
//                tax = Math.round(item_amount * 0.08);
//                final_amount =  tax + item_amount;
//                amountText = new Label(getFormatedValue((""+final_amount)));
//            }
//            total_taxes += tax;
//            total_money += Double.parseDouble(amountText.getText());
//            total_money = Math.round(total_money);
//            totalAmount.setText("Total: "+total_money);
//            taxesAmount.setText("Taxes: "+total_taxes);
//
//            itemAmountBox.getChildren().add(amountText);
//
//            update_transition_info(transcition_list);


// ================================================= UPDATE SHOW MORE OPTIONS ===================================


//                        // update item left in stock
//                        if (!tempBtn.getCount().equals("unavailable")) {
//                            int itemInStock = Integer.parseInt(tempBtn.getCount());
//                            optionsController.getAddAmount().setDisable(itemInStock <= 0);
//                            itemAvaiability.setStyle("-fx-font-size: " + font_size + "; -fx-text-fill: " + text_color + ";");
//                            itemAvaiability.setText(itemInStock + "");
//                            optionsController.setItemCount(itemAvaiability);
//                            button.setCount(tempBtn.getCount());
//                            myFunctions.updateButtonApperance(button, font_size);
//                        }
//
//
//                        // update cart count
//                        int ItemInCart = myFunctions.checkItemCounts(transcitionList, tempBtn);
//                        optionsController.getRemoveAmount().setDisable(!myFunctions.containsItem(transcitionList, tempBtn));
//                        itemCounter.setStyle("-fx-font-size: " + font_size + "; -fx-text-fill: " + text_color + ";");
//                        itemCounter.setText("Adjust item in Cart: " + ItemInCart + "");
//                        optionsController.setNumberOfItems(itemCounter);


//========================== update Item in list
/*
* //        transcitionList.getItems().clear();
//        transcitionList.getItems().setAll(backup);
//
//        for(Node eachNode: transcitionList.getItems()){
//
//            // stackpane have the item price and name of the item
//            Node all_contents = transcitionList.getItems().get(index);
//            if(!isDone) {
//                if (all_contents instanceof StackPane) {
//                    StackPane pane = (StackPane) all_contents;
//                    Label itemName = (Label) ((HBox) pane.getChildren().get(0)).getChildren().get(0);
//                    Label itemPrice = (Label) ((HBox) pane.getChildren().get(1)).getChildren().get(0);
//                    if (itemInList.getName().equals(itemName.getText()) && itemInList.getTotalPrice().equals(itemPrice.getText())) {
//                        itemName.setText(newName);
//                        itemPrice.setText(newPrice);
//                        if(!show_each_tax){
//                            isDone = true;
//                        }
//                    }
//                }
//
//                if (all_contents instanceof HBox && show_each_tax) {
//                    HBox pane = (HBox) all_contents;
//                    Label taxPrice = (Label) pane.getChildren().get(0);
//                    if (itemInList.getTax().equals(taxPrice.getText())) {
//                        taxPrice.setText(newPrice);
//                        isDone = true;
//                    }
//                }
//            }
//            backup.add(eachNode);
//        }
//        transcitionList.getItems().clear();
//        transcitionList.getItems().setAll(backup);
* */
