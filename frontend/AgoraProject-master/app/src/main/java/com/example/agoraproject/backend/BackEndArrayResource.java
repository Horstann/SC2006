package com.example.agoraproject.backend;

public class BackEndArrayResource {
    private  BackEndResource[] products;
    private int status;

    public int getStatus() {
        return status;
    }

    public BackEndResource[] getProductArray() {
        return products;
    }
}
