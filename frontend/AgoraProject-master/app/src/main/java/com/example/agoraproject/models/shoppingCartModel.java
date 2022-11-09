package com.example.agoraproject.models;

public class shoppingCartModel {
        String pname;
        String pprice;
        int image;

        public shoppingCartModel(String pname, String pprice, int image) {
            this.pname = pname;
            this.pprice = pprice;
            this.image = image;
        }

        public String getScname() {
            return pname;
        }

        public String getScprice() {
            return pprice;
        }

        public int getScImage() {
            return image;
        }
}
