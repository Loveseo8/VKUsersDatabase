package com.dmitrij.vkusersdatabase;

import android.content.ContentValues;
import android.content.Context; import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

class DBHandler extends SQLiteOpenHelper{

    private static final String DB_NAME = "VK";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Users";
    private static final String ID_COL = "id";
    private static final String COLORID_COL = "colorID";
    private static final String USERID_COL = "userID";
    private static final String LASTNAME_COL = "last_name";
    private static final String NAME_COL = "first_name";
    private static final String SEX_COL = "sex";
    private static final String AGE_COL = "age";
    private static final String CITY_COL = "city";
    private static final String LASTSEEN_COL = "last_seen";
    private static final String EDUCATION_COL = "education";
    private static final String HASPHOTO_COL = "has_photo";
    private static final String INTERESTS_COL = "interests";
    private static final String GROUPS_COL = "groups";
    private static final String FRIENDS_COL = "friends";
    private static final String FOLLOWERS_COL = "followers";
    private static final String PHONENUMBER_COL = "phone_number";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("+ ID_COL +" INTEGER PRIMARY KEY AUTOINCREMENT," + COLORID_COL + " TEXT," + USERID_COL + " INTEGER," + LASTNAME_COL + " TEXT," + NAME_COL + " TEXT," + SEX_COL + " TEXT, " + AGE_COL + " TEXT," + CITY_COL + " TEXT," + LASTSEEN_COL + " TEXT," + EDUCATION_COL + " TEXT," + HASPHOTO_COL + " TEXT," + INTERESTS_COL + " TEXT," + GROUPS_COL + " INTEGER," + FRIENDS_COL + " INTEGER," + FOLLOWERS_COL + " INTEGER," + PHONENUMBER_COL + " TEXT" + ");";
        db.execSQL(query);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertRecord(String color_index, int id, String last_name, String first_name, String sex, String age, String city, String last_seen, String education, String has_photo, String interests, int groups, int friends, int followers, String phone_number){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLORID_COL, color_index);
        values.put(USERID_COL, id);
        values.put(LASTNAME_COL, last_name);
        values.put(NAME_COL, first_name);
        values.put(SEX_COL, sex);
        values.put(AGE_COL, age);
        values.put(CITY_COL, city);
        values.put(LASTSEEN_COL, last_seen);
        values.put(EDUCATION_COL, education);
        values.put(HASPHOTO_COL, has_photo);
        values.put(INTERESTS_COL, interests);
        values.put(GROUPS_COL, groups);
        values.put(FRIENDS_COL, friends);
        values.put(FOLLOWERS_COL, followers);
        values.put(PHONENUMBER_COL, phone_number);

        db.insert(TABLE_NAME,null,values); db.close();
    }

    public ArrayList<String> getRecords(){
        String query="SELECT * FROM " + TABLE_NAME;
        ArrayList<String> results = new ArrayList<>();
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        while(cursor.isAfterLast()==false){
            results.add(cursor.getString(0)+"   "+cursor.getString(1)+ "   " + cursor.getString(2) + "   " + cursor.getString(3) + "   " + cursor.getString(4) + "   " + cursor.getString(5) + "   " + cursor.getString(6) + "   " + cursor.getString(7) + "   " + cursor.getString(8) + "   "  + cursor.getString(9) + "   " + cursor.getString(10) + "   " + cursor.getString(11) + "   " + cursor.getString(12) + "   " + cursor.getString(13) + "   " + cursor.getString(14));
            cursor.moveToNext();
        } db.close();
        return results;
    }
}
