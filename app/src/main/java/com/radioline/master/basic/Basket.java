package com.radioline.master.basic;

/**
 * Created by master on 13.11.2014.
 */
public class Basket {
    private String productId;
    private String name;
    private int quantity;
    private float requiredpriceUSD;
    private float requiredpriceUAH;
    private float summaUSD;
    private float summaUAH;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getRequiredpriceUSD() {
        return requiredpriceUSD;
    }

    public void setRequiredpriceUSD(float requiredpriceUSD) {
        this.requiredpriceUSD = requiredpriceUSD;
    }

    public float getRequiredpriceUAH() {
        return requiredpriceUAH;
    }

    public void setRequiredpriceUAH(float requiredpriceUAH) {
        this.requiredpriceUAH = requiredpriceUAH;
    }

    public float getSummaUSD() {
        return summaUSD;
    }

    public void setSummaUSD(float summaUSD) {
        this.summaUSD = summaUSD;
    }

    public float getSummaUAH() {
        return summaUAH;
    }

    public void setSummaUAH(float summaUAH) {
        this.summaUAH = summaUAH;
    }


}
