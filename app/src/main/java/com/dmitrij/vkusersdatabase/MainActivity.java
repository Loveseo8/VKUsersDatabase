package com.dmitrij.vkusersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String key = "8a47bbea8a47bbea8a47bbeafa8a32c9f088a478a47bbead59347e438c8ab96e520554f";
    String json = "";
    DBHandler db;
    EditText enter_id;
    Button button_insert;
    Button button_show;


    class CountTask extends AsyncTask<Integer, Integer, Void> {


        public Response getInfoByID(int userID) throws JSONException {
            try {
                URL url = new URL("https://api.vk.com/method/users.get?user_ids=" + userID + "&fields=counters,bdate,sex,last_seen,city,has_photo,interests,counters,contacts,education&access_token=" + key + "&v=5.126");
                URLConnection yc = url.openConnection();
                yc.connect();
                BufferedReader buffIn = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;
                while ((inputLine = buffIn.readLine()) != null) json = (inputLine);
                buffIn.close();

            } catch (IOException | NullPointerException e) {

                e.printStackTrace();

            }
            
            Response response = null;

            int color_index = 0;
            int id = 0;
            String last_name = null;
            String first_name = null;
            int sex = 0;
            String university_name = null;
            String faculty_name = null;
            String bdate = null;
            String adress = null;
            String last_seen = null;
            String education = null;
            int has_photo = 0;
            String interests = null;
            int groups = 0;
            int pages = 0;
            int friends = 0;
            int followers = 0;
            String phone_number = null;
            String counters = null;
            boolean is_closed = false;

            JSONObject reader = new JSONObject(json);
            JSONArray jsonArray = reader.getJSONArray("response");

            for(int i = 0; i < jsonArray.length(); i++){
                try{

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getInt("id");
                    last_name = jsonObject.getString("last_name");
                    first_name = jsonObject.getString("first_name");
                    bdate = jsonObject.getString("bdate");
                    sex = jsonObject.getInt("sex");
                    adress = jsonObject.getString("city");
                    last_seen = jsonObject.getString("last_seen");
                    university_name = jsonObject.getString("university_name");
                    faculty_name = jsonObject.getString("faculty_name");
                    has_photo = jsonObject.getInt("has_photo");
                    interests = jsonObject.getString("interests");
                    phone_number = jsonObject.getString("phone_number");
                    is_closed = jsonObject.getBoolean("is_closed");

                }catch (JSONException e){

                }
            }

            for(int i = 0; i < jsonArray.length(); i++){
                try{

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    counters = jsonObject.getString("counters");

                }catch (JSONException e){

                }
            }

            JSONObject read = new JSONObject(adress);
            adress = read.getString("title");

            read = new JSONObject(last_seen);
            int time = read.getInt("time");

            Date lst = new Date((long)time*1000);

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String last_seen_time = dateFormat.format(lst);

            String []bday = bdate.split("\\.");

            if(bday.length == 3) {

                LocalDate birthdate = new LocalDate(Integer.parseInt(bday[2]), Integer.parseInt(bday[1]), Integer.parseInt(bday[0]));
                LocalDate now = new LocalDate();
                Years age = Years.yearsBetween(birthdate, now);

                String agee = age.toString();

                bdate = agee;

            }


            JSONObject r = new JSONObject(counters);
            followers = r.getInt("followers");
            pages = r.getInt("pages");

            //friends = r.getInt("friends");

            if(university_name != "") education = university_name;
            if(faculty_name != "") education += " " + faculty_name;

            response = new Response(0, id, last_name, first_name, sex, bdate, adress, last_seen_time, education.trim(), has_photo, interests, pages, friends, followers, phone_number, counters, is_closed);
            

            return response;

        }
        @Override
        protected Void doInBackground(Integer... users) {
            for (int userID: users){
                try {
                    //db.insertRecord(getInfoByID(userID).first_name, getInfoByID(userID).last_name);
                    Log.d("TAGA", getInfoByID(userID).id + " " + getInfoByID(userID).last_name + " " + getInfoByID(userID).first_name + " " + getInfoByID(userID).sex + " " + getInfoByID(userID).bdate + " " + getInfoByID(userID).adress + " " + getInfoByID(userID).last_seen + " " + getInfoByID(userID).education + " " + getInfoByID(userID).has_photo + " " + getInfoByID(userID).interests +  " " + getInfoByID(userID).phone_number + " " +getInfoByID(userID).is_closed + " " + getInfoByID(userID).followers + " " + getInfoByID(userID).friends + " " + getInfoByID(userID).groups + " " + getInfoByID(userID).counters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                publishProgress(userID);
            }
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(getApplicationContext());

        enter_id = findViewById(R.id.enter_id);
        textView = findViewById(R.id.user_info);
        button_insert = findViewById(R.id.button_insert);
        button_show = findViewById(R.id.button_show);

        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView.setText(db.getRecords());

            }
        });

        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CountTask task = new CountTask();
                task.execute(Integer.parseInt(enter_id.getText().toString()));

            }
        });

    }

    }

class Response{

    int color_index;
    int id;
    String last_name;
    String first_name;
    int sex;
    String bdate;
    String adress;
    String last_seen;
    String education;
    int has_photo;
    String interests;
    int groups;
    int friends;
    int followers;
    String phone_number;
    String counters;
    boolean is_closed;

    //НА СТРАНИЦАХ ПОЛЬЗОВАТЕЛЕЙ НЕ ОТОБРАЖАЕТСЯ АДРЕС ЭЛЕКТРОННОЙ ПОЧТЫ И МЕТОД ДЛЯ ЕЁ ПОЛУЧЕНИЯ ОТСУТВУЕТ У VK api


    public Response(int color_index, int id, String last_name, String first_name, int sex, String bdate, String adress, String last_seen, String education, int has_photo, String interests, int groups, int friends, int followers, String phone_number, String counters, boolean is_closed) {
        this.color_index = color_index;
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.sex = sex;
        this.bdate = bdate;
        this.adress = adress;
        this.last_seen = last_seen;
        this.education = education;
        this.has_photo = has_photo;
        this.interests = interests;
        this.groups = groups;
        this.friends = friends;
        this.followers = followers;
        this.phone_number = phone_number;
        this.counters = counters;
        this.is_closed = is_closed;
    }
}

