package sample;

import javafx.scene.control.Label;

public class myLabel  extends Label {

    private String name, price, tax, fees, totalPrice, customPrice, eachItemTax, eachItemFees, eachPrice;
    private int times;

    public myLabel(String text) {
        super(text);
        times = 1;
    }

    public String getName() {
        return super.getText();
    }

    public void setName(String name) {
        super.setText(name);
    }



    public String getPrice() {
        return price.replace(",", "");
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(String customPrice) {
        this.customPrice = customPrice;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getEachItemTax() {
        return eachItemTax;
    }

    public void setEachItemTax(String eachItemTax) {
        this.eachItemTax = eachItemTax;
    }

    public String getEachItemFees() {
        return eachItemFees;
    }

    public void setEachItemFees(String eachItemFees) {
        this.eachItemFees = eachItemFees;
    }

    public String getEachPrice() {
        return eachPrice;
    }

    public void setEachPrice(String eachPrice) {
        this.eachPrice = eachPrice;
    }
}
