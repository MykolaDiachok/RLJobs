package com.radioline.master.basic;

/**
 * Created by master on 13.11.2014.
 */
public class Basket {
    private String productid;
    private int quantity;
    private float requiredprice;
    private float summa;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getSumma() {
        return summa;
    }

    public void setSumma(float summa) {
        this.summa = summa;
    }

    public void setSumma()
    {
        this.summa = this.quantity*this.requiredprice;
    }

    public float getRequiredprice() {
        return requiredprice;
    }

    public void setRequiredprice(float requiredprice) {
        this.requiredprice = requiredprice;
    }
}
