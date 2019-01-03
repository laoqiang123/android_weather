package com.example.administrator.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/12/30 0030.
 */
public class ChooseCityActivity extends AppCompatActivity {
    private ExpandableListView elv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecity_layout);
        final Map<String, Map<String, List<String>>> data = getJsonData(readCityJson());
        elv = (ExpandableListView) findViewById(R.id.expandible_listview);
        ThreeLevelListAdapter adapter = new ThreeLevelListAdapter(ChooseCityActivity.this, data);
        elv.setAdapter(adapter);
        elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    elv.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }


    public String readCityJson() {
        InputStream is = null;
        StringBuffer sb = new StringBuffer();
        try {
            is = getAssets().open("cities.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String content;
            while ((content = br.readLine()) != null) {
                sb.append(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }



    public Map<String, Map<String, List<String>>> getJsonData(String json) {
        JSONArray jsonArray = null;
        String city = null;
        List<String> counties = new ArrayList<>();
        Map<String, List<String>> cityAndCounties = null;
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        try {
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                cityAndCounties = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String province = jsonObject.getString("name");
                JSONArray jsonArray1 = jsonObject.getJSONArray("cities");
                int pre = -1;
                for (int k = 0; k < jsonArray1.length(); k++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                    String c = jsonObject1.getString("c");
                    String n = jsonObject1.getString("n");
                    if (c.substring(c.length() - 2).equals("00")) {
                        cityAndCounties.put(n, null);//直辖市没有区，默认为空
                    } else {
                        if (Integer.parseInt(c.subSequence(c.length() - 4, c.length() - 2).toString()) == pre + 1) {
                            cityAndCounties.put(city, counties);
                            counties = null;
                            counties = new ArrayList<>();
                            pre = Integer.parseInt(c.subSequence(c.length() - 4, c.length() - 2).toString());
                        }
                        if (c.substring(c.length() - 2).equals("01")) {
                            city = n;
                            pre = Integer.parseInt(c.subSequence(c.length() - 4, c.length() - 2).toString());

                        } else {
                            counties.add(n);
                        }
                    }

                }
                map.put(province, cityAndCounties);
                cityAndCounties = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;

    }
}
