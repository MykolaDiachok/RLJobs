package com.radioline.master.basic;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by master on 03.01.2015.
 * Кол.	Сумма количества едениц остатков
 * 5	1 267
 * 20	619
 * 10	588
 * 100	373
 * 30	333
 * 40	202
 * 50	153
 * 70	95
 * 60	94
 * 80	45
 * 90	42
 */
@ParseClassName("ParseItems")
public class ParseItems extends ParseObject {
    public ParseItems() {
    }

    /**
     * -Article
     * -Availability
     * -Brand
     * -Code
     * -Coloration
     * -FullNameGroup
     * -Group
     * -GroupId
     * -ItemId
     * -Model
     * -Name
     * -OurWebSite
     * -PartNumber
     * -Price
     * -PriceROP
     * -PriceROPUAH
     * -PriceRRP
     * -PriceRRPUAH
     * -PriceUAH
     * -Stock
     * -Warranty
     * -createdAt
     * -objectId
     * -updatedAt
     */

    public static ParseQuery<ParseItems> getQuery() {
        return ParseQuery.getQuery(ParseItems.class);
    }

//    public String getObjectId(){
//        return getString("id");
//    }

    public Date getUpdatedAt() {
        return getDate("updatedAt");
    }

    public Date getCreatedAt() {
        return getDate("createdAt");
    }

    public String getWarranty() {
        return getString("Warranty");
    }

    public Integer getStock() {
        return getInt("Stock");
    }

    public double getPriceUAH() {
        return getDouble("PriceUAH");
    }

    public double getPriceRRPUAH() {
        return getDouble("PriceRRPUAH");
    }

    public double getPriceRRP() {
        return getDouble("PriceRRP");
    }

    public double getPriceROPUAH() {
        return getDouble("PriceROPUAH");
    }

    public double getPriceROP() {
        return getDouble("PriceROP");
    }

    public double getPrice() {
        return getDouble("Price");
    }

    public String getPartNumber() {
        return getString("PartNumber");
    }

    public void setPartNumber(String PartNumber) {
        put("PartNumber", PartNumber);
    }

    public String getOurWebSite() {
        return getString("OurWebSite");
    }

    public void setOurWebSite(String OurWebSite) {
        put("OurWebSite", OurWebSite);
    }

    public String getName() {
        return getString("Name");
    }

    public void setName(String Name) {
        put("Name", Name);
    }

    public String getModel() {
        return getString("Model");
    }

    public void setModel(String Model) {
        put("Model", Model);
    }

    public String getItemId() {
        return getString("ItemId");
    }

    public void setItemId(String ItemId) {
        put("ItemId", ItemId);
    }

    public String getGroupId() {
        return getString("GroupId");
    }

    public void setGroupId(String GroupId) {
        put("GroupId", GroupId);
    }

    public String getFullNameGroup() {
        return getString("FullNameGroup");
    }

    public void setFullNameGroup(String FullNameGroup) {
        put("FullNameGroup", FullNameGroup);
    }

    public String getColoration() {
        return getString("Coloration");
    }

    public void setColoration(String Coloration) {
        put("Coloration", Coloration);
    }

    public String getCode() {
        return getString("Code");
    }

    public void setCode(String Code) {
        put("Code", Code);
    }

    public String getBrand() {
        return getString("Brand");
    }

    public void setBrand(String Brand) {
        put("Brand", Brand);
    }

    public Boolean getAvailability() {
        return getBoolean("Availability");
    }

    public void setAvailability(Boolean Availability) {
        put("Availability", Availability);
    }

    public String getArticle() {
        return getString("Article");
    }

    public void setArticle(String Article) {
        put("Article", Article);
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

    public void setImage(ParseFile file) {
        put("image", file);
    }

    public String getImageURL() {
        return getParseFile("image").getUrl();
    }
}
