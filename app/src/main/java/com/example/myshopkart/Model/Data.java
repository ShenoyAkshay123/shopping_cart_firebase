package com.example.myshopkart.Model;

public class Data {

    private String mType;
    private int mAmount;
    private String mNote;
    private String Date;
    private String id;

    public Data() {

    }

    public Data(String mType, int mAmount, String mNote, String date, String id) {
        this.mType = mType;
        this.mAmount = mAmount;
        this.mNote = mNote;
        Date = date;
        this.id = id;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public int getmAmount() {
        return mAmount;
    }

    public void setmAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
