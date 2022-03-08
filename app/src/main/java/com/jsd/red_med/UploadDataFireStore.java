package com.jsd.red_med;

public class UploadDataFireStore {

    String med_type,med_name,id, time;

    public UploadDataFireStore() {
    }

    UploadDataFireStore(String med_type, String med_name, String id, String time){
        this.med_type = med_type;
        this.med_name = med_name;
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }
    public String getMed_type() {
        return med_type;
    }

    public String getMed_name() {
        return med_name;
    }

    public String getTime() {
        return time;
    }
}
