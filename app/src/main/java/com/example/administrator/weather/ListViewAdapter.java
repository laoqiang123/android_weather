package com.example.administrator.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2019/1/1 0001.
 */
public class ListViewAdapter extends BaseAdapter {
    private WeatherInfo data;
    private Context context;


    @Override
    public int getCount() {
        return data.getList().size();
    }

    public WeatherInfo getData() {
        return data;
    }

    public void setData(WeatherInfo data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public ListViewAdapter(WeatherInfo data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
        TextView info = (TextView) convertView.findViewById(R.id.info);
        List<Weather> list = data.getList();
        Weather w = list.get(position);
        String[] content = w.getLow().split("\\s+");
        String low = content[content.length-1];
        String[] content1 = w.getHigh().split("\\s+");
        String high = content1[content1.length-1];
        info.setText(low + "~"+high+","+w.getType()+","+w.getFx()+","+w.getFl());
        return convertView;
    }
}
