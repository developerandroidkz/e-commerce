package kz.putandgo.kuanysh.menuexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kuanish on 23/03/2017.
 */

public class SQLiteDB {
    private static final String DB_NAME = "Basket";
    private static final int DB_VERSION = 2;
    private static final String DB_TABLE = "userBasket";
    public static final String COLUMN_ID = "product_id";
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_NIK_NAME = "nik_name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TYPE_NAME= "type_name";
    public static final String COLUMN_MASSA_NETTO = "massa_netto";
    public static final String COLUMN_MASSA_BRUTTO = "massa_brutto";
    public static final String COLUMN_MANUFACTURER_NAME = "manufacturer_name";
    public static final String COLUMN_SPOSOBIY_HRANENIE = "sposobiy_hranenie";
    public static final String COLUMN_IMAGE_1 = "image_1_url";
    public static final String COLUMN_IMAGE_4 = "image_4_url";
    public static final String COLUMN_IMAGE_3 = "image_3_url";
    public static final String COLUMN_IMAGE_2 = "image_2_url";
    public static final String COLUMN_MARKET_ID = "market_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_COUNT = "count";

    private static final String DB_CREATE_DISHES_TABLE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " text, " +
                    COLUMN_BARCODE + " text, " +
                    COLUMN_NAME + " text, "+
                    COLUMN_NIK_NAME + " text, " +
                    COLUMN_DESCRIPTION + " text, " +
                    COLUMN_TYPE + " integer, "+
                    COLUMN_TYPE_NAME + " text," +
                    COLUMN_MASSA_NETTO + " text," +
                    COLUMN_MASSA_BRUTTO + " text, " +
                    COLUMN_MANUFACTURER_NAME + " text, "+
                    COLUMN_SPOSOBIY_HRANENIE + " text, " +
                    COLUMN_IMAGE_1 + " text, " +
                    COLUMN_IMAGE_2 + " text, "+
                    COLUMN_IMAGE_3 + " text, "+
                    COLUMN_IMAGE_4 + " text, "+
                    COLUMN_MARKET_ID + " integer, "+
                    COLUMN_DATE + " datetime, "+
                    COLUMN_PRICE + " integer, "+
                    COLUMN_COUNT + " integer "+
                    ");";
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    public SQLiteDB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение

    /** This method opens database*/
    public void open() {

            mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
            mDB = mDBHelper.getWritableDatabase();
    }


