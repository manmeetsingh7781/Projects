package sample;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class ItemOptions {

    @FXML private Label itemLabel, itemPrice,itemFee,itemTax, itemCount, itemAgeNum, itemAgeRes, cartCount, itemTaxPrice;
    @FXML private Button addAmount, removeAmount, openINV, removeAll;
    @FXML private GridPane main_container;

    public String getItemLabel() {
        return itemLabel.getText();
    }

    public void setItemLabel(Label itemLabel) {
        this.itemLabel.setStyle(itemLabel.getStyle());
        this.itemLabel.setText(itemLabel.getText());
    }

    public String getItemPrice() {
        return itemPrice.getText();
    }

    public void setItemPrice(Label itemPrice) {
        this.itemPrice.setStyle(itemPrice.getStyle());
        this.itemPrice.setText(itemPrice.getText());
    }

    public String getItemFee() {
        return itemFee.getText();
    }

    public void setItemFee(Label itemFee) {
        this.itemFee.setStyle(itemFee.getStyle());
        this.itemFee.setText(itemFee.getText());
    }

    public String getItemTax() {
        return itemTax.getText();
    }

    public void setItemTax(Label itemTax) {
        this.itemTax.setStyle(itemTax.getStyle());
        this.itemTax.setText(itemTax.getText());
    }

    public String getItemTaxPrice() {
        return itemTaxPrice.getText();
    }

    public void setItemTaxPrice(Label itemTaxPrice) {
        this.itemTaxPrice.setStyle(itemTaxPrice.getStyle());
        this.itemTaxPrice.setText(itemTaxPrice.getText());
    }

    public String getItemCount() {
        return itemCount.getText();
    }

    public void setItemCount(Label itemCount) {
        this.itemCount.setStyle(itemCount.getStyle());
        this.itemCount.setText(itemCount.getText());
    }

    public String getNumberOfItems() {
        return cartCount.getText();
    }

    public void setNumberOfItems(Label incraseAmount) {
        this.cartCount.setStyle(incraseAmount.getStyle());
        this.cartCount.setText(incraseAmount.getText());
    }

    public Button getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(Button addAmount) {
        this.addAmount.setStyle(addAmount.getStyle());
        this.addAmount.setText(addAmount.getText());
    }

    public Button getRemoveAmount() {
        return removeAmount;
    }

    public String getitemAgeNum() {
        return itemAgeNum.getText();
    }


    public void setitemAgeNum(Label itemAgeNum) {
        this.itemAgeNum.setStyle(itemAgeNum.getStyle());
        this.itemAgeNum.setText(itemAgeNum.getText());
    }

    public String getAgeRes() {
        return itemAgeRes.getText();
    }


    public void setItemAgeRes(Label itemAgeRes) {
        this.itemAgeRes.setStyle(itemAgeRes.getStyle());
        this.itemAgeRes.setText(itemAgeRes.getText());
    }

    public GridPane getMain_container() {
        return main_container;
    }

    public void setRemoveAmount(Button removeAmount) {
        this.removeAmount.setStyle(removeAmount.getStyle());
        this.removeAmount.setText(removeAmount.getText());
    }

    public Button getOpenINV() {
        return openINV;
    }

    public void setOpenINV(Button openINV) {
        this.openINV.setStyle(openINV.getStyle());
        this.openINV.setText(openINV.getText());
    }

    public Button getRemoveAll() {
        return removeAll;
    }

    public void setRemoveAll(Button removeAll) {
        this.removeAll = removeAll;
    }
}
