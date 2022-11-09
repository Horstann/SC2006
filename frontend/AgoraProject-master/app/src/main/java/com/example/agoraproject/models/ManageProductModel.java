package com.example.agoraproject.models;

public class ManageProductModel {
    String mName;
    String mPrice;
    String imageUrl;
    String mProdId;
    String mDes;
    public ManageProductModel(String mProdId,String mName, String mPrice, String mDes,String imageUrl) {
        this.mName = mName;
        this.mPrice = mPrice;
        this.imageUrl = imageUrl;
        this.mProdId = mProdId;
        this.mDes = mDes;
    }

    public String getmProdId() {
        return mProdId;
    }

    public String getmDes() {
        return mDes;
    }

    public String getmName() {
        return mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
