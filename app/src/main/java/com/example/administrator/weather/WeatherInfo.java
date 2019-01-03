package com.example.administrator.weather;

import java.util.List;

/**
 * Created by Administrator on 2019/1/1 0001.
 */
public class WeatherInfo {
    private String city;
    private List<Weather> list;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Weather> getList() {
        return list;
    }

    public void setList(List<Weather> list) {
        this.list = list;
    }
}
