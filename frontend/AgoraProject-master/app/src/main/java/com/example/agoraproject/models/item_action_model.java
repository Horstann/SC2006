package com.example.agoraproject.models;

public class item_action_model {
    String itemName;
    String itemDescription;
    String itemPrice;
    int itemImage;


    public item_action_model(String itemName,String itemDescription, String itemPrice, int itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public int getItemImage() {
        return itemImage;
    }
}
