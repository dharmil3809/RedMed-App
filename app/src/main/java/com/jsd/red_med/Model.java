package com.jsd.red_med;

public class Model {

    String str_type, str_name, id, time;

    public Model(String str_name, String str_type, String time, String id) {
        this.str_name = str_name;
        this.str_type = str_type;
        this.time = time;
        this.id = id;
    }


    public String getStr_type() {
        return str_type;
    }

    public void setStr_type(String str_type) {
        this.str_type = str_type;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

