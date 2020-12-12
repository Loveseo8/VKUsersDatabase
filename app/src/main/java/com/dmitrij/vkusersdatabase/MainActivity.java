package com.dmitrij.vkusersdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.user_info);

        Gson gson = new Gson();
        String key = "8a47bbea8a47bbea8a47bbeafa8a32c9f088a478a47bbead59347e438c8ab96e520554f";
        String user_id = "368468514";
        String json = "";
        String strUrl = "https://api.vk.com/method/users.get?user_ids=" + user_id + "&fields=bdate&access_token=" + key + "&v=5.126";

            try {

                URL url = new URL(strUrl);
                URLConnection yc = url.openConnection();
                yc.connect();
                BufferedReader buffIn = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;
                while ((inputLine = buffIn.readLine()) != null) json = (inputLine);
                buffIn.close();

            } catch (IOException | NullPointerException e) {

                e.printStackTrace();

            }

        Response response;

            response = gson.fromJson(json, Response.class);

            textView.setText(response.user.first_name);

        }

    }


class Response {

    User user;

}

class User {

    public int id;
    public String bdate;
    public String first_name;
    public String last_name;
    public String connections;
    public Boolean is_closed;

}