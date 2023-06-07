package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.File;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;


public class Functions {



    String filterKeyPad(String textFieldString){
        return  textFieldString.replace(".", "").replace(" ", "")
                .replace(",", "").replace("x", "").replace("$", "").strip();
    }


    // this function returns the Double formated text like: 55 which really is 0.55 in the POS Systems
    String getFormatedValue(String text){
        String cents_st, amount;
        text = text.replace("$", "").replace(",", "").replace("x", "").replace(".","").replace(",","").strip();

        BigInteger original = new BigInteger( text);
        String f = "0,000";
        if(text.length() <= 5){
            f = "0";
        }
        DecimalFormat format = new DecimalFormat(f);
        BigInteger dollars = original.divide(BigInteger.valueOf(100));
        BigInteger cents = (original.remainder(BigInteger.valueOf(100)));
        if(cents.intValueExact() < 10){
            cents_st = ".0"+cents;
        }else{
            cents_st = "."+cents;
        }
        amount = (format.format(new BigInteger(dollars.toString()).doubleValue()) + "" + cents_st);
        return amount;
    }


    BigDecimal getDecimalForm(String number){
        DecimalFormat format = new DecimalFormat("0.00");
        return new BigDecimal(format.format(Double.parseDouble(number.replace(",", ""))));
    }

    // attach image to the image view through FXML
    void attach_Image(ImageView imgView, String imageLink){
        File file = new File(imageLink);
        Image img = new Image(file.toURI().toString());
        imgView.setImage(img);
    }


    // if something went wrong it will blink error to the keyPad to get user attention
    void showAnimation(Node node, String starting_animation, String end_animation) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), evt -> node.setStyle(starting_animation)),
                new KeyFrame(Duration.seconds(0.1), evt -> node.setStyle(end_animation)));
        timeline.setCycleCount(6);
        timeline.setDelay(Duration.ONE);
        timeline.play();
    }



    void updateButtonApperance(myButton btn, int font_size){

        if(!btn.getCount().equals("unavailable")){
            int count = Integer.parseInt(btn.getCount());
            String warning = " -fx-background-color: #fbc531;\n" +
                    "   -fx-text-fill: black;\n" +
                    "   -fx-text-weight: bolder;-fx-font-size: " +font_size+ ";\n" +
                    "    -fx-transition-time: 0.5;\n" +
                    "    -fx-cursor: hand;\n" +
                    "  -fx-border-style: solid;\n" +
                    "  -fx-border-radius: 5;\n" +
                    "  -fx-border-color: black;";
            if(count <= 0){
                    String stopStyle = (" -fx-background-color: #c23616;\n" +
                            "   -fx-text-fill: white;\n" +
                            "   -fx-text-weight: bolder;-fx-font-size: " +font_size+ ";\n" +
                            "    -fx-transition-time: 0.5;\n" +
                            "    -fx-cursor: hand;\n" +
                            "  -fx-border-style: solid;\n" +
                            "  -fx-border-radius: 5;\n" +
                            "  -fx-border-color: white;");
                    showAnimation(btn, warning, stopStyle);
                    btn.setDisable(true);
            }
            if(count <= 5 && count > 0){
                btn.setStyle(warning);
                btn.setDisable(false);
            }
            if(count > 5){
                btn.setStyle("-fx-font-size: " + font_size);
                btn.getStyleClass().add("item_buttons_panel");
            }
        }

    }


//    int getItemIndex(ListView<Node> items, myButton button){
//        for(int i = 0; i < items.getItems().size(); i++){
//            Object eachItem = items.getItems().get(i);
//            boolean stackPaneType = eachItem instanceof StackPane;
//            if(stackPaneType) {
//                String clickedOnItem_name = button.getName();
//                System.out.println("Item Clicked On is: " + clickedOnItem_name);
//                String item_name = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(0)).getChildren().get(0)).getText();
//                String item_price = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(1)).getChildren().get(0)).getText();
//
//                if(item_name.contains(" x ")) {
//                    String[] parts = item_name.split(" x ");
//                    item_name = parts[0];
//                    clickedOnItem_name = button.getName().split(" x ")[0];
//                }
//
//                if(item_name.equals(clickedOnItem_name) && item_price.equals(getFormatedValue(button.getOutTheDoorPrice()))){
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }



    int getItemIndex(ListView<Node> items, myLabel label){
        for(int i = 0; i < items.getItems().size(); i++){
            Object eachItem = items.getItems().get(i);
            boolean stackPaneType = eachItem instanceof StackPane;
            if(stackPaneType) {
                String clickedOnItem_name = label.getName();
                String item_name = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(0)).getChildren().get(0)).getText();
                String item_price = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(1)).getChildren().get(0)).getText();
                if(item_name.contains(" x ")) {
                    String[] parts = item_name.split(" x ");
                    item_name = parts[0];
                    clickedOnItem_name = label.getName().split(" x ")[0];
                }

                if(item_name.equals(clickedOnItem_name) && item_price.equals(getFormatedValue(label.getTotalPrice()))){
                    return i;
                }
            }
        }
        return -1;
    }



    int checkItemCounts(ListView<Node> items, myLabel label){
        int counter = 0;
        for (int i  = 0; i < items.getItems().size(); i++) {
            Object node = items.getItems().get(i);
            if(node instanceof StackPane) {
                String item_name = ((Label) ((HBox) ((StackPane) node).getChildren().get(0)).getChildren().get(0)).getText();
                String item_price = ((Label) ((HBox) ((StackPane) node).getChildren().get(1)).getChildren().get(0)).getText();
                if(item_name.contains(" x ")){
                    String[] parts = item_name.split(" x ");
                    item_name = parts[0];
                    if(item_name.equals(label.getName().split(" x ")[0]) && item_price.equals(getFormatedValue(label.getTotalPrice()))){
                        counter += Integer.parseInt(parts[1].split("\\(")[0]);
                    }
                }else if(item_name.equals(label.getName()) && item_price.equals(getFormatedValue(label.getTotalPrice()))){
                    counter++;
                }
            }
        }
       return counter;
    }

    boolean containsItem(ListView<Node> items, myLabel label){
        for(int i = 0; i < items.getItems().size(); i++){
            Object eachItem = items.getItems().get(i);
            if(eachItem instanceof StackPane) {
                String clickedOnItem_name = label.getName();
                String item_name = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(0)).getChildren().get(0)).getText();
                String item_price = ((Label) ((HBox) ((StackPane) eachItem).getChildren().get(1)).getChildren().get(0)).getText();
                if(item_name.contains(" x ")) {
                    String[] parts = item_name.split(" x ");
                    item_name = parts[0];
                    clickedOnItem_name = (label.getName().split(" x ")[0]);
                }
                if(item_name.equals(clickedOnItem_name) && item_price.equals(getFormatedValue(label.getTotalPrice().replace(",", "")))){
                    return true;
                }

            }
        }
        return false;
    }






//
//    BigDecimal calculateTotal(ListView<Object> items, BigDecimal total, int size) {
//
//        for (int i  = 0; i < size; i++) {
//            Object node = items.getItems().get(i);
//            if (node instanceof StackPane) {
//                BigDecimal price = new BigDecimal(((Label) ((HBox) ((StackPane) node).getChildren().get(1)).getChildren().get(0)).getText());
//                System.out.println("Item Price:  " + price.toString());
//                total = total.add(price);
//
//            }
//        }
//        return total;
//    }


}
