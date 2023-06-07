package Graphing;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class GraphController {

    @FXML
    GridPane gridPane;

    @FXML
    Canvas canvas;


    @FXML
    VBox vertical_container;

    @FXML
    Button addBar;


    @FXML
    ListView<VBox> function;

    @FXML
    ToggleButton toggleTable;

    @FXML
    TableView<Coordinates> tableView;


    static Grid grid;


    private ObservableList<VBox> function_names;

    private Button addValue;
    private HBox tableInputHBox;
    private ML_Model ml_model;




    public void initialize(){


        ml_model = new ML_Model();

        // the table button
        addValue = new Button("ADD");

        // the table input panel
        tableInputHBox = new HBox();

        initializeTableView();

        addValue.setCursor(Cursor.HAND);
        addBar.setCursor(Cursor.HAND);
        toggleTable.setCursor(Cursor.HAND);



        function_names = FXCollections.observableArrayList();


        // initialize the empty item in the list with input
        function_names.add(addInput());
        function.setItems(function_names);


        gridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        grid = new Grid(canvas.getGraphicsContext2D());



        canvas.setOnMousePressed(this :: actionEventOnCanvasClick);

        addBar.setOnAction(event -> function_names.add(addInput()));



        // the button next to ADD which allows to insert the values manually into the table
        toggleTable.setOnMouseClicked(event -> handleToggleTable());

    }

    private void handleToggleTable(){
        // the button that allows the table to open/close
        boolean isSelected = toggleTable.isSelected();

        // if the button is clicked then we can see the table
        tableView.setVisible(isSelected);
        tableInputHBox.setVisible(isSelected);

        // else erase table data once the button is unselected
        if(!isSelected){
            tableView.getItems().removeAll(tableView.getItems());
            grid.erasePlottedPoints();
            addValue.setDisable(false);
            new Grapher(canvas.getGraphicsContext2D(), new Function(ml_model.getEquation()), true, grid.getCenter_x(), grid.getCenter_y(),  grid.getBox_width());
            ml_model = new ML_Model();

        }
    }


    // when user clicks on canvas, these are the event that will occur
    private void actionEventOnCanvasClick(MouseEvent event){

        // to keep the limit of points being added into list
        boolean isFull = tableView.getItems().size() >= 5;


        if(!isFull) {

            Double[] coordinates = grid.plotPointWRMouse(event.getX(), event.getY());
            Double[] rounded = new Double[]{(double) Math.round(coordinates[0]), (double) Math.round(coordinates[1])};

            tableView.getItems().add(new Coordinates(rounded));


            ml_model.addX(rounded[0]);
            ml_model.addY(rounded[1]);

        }else{

            drawMLEquation();

        }

        if(!toggleTable.isSelected()){
            tableView.setVisible(true);
            tableInputHBox.setVisible(true);
            toggleTable.setSelected(true);
        }

        addValue.setDisable(isFull);

    }


    // controls the Table View on the scene, the columns, and events
    private void initializeTableView(){

        /*

         The table view column initialize for input values from the chart

         */

        TableColumn<Coordinates, String> x_col = new TableColumn<>("x");
        x_col.setPrefWidth(80);
        x_col.setCellValueFactory(new PropertyValueFactory<>("x"));


        TableColumn<Coordinates, String> y_col = new TableColumn<>("ƒ(x)");
        y_col.setPrefWidth(135);
        y_col.setCellValueFactory(new PropertyValueFactory<>("y"));



        /* adding text field to the x value column to enter manual items */
        TextField x_col_textField = new TextField();
        x_col_textField.setPromptText("x values");
        x_col_textField.setPrefWidth(x_col.getPrefWidth());

        TextField y_col_textField = new TextField();
        y_col_textField.setPromptText("ƒ(x) values");
        y_col_textField.setPrefWidth(y_col.getPrefWidth());



        // adds the x and y values that are inserted through the table view
        addValue.setOnAction(event -> handleEditTable(x_col_textField, y_col_textField));


        // add all of the Text fields into the HBox

        tableInputHBox.getChildren().addAll(x_col_textField, y_col_textField, addValue);
        tableInputHBox.setSpacing(5);
        tableInputHBox.setVisible(false);

        vertical_container.getChildren().add(tableInputHBox);


        tableView.getColumns().add(x_col);
        tableView.getColumns().add(y_col);

    }

    private void drawMLEquation(){

            // draw the graph on the canvas and start the new thread
        ml_model.fit();

        new Grapher(canvas.getGraphicsContext2D(), new Function(ml_model.getEquation()), false, grid.getCenter_x(), grid.getCenter_y(),  grid.getBox_width());

        // this is to add the input box when the ML Model finds the equation
        VBox vBox = addInput();
        TextField field = (TextField) ((HBox) vBox.getChildren().get(1)).getChildren().get(0);
        field.setText(ml_model.getEquation());
        field.setEditable(false);
        function_names.add(vBox);

    }

    private void handleEditTable(TextField x_col_textField, TextField y_col_textField){

        String x_val  = x_col_textField.getText();
        String y_val  = y_col_textField.getText();
        boolean isFull = tableView.getItems().size() == 5;

        if(x_val.length() > 0 && y_val.length() > 0 && !isFull ){

            double parsed_x = Double.parseDouble(x_val);
            double parsed_y = Double.parseDouble(y_val);

            grid.plotPoint(parsed_x, parsed_y);
            tableView.getItems().add(new Coordinates(parsed_x, parsed_y));

            ml_model.addX(parsed_x);
            ml_model.addY(parsed_y);


            x_col_textField.clear();
            y_col_textField.clear();


        }

        addValue.setDisable(isFull);

        if(isFull){
            drawMLEquation();
        }



    }


    // creates a text bar and the buttons to graph and delete
    private VBox addInput(){


        VBox vBox = new VBox();
        HBox hBox = new HBox();

        TextField field = new TextField();
        field.setId("function_field");
        field.setPromptText("Enter Function");
        field.setPrefWidth(230);


        GraphButton graphBtn = new GraphButton("Graph");
        GraphButton deleteButton = new GraphButton("Delete");
        deleteButton.setVisible(true);
        deleteButton.setDisable(false);


        // the event when use clicks on graph button
        graphBtn.setOnAction(event -> handleGraphing(field, graphBtn));

        deleteButton.setOnAction(event -> handleEraseGraph(field, vBox));


        field.setOnKeyReleased(e->{
            // if the text bar has no text then disable the graph button, avoids extra iterations
            graphBtn.setDisable(!(field.getText().length() > 0));
        });

        // after all add everything together in the vertically box and return it to main container
        hBox.getChildren().addAll(field);
        vBox.getChildren().addAll(new Label(), hBox,  new Label(), new HBox(graphBtn, new Label("\t\t"), deleteButton));

        return vBox;
    }


    private void handleGraphing(TextField field, Button graphBtn){
        try {

            // avoid bubbling
            field.setEditable(false);
            field.setOpacity(0.5);
            graphBtn.setOpacity(0.5);
            graphBtn.setDisable(true);

            // draw the graph on the canvas and start the new thread
            new Grapher(canvas.getGraphicsContext2D(), new Function(field.getText()), false, grid.getCenter_x(), grid.getCenter_y(),  grid.getBox_width());


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void handleEraseGraph(TextField field, VBox vBox){
        try {

            // before we draw lets check is field has some value to redraw
            String func = field.getText();
            if(func.length() > 0) {
                new Grapher(canvas.getGraphicsContext2D(), new Function(func), true, grid.getCenter_x(), grid.getCenter_y(),  grid.getBox_width());
            }

            // remove the item from the list
            function_names.remove(vBox);

            // the list view has deleted all text field rather then manually clicking and adding the new text bar
            // add automatically to the view knowing its the last one
            if(function.getItems().size() == 0){
                function.getItems().add(addInput());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
