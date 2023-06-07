package sample;

import javafx.scene.control.Button;


public class myButton extends Button {
    private String fees, price, name, count, taxPrice, outTheDoorPrice, defaultPrice, defaultFees, defaultTax, tempPriceHolder, itemInListName,

    // if item is being multiplied this is where its used
    customPrice, customTax, customFees;
    private boolean age_restriction, taxable;
    private int age;

    public boolean isAge_restriction() {
        return age_restriction;
    }

    public void setAge_restriction(boolean age_restriction) {
        this.age_restriction = age_restriction;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        super.setText(this.name);
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public String getOutTheDoorPrice() {
        return outTheDoorPrice;
    }

    public void setOutTheDoorPrice(String outTheDoorPrice) {
        this.outTheDoorPrice = outTheDoorPrice;
    }


    public String getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(String defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getDefaultFees() {
        return defaultFees;
    }

    public void setDefaultFees(String defaultFees) {
        this.defaultFees = defaultFees;
    }

    public String getDefaultTax() {
        return defaultTax;
    }

    public void setDefaultTax(String defaultTax) {
        this.defaultTax = defaultTax;
    }

    public String getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(String customPrice) {
        this.customPrice = customPrice;
    }

    public String getCustomTax() {
        return customTax;
    }

    public void setCustomTax(String customTax) {
        this.customTax = customTax;
    }

    public String getCustomFees() {
        return customFees;
    }

    public String getTempPriceHolder() {
        return tempPriceHolder;
    }

    public void setTempPriceHolder(String tempPriceHolder) {
        this.tempPriceHolder = tempPriceHolder;
    }

    public String getItemInListName() {
        return itemInListName;
    }

    public void setItemInListName(String itemInListName) {
        this.itemInListName = itemInListName;
    }

    public void setCustomFees(String customFees) {
        this.customFees = customFees;
    }
}