package com.dmitrij.vkusersdatabase;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    String key = "8a47bbea8a47bbea8a47bbeafa8a32c9f088a478a47bbead59347e438c8ab96e520554f";
    String json = "";
    DBHandler db;
    EditText enter_id;
    Button button_insert;
    Button button_show;
    Button button_search;
    Button button_sort;
    ArrayList<String> res = new ArrayList<>();


    class VKTask extends AsyncTask<Integer, Integer, Void> {


        @SuppressLint("ResourceType")
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

            String color_index = null;
            int id = 0;
            String last_name = null;
            String first_name = null;
            int gender = 0;
            String university_name = null;
            String faculty_name = null;
            String bdate = "";
            String adress = "";
            String last_seen = "";
            String education = "";
            String interests = "";
            int groups = 0;
            int friends = 0;
            int followers = 0;
            String phone_number = "";
            String counters = "";
            boolean is_closed = false;
            String sex = "";
            String age = "";
            int photos = 0;
            String photo = "";

            JSONObject reader = new JSONObject(json);
            JSONArray jsonArray = reader.getJSONArray("response");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getInt("id");
                    last_name = jsonObject.getString("last_name");
                    first_name = jsonObject.getString("first_name");
                    bdate = jsonObject.getString("bdate");
                    gender = jsonObject.getInt("sex");

                } catch (JSONException e) {

                }
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    counters = jsonObject.getString("counters");
                    adress = jsonObject.getString("city");
                    last_seen = jsonObject.getString("last_seen");
                    university_name = jsonObject.getString("university_name");
                    faculty_name = jsonObject.getString("faculty_name");
                    interests = jsonObject.getString("interests");
                    phone_number = jsonObject.getString("phone_number");
                    is_closed = jsonObject.getBoolean("is_closed");

                } catch (JSONException e) {

                }
            }

            if (first_name != "DELETED") {


                if (adress != "") {

                    JSONObject read = new JSONObject(adress);
                    adress = read.getString("title");

                }

                if (last_seen != "") {


                    JSONObject d = new JSONObject(last_seen);
                    int time = d.getInt("time");

                    Date lst = new Date((long) time * 1000);

                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    last_seen = dateFormat.format(lst);

                }

                if (bdate != "") {

                    String[] bday = bdate.split("\\.");

                    if (bday.length == 3) {

                        LocalDate birthdate = new LocalDate(Integer.parseInt(bday[2]), Integer.parseInt(bday[1]), Integer.parseInt(bday[0]));
                        LocalDate now = new LocalDate();
                        Years years = Years.yearsBetween(birthdate, now);

                        age = years.toString().replaceAll("P", "").replaceAll("Y", "");

                    }
                }

                int duration = 0;

                if (last_seen != "") {

                    String[] b = last_seen.split("\\.");

                    if (b.length == 3) {

                        LocalDate birthdate = new LocalDate(Integer.parseInt(b[2]), Integer.parseInt(b[1]), Integer.parseInt(b[0]));
                        LocalDate now = new LocalDate();
                        Years years = Years.yearsBetween(birthdate, now);

                        duration = Integer.parseInt(years.toString().replaceAll("P", "").replaceAll("Y", ""));

                    }
                }


                if (counters != "") {

                    JSONObject r = new JSONObject(counters);
                    followers = r.getInt("followers");
                    photos = r.getInt("photos");

                }

                if (university_name != "") education = university_name;
                else if (faculty_name != "" && university_name != "")
                    education = university_name + ",  " + faculty_name;

                if (gender == 1) sex = "Ж";
                else if (gender == 2) sex = "М";


                try {
                    URL url = new URL("https://api.vk.com/method/friends.get?user_id=" + userID + "&access_token=" + key + "&v=5.126");
                    URLConnection yc = url.openConnection();
                    yc.connect();
                    BufferedReader buffIn = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    while ((inputLine = buffIn.readLine()) != null) json = (inputLine);
                    buffIn.close();

                } catch (IOException | NullPointerException e) {

                    e.printStackTrace();

                }

                if (json != null) {

                    JSONObject ree = new JSONObject(json).getJSONObject("response");
                    friends = ree.getInt("count");

                }


                if (photos > 0) photo = "ДА";
                else photo = "НЕТ";

                try {
                    URL url = new URL("https://api.vk.com/method/users.getSubscriptions?user_id=" + userID + "&access_token=" + key + "&v=5.126");
                    URLConnection yc = url.openConnection();
                    yc.connect();
                    BufferedReader buffIn = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    while ((inputLine = buffIn.readLine()) != null) json = (inputLine);
                    buffIn.close();

                } catch (IOException | NullPointerException e) {

                    e.printStackTrace();

                }

                JSONObject l = new JSONObject(json).getJSONObject("response").getJSONObject("groups");
                groups = l.getInt("count");


                if (is_closed) color_index = "синий";
                else if (friends == 0 || photo == "НЕТ" || duration >= 1)
                    color_index = "красный";

                else if (age != "" && (Integer.parseInt(age) < 16 || Integer.parseInt(age) > 100))
                    color_index = "красный";
                else color_index = "зелёный";
            } else color_index = "красный";


            response = new Response(color_index, id, last_name, first_name, sex, age, adress, last_seen, education, photo, interests, groups, friends, followers, phone_number, is_closed);

            return response;

        }

        @Override
        protected Void doInBackground(Integer... users) {
            for (int userID : users) {
                try {
                    ArrayList<String> res = new ArrayList<>();
                    res = db.getRecords();
                    int count = 0;

                    for (int i = 0; i < res.size(); i++) {

                        String[] ids = res.get(i).split("   ");

                        if (ids[2].contains(String.valueOf(userID))) {

                            count++;
                        }

                    }

                    if (count == 0)
                        db.insertRecord(getInfoByID(userID).color_index, getInfoByID(userID).id, getInfoByID(userID).last_name, getInfoByID(userID).first_name, getInfoByID(userID).sex, getInfoByID(userID).age, getInfoByID(userID).city, getInfoByID(userID).last_seen, getInfoByID(userID).education, getInfoByID(userID).has_photo, getInfoByID(userID).interests, getInfoByID(userID).groups, getInfoByID(userID).friends, getInfoByID(userID).followers, getInfoByID(userID).phone_number);

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
        button_insert = findViewById(R.id.button_insert);
        button_show = findViewById(R.id.button_show);
        button_search = findViewById(R.id.button_search);
        button_sort = findViewById(R.id.button_sort);

        tableLayout = (TableLayout) findViewById(R.id.tablelayout);


        button_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    ArrayList<String> a = new ArrayList<>();

                    res = db.getRecords();
                    a = (ArrayList<String>) res.stream().filter(s -> s.contains("М")).collect(Collectors.toList());


                    tableLayout.removeViews(2, tableLayout.getChildCount() - 2);


                    for (int i = 0; i < a.size(); i++) {

                        TableRow row = new TableRow(getApplicationContext());
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                        String[] colText = a.get(i).split("   ");
                        for (String text : colText) {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(text);
                            row.addView(tv);
                        }
                        tableLayout.addView(row);

                    }

                }
        });


        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Random rn = new Random();
                VKTask task = new VKTask();
                task.execute(rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1, rn.nextInt(630000000) + 1);

            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    tableLayout.removeViews(2, tableLayout.getChildCount() - 2);

                    String str = enter_id.getText().toString();

                    if (str != null) {

                        ArrayList<String> search = new ArrayList<>();

                        res = db.getRecords();
                        search = (ArrayList<String>) res.stream().filter(s -> s.contains(str)).collect(Collectors.toList());

                        enter_id.setText("");

                        if (search.size() > 0) {

                            for (int i = 0; i < search.size(); i++) {


                                TableRow row = new TableRow(getApplicationContext());
                                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                String[] colText = search.get(i).split("   ");
                                for (String text : colText) {
                                    TextView tv = new TextView(getApplicationContext());
                                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT));
                                    tv.setGravity(Gravity.CENTER);
                                    tv.setTextSize(16);
                                    tv.setPadding(5, 5, 5, 5);
                                    tv.setText(text);
                                    row.addView(tv);
                                }
                                tableLayout.addView(row);

                            }

                        }
                    }

                }
        });


        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    tableLayout.removeViews(2
                            , tableLayout.getChildCount() - 2);

                    res.clear();

                    res = db.getRecords();
                    for (int i = 0; i < res.size(); i++) {

                        TableRow row = new TableRow(getApplicationContext());
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                        String[] colText = res.get(i).split("   ");
                        for (String text : colText) {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(text);
                            row.addView(tv);
                        }
                        tableLayout.addView(row);

                    }
                }

        });

    }
}

class Response{

    String color_index;
    int id;
    String last_name;
    String first_name;
    String sex;
    String age;
    String city;
    String last_seen;
    String education;
    String has_photo;
    String interests;
    int groups;
    int friends;
    int followers;
    String phone_number;
    boolean is_closed;

    //НА СТРАНИЦАХ ПОЛЬЗОВАТЕЛЕЙ НЕ ОТОБРАЖАЕТСЯ АДРЕС ЭЛЕКТРОННОЙ ПОЧТЫ И ОПЫТ РАБОТЫ. МЕТОДОВ ДЛЯ ПОЛУЧЕНИЯ ЭТИХ ДАННЫХ ОТСУТВУЮТ У VK api


    public Response(String color_index, int id, String last_name, String first_name, String sex, String age, String city, String last_seen, String education, String has_photo, String interests, int groups, int friends, int followers, String phone_number, boolean is_closed) {
        this.color_index = color_index;
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.sex = sex;
        this.age = age;
        this.city = city;
        this.last_seen = last_seen;
        this.education = education;
        this.has_photo = has_photo;
        this.interests = interests;
        this.groups = groups;
        this.friends = friends;
        this.followers = followers;
        this.phone_number = phone_number;
        this.is_closed = is_closed;
    }
}

