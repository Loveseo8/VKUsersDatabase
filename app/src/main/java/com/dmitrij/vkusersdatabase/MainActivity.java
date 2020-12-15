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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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
                URL url = new URL("https://api.vk.com/method/users.get?user_ids=" + userID + "&fields=bdate&access_token=" + key + "&v=5.126");
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

            String first_name = null;
            int id = 0;
            String last_name = null;
            boolean can_access_closed = false;
            boolean is_closed = false;
            String bdate = null;

            JSONObject reader = new JSONObject(json);
            JSONArray jsonArray = reader.getJSONArray("response");
            for(int i = 0; i < jsonArray.length(); i++){
                try{

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    first_name = jsonObject.getString("first_name");
                    id = jsonObject.getInt("id");
                    last_name = jsonObject.getString("last_name");
                    can_access_closed =jsonObject.getBoolean("can_access_closed");
                    is_closed = jsonObject.getBoolean("is_closed");
                    bdate = jsonObject.getString("bdate");

                }catch (JSONException e){

                }
            }

            response = new Response(first_name, id, last_name, can_access_closed, is_closed, bdate);
            

            return response;

        }
        @Override
        protected Void doInBackground(Integer... users) {
            for (int userID: users){
                try {
                    db.insertRecord(getInfoByID(userID).first_name, getInfoByID(userID).last_name);
                    Log.d("TAGA", getInfoByID(userID).first_name+ " " + getInfoByID(userID).last_name);
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

    String first_name;
    int id;
    String last_name;
    boolean can_access_closed;
    boolean is_closed;
    String bdate;


    public Response(String first_name, int id, String last_name, boolean can_access_closed, boolean is_closed, String bdate) {
        this.first_name = first_name;
        this.id = id;
        this.last_name = last_name;
        this.can_access_closed = can_access_closed;
        this.is_closed = is_closed;
        this.bdate = bdate;
    }
}
