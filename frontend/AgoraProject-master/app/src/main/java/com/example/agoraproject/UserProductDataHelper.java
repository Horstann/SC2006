package com.example.agoraproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.agoraproject.models.AgoraProductModels;
import com.example.agoraproject.models.FaveItemModel;

import java.util.ArrayList;

public class UserProductDataHelper extends SQLiteOpenHelper {
    private static final String DB_Name = "userfavproduct.db";
    private static final String Table_Name = "favProductInfo";
    private static final String COL_ItemID = "prodId";
    private static final String COL_ID = "id";
    private static final String COL_ItemName ="productname";
    private static final String COL_ItemPrice ="productprice";
    private static final String COL_ItemDes="productDes";
    private static final String COL_ItemImage ="productImage"; // need change
    private static final String COL_Total_Bought ="productTotalBought";
    private static final String COL_Total_Units = "productTotalUnits";
    private static final String COL_Item_Price_Threshold = "productPriceThreshold";
    private static final String COL_Item_Unit_Threshold = "productUnitThreshold";
    private static final String COL_Item_Distance="distanceToSeller";
    private static final String COL_Item_ClosingTime="productClosingTime";
    private static final String COL_Item_Item_SellerLat="productSellerLat";
    private static final String COL_Item_Item_SellerLong="productSellerLong";
    public static String strSeparator = "__,__";

//    private static final int[] price_threshold;
//    private static final nt[] unit_threshold;

    public UserProductDataHelper(Context context) {
        super(context,DB_Name,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table " + Table_Name + "(id INTEGER primary key autoincrement ,prodId TEXT,productname TEXT ," +
                "productprice TEXT ,productDes TEXT, productImage TEXT," +
                "productTotalBought INTEGER,productTotalUnits INTEGER,productPriceThreshold TEXT,productUnitThreshold TEXT," +
                "distanceToSeller DOUBLE, productClosingTime TEXT,productSellerLat DOUBLE,productSellerLong DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists " + Table_Name);
        onCreate(db);
    }
    public boolean insertUserProductData(String pid,String name,String price,String des,String image,int bought,int units,String price_threshold,String unit_Threshold
    ,Double distancetoseller,String closingtime,Double sellerlat,Double sellerlong){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(COL_ItemID,pid);
        c.put(COL_ItemName,name);
        c.put(COL_ItemPrice,price);
        c.put(COL_ItemDes,des);
        c.put(COL_ItemImage,image);
        c.put(COL_Total_Bought,bought);
        c.put(COL_Total_Units,units);
        c.put(COL_Item_Price_Threshold,price_threshold);
        c.put(COL_Item_Unit_Threshold,unit_Threshold);
        c.put(COL_Item_Distance,distancetoseller);
        c.put(COL_Item_ClosingTime,closingtime);
        c.put(COL_Item_Item_SellerLat,sellerlat);
        c.put(COL_Item_Item_SellerLong,sellerlong);


        long res = db.insert(Table_Name,null,c);
        if (res == -1) return false;
        else return true;
    }
    public int deleteFavItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_Name,"productname =?", new String[]{id});
    }

    public ArrayList<AgoraProductModels> getFaveData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<AgoraProductModels> fItem = new ArrayList<>();
        Cursor c = db.rawQuery("select * from "+Table_Name,null);
        while(c.moveToNext()){
            int id = c.getInt(0);
            String pid = c.getString(1);
            String pname = c.getString(2);
            String pprice= c.getString(3);
            String pdes = c.getString(4);
            String pImages = c.getString(5);
            int totalbought = c.getInt(6);
            int totalunit = c.getInt(7);
            String price_threshold = c.getString(8);
            String unit_threshold = c.getString(9);
            Double distancetoseller = c.getDouble(10);
            String closingtime = c.getString(11);
            Double sellerlat = c.getDouble(12);
            Double sellerlong = c.getDouble(13);
            String[] images = convertStringToArray(pImages);

            String[] price_thre =convertStringToArray(price_threshold);
            String[] unit_thre =convertStringToArray(unit_threshold);
            float[] price_threshold_conv = new float[5];
            int[] unit_threshold_conv = new int[5];
            for(int i = 0; i < 5 ;i++){
                price_threshold_conv[i] =Float.parseFloat(price_thre[i]);
                unit_threshold_conv[i] = Integer.parseInt(unit_thre[i]);
            }
            AgoraProductModels f = new AgoraProductModels(pid,pname,pdes,pprice,images,totalunit,totalbought,price_threshold_conv,unit_threshold_conv,distancetoseller,closingtime,sellerlat,sellerlong);
            fItem.add(f);
        }
        return fItem;
    }

    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
