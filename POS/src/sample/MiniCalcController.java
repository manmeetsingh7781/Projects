package sample;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MiniCalcController {

    @FXML private GridPane keyPad, main_container;

    @FXML private TextField outputPad;

    @FXML private Button delete;


    public void setMain_containerStyle(String style){
        main_container.setStyle(style);
    }

    public void setBtnsStyle(String style){
        for(Node n: keyPad.getChildren()){
            Button btn = (Button) n;
            btn.setStyle(style);
        }
    }

    public void clearOutput(){
        outputPad.clear();
    }

    public void initialize(){
        InitKeyPad(keyPad);

    }

    private void InitKeyPad(GridPane pane){

        //pane Initilize the keys

        delete.setOnAction(actionEvent -> {
            if(outputPad.getText().length() > 0) {
                outputPad.setText(outputPad.getText(0, outputPad.getText().length() - 1));
            }
        });

        for(Node n: pane.getChildren()){
            Button btn = (Button) n;

            btn.setOnAction(mouseEvent->{
                outputPad.setText(outputPad.getText() + btn.getText());

                if ("=".equals(btn.getText())) {
                    String method = findOperator(outputPad.getText());
                    switch (method) {
                        case "+":
                            add(outputPad.getText());
                            break;
                        case "-":
                            subtract(outputPad.getText());
                            break;
                        case "x":
                            multiply(outputPad.getText());
                            break;
                        case "/":
                            divide(outputPad.getText());
                            break;
                    }
                }
            });


        }
    }

    private String findOperator(String text){
        text = text.replace("=", "").replace(".", "");
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(text);
        if(m.find()){
            return m.group();
        }
        return "NAN";
    }

    private void add(String input){
        input = input.replace("=","");
        String[] parts = input.split("\\+");
        if(input.contains(".")){
            outputPad.setText(outputPad.getText() + new BigDecimal(parts[0]).add(new BigDecimal(parts[1])).doubleValue() );
        }else{
            outputPad.setText(outputPad.getText() + new BigInteger(parts[0]).add(new BigInteger(parts[1])).intValueExact()) ;
        }
    }
    private void subtract(String input){
        input = input.replace("=","");
        String[] parts = input.split("-");

        if(input.contains(".")){
            outputPad.setText(outputPad.getText() + new BigDecimal(parts[0]).subtract(new BigDecimal(parts[1])).doubleValue());
        }else{
            outputPad.setText(outputPad.getText() + new BigInteger(parts[0]).subtract(new BigInteger(parts[1])).intValueExact());
        }
    }

    private void multiply(String input){
        input = input.replace("=","");
        String[] parts = input.split("x");
        if(input.contains(".")){
            outputPad.setText(outputPad.getText() + new BigDecimal(parts[0]).multiply(new BigDecimal(parts[1])).doubleValue());
        }else{
            outputPad.setText(outputPad.getText() + new BigInteger(parts[0]).multiply(new BigInteger(parts[1])).intValueExact());
        }
    }

    private void divide(String input){
        input = input.replace("=","");
        String[] parts = input.split("/");
        try {
            if (input.contains(".")) {
                outputPad.setText(outputPad.getText() + (new BigDecimal(parts[0]).doubleValue() / (new BigDecimal(parts[1])).doubleValue()));
            } else {
                outputPad.setText(outputPad.getText() + new BigInteger(parts[0]).divide(new BigInteger(parts[1])).intValueExact());
            }
        }catch (ArithmeticException err){
            outputPad.setText("Error");
        }
    }
}
