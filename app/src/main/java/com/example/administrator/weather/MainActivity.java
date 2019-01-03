package com.example.administrator.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final private int city = 110;
    final private int about = 111;
    private AlertDialog alert = null;
    private ListView listview;
    private AlertDialog.Builder builder = null;
    private ImageView refresh;
    private TextView tv_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences sp = MainActivity.this.getSharedPreferences("weather", Context.MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.listview);
        refresh = (ImageView) findViewById(R.id.refresh);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setText(sp.getString("weather", "常熟"));
         String result = null;
        try {
            result = HttpUtil.getResult("http://wthrcdn.etouch.cn/weather_mini?city=" + URLEncoder.encode(sp.getString("weather", "常熟"), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
     refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewAdapter adapter = null;
                try {
                    adapter = new ListViewAdapter(getWeatherJson(HttpUtil.getResult("http://wthrcdn.etouch.cn/weather_mini?city=" + URLEncoder.encode(sp.getString("weather", "常熟"), "utf-8"))), MainActivity.this);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_city.setText(sp.getString("weather", "常熟"));
                listview.setAdapter(adapter);
                listview.deferNotifyDataSetChanged();

            }
        });


        ListViewAdapter adapter = new ListViewAdapter(getWeatherJson(result), MainActivity.this);
        listview.setAdapter(adapter);
    }

    public WeatherInfo getWeatherJson(String json) {
        WeatherInfo wi = new WeatherInfo();
        List<Weather> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("yesterday");
            String city = jsonObject1.getString("city");
            wi.setCity(city);
            Weather w1 = new Weather();
            list.add(getYesterdayWeather(w1, jsonObject2));
            JSONArray jsonArray = jsonObject1.getJSONArray("forecast");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                w1 = null;
                w1 = new Weather();
                list.add(getforecastWeather(w1, jsonObject3));
            }
            wi.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wi;

    }
    public Weather getYesterdayWeather(Weather w, JSONObject jsonObject) {
        try {
            w.setDate(jsonObject.getString("date"));
            String s = jsonObject.getString("fl");
            String fl = s.substring(s.length()-5,s.length()-3);
            w.setFl(fl);
            w.setFx(jsonObject.getString("fx"));
            w.setHigh(jsonObject.getString("high"));
            w.setLow(jsonObject.getString("low"));
            w.setType(jsonObject.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return w;
    }


    public Weather getforecastWeather(Weather w, JSONObject jsonObject) {
        try {
            w.setDate(jsonObject.getString("date"));
            String s = jsonObject.getString("fengli");
            String fl = s.substring(s.length()-5,s.length()-3);
            w.setFl(fl);
            w.setFx(jsonObject.getString("fengxiang"));
            w.setHigh(jsonObject.getString("high"));
            w.setLow(jsonObject.getString("low"));
            w.setType(jsonObject.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return w;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, city, 1, "city");
        menu.add(1, about, 2, "about");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case city:
                Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
                startActivity(intent);
                break;
            case about:
                builder = new AlertDialog.Builder(MainActivity.this);
                alert = builder
                        .setTitle("学生信息：")
                        .setMessage("zb1017107-李利民")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
