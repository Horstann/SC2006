package com.example.agoraproject.models;

public class AgoraProductModels {
    String productName;
    String itemPrice;
    String productdes;
    String productId;
    String[] itemImage;
    int total_units ;
    int total_bought ;
    float[] price_threshold ;
    int[] unit_threshold ;
    String userName;
    Double distancetoseller;
    String closingtime;
    Double sellerlat;
    Double sellerlong;
    public AgoraProductModels(String productId, String productName, String productdes, String itemPrice, String[] itemImage,
                              int total_units, int total_bought, float[]  price_threshold, int[] unit_threshold,Double distancetoseller,String closingtime
                                ,Double sellerlat,Double sellerlong)
    {
        this.productId = productId;
        this.productName = productName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.productdes = productdes;
        this.total_units = total_units;
        this.total_bought = total_bought;
        this.price_threshold = price_threshold;
        this.unit_threshold = unit_threshold;
        this.distancetoseller=distancetoseller;
        this.closingtime=closingtime;
        this.sellerlat=sellerlat;
        this.sellerlong=sellerlong;

    }

    public Double getSellerlat() {
        return sellerlat;
    }

    public Double getSellerlong() {
        return sellerlong;
    }

    public Double getDistancetoseller() {
        return distancetoseller;
    }

    public String getClosingtime() {
        return closingtime;
    }

    public int getTotal_units() {
        return total_units;
    }

    public int getTotal_bought() {
        return total_bought;
    }

    public float[] getPrice_threshold() {
        return price_threshold;
    }

    public int[] getUnit_threshold() {
        return unit_threshold;
    }

    public  String getProductId() { return productId;}
    public String[] getItemImage() {
        return itemImage;
    }
    public String getProductdes() {
        return productdes;
    }
    public String getItemPrice() {
        return itemPrice;
    }
    public String getProductName(){
        return productName;
    }
}
