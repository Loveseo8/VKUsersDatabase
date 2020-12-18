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
        String query = "CREATE TABLE " + TABLE_NAME + " ("+ ID_COL +" INTEGER PRIMARY KEY AUTOINCREMENT," + COLORID_COL + " INTEGER," + USERID_COL + " INTEGER," + LASTNAME_COL + " TEXT," + NAME_COL + " TEXT," + LASTNAME_COL + " TEXT," + ");";
        db.execSQL(query);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertRecord(int color_index, int id, String last_name, String first_name, String sex, int age, String city, String last_seen, String education, String has_photo, String interests, int groups, int friends, int followers, String phone_number){

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

    public String getRecords(){
        String query="SELECT * FROM " + TABLE_NAME;
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        while(cursor.isAfterLast()==false){
            result+=cursor.getString(0)+" "+cursor.getString(1)+ " " + cursor.getString(2) + "\n";
            cursor.moveToNext();
        } db.close();
        return result;
    }
}
