package com.radioline.master.basic;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by master on 03.01.2015.
 */
@ParseClassName("ParseGroups")
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

    public static ParseQuery<ParseGroups> getQuery() {
        return ParseQuery.getQuery(ParseGroups.class);
    }

    public String getObjectId() {
        return getString("objectId");
    }

    public void setObjectId(String objectId) {
        put("objectId", objectId);
    }

    public Date getUpdatedAt() {
        return getDate("updatedAt");
    }

    public void setUpdatedAt(Date updatedAt) {
        put("updatedAt", updatedAt);
    }

    public Date getCreatedAt() {
        return getDate("createdAt");
    }

    public void setCreatedAt(Date createdAt) {
        put("createdAt", createdAt);
    }

    public String getWarranty() {
        return getString("Warranty");
    }

    public void setWarranty(String Warranty) {
        put("Warranty", Warranty);
    }

    public Integer getStock() {
        return getInt("Stock");
    }

    public void setStock(Integer Stock) {
        put("Stock", Stock);
    }

    public float getPriceUAH() {
        return (float) get("PriceUAH");
    }

    public void setPriceUAH(float PriceUAH) {
        put("PriceUAH", PriceUAH);
    }

    public float getPriceRRPUAH() {
        return (float) get("PriceRRPUAH");
    }

    public void setPriceRRPUAH(float PriceRRPUAH) {
        put("PriceRRPUAH", PriceRRPUAH);
    }

    public float getPriceRRP() {
        return (float) get("PriceRRP");
    }

    public void setPriceRRP(float PriceRRP) {
        put("PriceRRP", PriceRRP);
    }

    public float getPriceROPUAH() {
        return (float) get("PriceROPUAH");
    }

    public void setPriceROPUAH(float PriceROPUAH) {
        put("PriceROPUAH", PriceROPUAH);
    }

    public float getPriceROP() {
        return (float) get("PriceROP");
    }

    public void setPriceROP(float PriceROP) {
        put("PriceROP", PriceROP);
    }

    public float getPrice() {
        return (float) get("Price");
    }

    public void setPrice(float Price) {
        put("Price", Price);
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
}