    // закрыть подключение
    //This method closes database
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    /**This method add event to AllEvents */
    public void addproduct(String id, String barcode, String name, String nik_name,
                        String description, int type,String type_name,String massa_netto,
                           String massa_brutto, String manufacturer_name, String sposobiy_hranenie,
                        String image_1, String image_2, String image_3, String image_4, int market_id,
                           String date, int price,int count) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID , id);
        cv.put(COLUMN_BARCODE, barcode);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_NIK_NAME, nik_name);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_TYPE_NAME, type_name);
        cv.put(COLUMN_MASSA_NETTO, massa_netto);
        cv.put(COLUMN_MASSA_BRUTTO , massa_brutto);
        cv.put(COLUMN_MANUFACTURER_NAME, manufacturer_name);
        cv.put(COLUMN_SPOSOBIY_HRANENIE, sposobiy_hranenie);
        cv.put(COLUMN_IMAGE_1, image_1);
        cv.put(COLUMN_IMAGE_2, image_2);
        cv.put(COLUMN_IMAGE_3, image_3);
        cv.put(COLUMN_IMAGE_4, image_4);
        cv.put(COLUMN_MARKET_ID, market_id);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_COUNT, count);
        mDB.insert(DB_TABLE, null, cv);
    }
    public String getCount(String barcode,String marketId){
        String count="1";
        String selectQuery = "SELECT * FROM " + DB_TABLE + " WHERE "+COLUMN_BARCODE+"='"+barcode+"' AND "+COLUMN_MARKET_ID+"= "+marketId+"";
        Cursor cursor = mDB.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count=cursor.getInt(18)+"";

            } while (cursor.moveToNext());
        }
        return count;
    }
    public void changeCount(String barcode,String marketId,String count){
        String UpdateQuery = "UPDATE " + DB_TABLE +" SET "+COLUMN_COUNT+" = "+count+ " WHERE "+COLUMN_BARCODE+" ='"+barcode+"' AND "+COLUMN_MARKET_ID+"='"+marketId+"'";
        Cursor cursors = mDB.rawQuery(UpdateQuery, null);
        if(cursors!=null && cursors.getCount()>0){
            if (cursors.moveToFirst()) {
                do {
                }while (cursors.moveToNext());
                }
            }
    }
    public boolean isHave(String barcode,String marketId,String price){
        int PriceInDB,senderPrice=0;
        String selectQuery = "SELECT * FROM " + DB_TABLE + " WHERE "+COLUMN_BARCODE+"='"+barcode+"' AND "+COLUMN_MARKET_ID+"= "+marketId+"";
        Cursor cursor = mDB.rawQuery(selectQuery, null);
        if(cursor!=null && cursor.getCount()>0){
            if (cursor.moveToFirst()) {
                do {

            PriceInDB=cursor.getInt(17);

            try{
                senderPrice=Integer.parseInt(price);
            }catch (NumberFormatException e){
                Log.e("SenderPrice to int",e.getMessage());
            }
            if(PriceInDB==senderPrice){

            }else{
                String UpdateQuery = "UPDATE " + DB_TABLE +" SET "+COLUMN_PRICE+" = '"+price+ "' WHERE "+COLUMN_BARCODE+" ='"+barcode+"' AND "+COLUMN_MARKET_ID+"='"+marketId+"'";
                Cursor cursors = mDB.rawQuery(UpdateQuery, null);
                if(cursors!=null && cursors.getCount()>0){
                    if (cursors.moveToFirst()) {
                        do {
                        }while (cursors.moveToNext());
                    }
                }
            }
                } while (cursor.moveToNext());
            }
            return true;
        }else{
            return false;
        }
    }
    public String summa(String marketId){
        String selectQuery = "SELECT * FROM " + DB_TABLE + " WHERE "+COLUMN_MARKET_ID+"="+marketId;
        Cursor cursor = mDB.rawQuery(selectQuery, null);
        double i=0;
        if (cursor.moveToFirst()) {
            do {

                i=i+((cursor.getInt(18)/cursor.getInt(5))*cursor.getInt(17));
            } while (cursor.moveToNext());
        }
            return i+"";
    }
    public boolean isDeletedInDb(String barcode,String marketId) {
        try {
            mDB.delete(DB_TABLE, COLUMN_BARCODE + "=" + barcode + " and " + COLUMN_MARKET_ID + "=" + marketId, null);
            return true;
        }catch (SQLiteAbortException e){
            Log.e("delete some row",e.getMessage());
            return false;
        }
    }
        public List<Basket> getProducts(String marketId){
        List<Basket> newsList = new ArrayList<Basket>();
        String selectQuery = "SELECT * FROM " + DB_TABLE + " WHERE "+COLUMN_MARKET_ID+"="+marketId;
        Cursor cursor = mDB.rawQuery(selectQuery, null);
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                Basket products = new Basket();
                products.setProduct_id(cursor.getString(0));
                products.setBarcode(cursor.getString(1));
                products.setName(cursor.getString(2));
                products.setNik_name(cursor.getString(3));
                products.setDescription(cursor.getString(4));
                products.setType(cursor.getInt(5));
                products.setType_name(cursor.getString(6));
                products.setMassa_netto(cursor.getString(7));
                products.setMassa_brutto(cursor.getString(8));
                products.setManufacturer_name(cursor.getString(9));
                products.setSposobiy_hranenie(cursor.getString(10));
                products.setImage_1_url(cursor.getString(11));
                products.setImage_2_url(cursor.getString(12));
                products.setImage_3_url(cursor.getString(13));
                products.setImage_4_url(cursor.getString(14));
                products.setMarket_id(cursor.getInt(15));
                products.setDate(cursor.getString(16));
                products.setPrice(cursor.getInt(17));
                products.setCount(cursor.getInt(18));
                newsList.add(products);
            } while (cursor.moveToNext());
        }
        return newsList;
    }
    public void clear() {
        mDB.delete(DB_TABLE, null, null);
    }
    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_DISHES_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
