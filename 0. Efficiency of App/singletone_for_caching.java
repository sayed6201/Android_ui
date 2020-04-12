package com.sayed.ahmed.vu;

import android.util.Log;

public class Dataholder {

    private int currentPageNum = 0;
    private int totalPageNum = 1;


    private static final Dataholder holder = new Dataholder();

    public static Dataholder getInstance() {return holder;}

    public int getCurrentPageNumber() {return this.currentPageNum;}
    public void setCurrentPageNumber(int page) {this.currentPageNum = page;}

    public int getTotalPageNumber() {return this.totalPageNum;}
    public void setTotalPageNumber(int page) {this.totalPageNum = page;}

    public boolean hasMoreData() {
        Log.d("DATA_HOLDER","currentPageNum: "+currentPageNum+" totalPageNum: "+totalPageNum);
        return this.currentPageNum <= this.totalPageNum;
    }

}
