package com.example.lixiang.mydiary.model;

import java.io.Serializable;
import java.util.Calendar;

public class Diary implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private String mDate;
    private String mWeek;
    private String mContent;

    /**
     * 是否置顶
     */
    public int top;
    /**
     * 置顶时间
     **/
    public long time;

    public String toString() {
        return "Time:" + mDate + "Week:" + mWeek + "Content:" + mContent;
    }

    public Diary(String date,String week ,String content) {
        this.mDate = date;
        this.mWeek = week;
        this.mContent = content;
    }

    public String getDate() {
        return mDate;
    }

    public String getWeek(){
        return mWeek;
    }

    public String getContent() {
        return mContent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    @Override
    public int compareTo(Object another) {
        if (another == null || !(another instanceof Diary)) {
            return -1;
        }
        Diary otherSession = (Diary) another;
        int result = 0 - (top - otherSession.getTop());
        if (result == 0) {
            result = 0 - compareToTime(time, otherSession.getTime());
        }
        return result;
    }

    /**
     * 根据时间对比
     * */
    public static int compareToTime(long lhs, long rhs) {
        Calendar cLhs = Calendar.getInstance();
        Calendar cRhs = Calendar.getInstance();
        cLhs.setTimeInMillis(lhs);
        cRhs.setTimeInMillis(rhs);
        return cLhs.compareTo(cRhs);
    }

}