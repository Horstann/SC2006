package com.example.agoraproject.models;

import android.graphics.Bitmap;

public class FaveItemModel {
    String productName;
    String itemPrice;
    String productdes;
    String productId;
    String[] itemImage;
    int total_units;
    int total_bought;
    float[] price_threshold;
    int[] unit_threshold;

    public FaveItemModel(String productId, String productName, String productdes, String itemPrice, String[] itemImage,
                         int total_units, int total_bought, float[] price_threshold, int[] unit_threshold) {
        this.productId = productId;
        this.productName = productName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.productdes = productdes;
        this.total_units = total_units;
        this.total_bought = total_bought;
        this.price_threshold = price_threshold;
        this.unit_threshold = unit_threshold;
    }

    public String getProductName() {
        return productName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getProductdes() {
        return productdes;
    }

    public String getProductId() {
        return productId;
    }

    public String[] getItemImage() {
        return itemImage;
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
}
