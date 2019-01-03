package com.example.administrator.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private  Map<String,Map<String,List<String>>> data;
    private List<String> provinceslist = new ArrayList<>();//存放省的数组
    private SharedPreferences sp =null;

    public ThreeLevelListAdapter(Context context ,Map<String,Map<String,List<String>>> data) {
        this.context = context;
        this.data = data;
        initData(data);
    }
    public void initData(Map<String,Map<String,List<String>>> data){
        sp = context.getSharedPreferences("weather",Context.MODE_PRIVATE);
        Set<String> provinces = data.keySet();
        Iterator<String> it = provinces.iterator();
        while(it.hasNext()){
            provinceslist.add(it.next());
        }
    }


    @Override
    public int getGroupCount() {
        return provinceslist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        // no idea why this code is working

        return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {


        return child;


    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_first, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowParentText);
        text.setText(this.provinceslist.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);
        Log.e("ffff",provinceslist.get(groupPosition)+"hhh");
        Map<String,List<String>> childData = data.get(provinceslist.get(groupPosition));//根据省份去获取城市、区信息
        final List<String> cities = new ArrayList<>();//存放城市信息
        final List<List<String>> countries = new ArrayList<List<String>>();//存放country
        Set<String> cs = childData.keySet();
        Iterator<String> it = cs.iterator();
        while(it.hasNext()){
            String key = it.next();
            cities.add(key);
            countries.add(childData.get(key));
        }
        secondLevelELV.setAdapter(new SecondLevelAdapter(context, cities, countries));
        secondLevelELV.setGroupIndicator(null);
       secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
           int previousGroup = -1;

           @Override
           public void onGroupExpand(int groupPosition) {
               if (groupPosition != previousGroup)
                   secondLevelELV.collapseGroup(previousGroup);
               previousGroup = groupPosition;
           }
       });
        final SharedPreferences.Editor ed = sp.edit();
        secondLevelELV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,final  int groupPosition, long id) {
                AlertDialog.Builder builder = null;
                AlertDialog alert = null;
                Log.e("log", cities.get(groupPosition).toString());
                builder = new AlertDialog.Builder(context);
                alert = builder
                        .setTitle("您选择的城市：")
                        .setMessage(cities.get(groupPosition).toString())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ed.putString("weather",cities.get(groupPosition).toString());
                                ed.commit();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alert.show();
                return false;
            }
        });
       secondLevelELV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition,final  int childPosition, long id) {
               AlertDialog.Builder builder = null;
               AlertDialog alert = null;
               Log.e("log", cities.get(groupPosition).toString());
               builder = new AlertDialog.Builder(context);
               alert = builder
                       .setTitle("您选择的区/镇：")
                       .setMessage(countries.get(groupPosition).get(childPosition).toString())
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               ed.putString("weather",countries.get(groupPosition).get(childPosition).toString());
                               ed.commit();
                           }
                       }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       }).create();
               alert.show();
               return true;
           }
       });


        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}