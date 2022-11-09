package com.example.agoraproject.backend;

public class BackEndResource {
    private int status;
    private String helloWorld;
    private String productId;
    private String url;
    private float distanceFromUser;
    private String name;
    private int totalUnits;
    private int totalBought;
    private float[] priceThresholds;
    private int[] unitThresholds;
    //closingTime: timestamp
    private String desc;
    private String[] pics;
    private Double sellerLat;
    private Double sellerLong;
    private String sellerAddr;
    private String qrcode;
    private boolean isClosed;
    private String closingTime;
    private int unitsBought;
    private double homeLat;
    private double homeLong;
    private String homeAddr;

    public double getHomeLat() {
        return homeLat;
    }

    public double getHomeLong() {
        return homeLong;
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public int getUnitsBought() {
        return unitsBought;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }
    public String getProductId() {
        return productId;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() { return status;}
    public String getHelloWorld() {
        return helloWorld;
    }

    public String getName() {
        return name;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public int getTotalBought() {
        return totalBought;
    }

    public float[] getPriceThresholds() {
        return priceThresholds;
    }

    public int[] getUnitThresholds() {
        return unitThresholds;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getPics() {
        return pics;
    }

    public Double getSellerLat() {
        return sellerLat;
    }

    public Double getSellerLong() {
        return sellerLong;
    }

    public String getSellerAddr() {
        return sellerAddr;
    }
}
