package com.example.lixiang.mydiary.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.example.lixiang.mydiary.MyApplication;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.model.Diary;
import com.example.lixiang.mydiary.model.User;

/**
 * Created by Aroya on 1/8/2018.
 */
public class CalendarFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    public static List<Diary> mList;

    //constructor
    public CalendarFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    //notifyDataChange操作触发
    @Override
    public void onDataSetChanged() {
        if(User.isLogin()){
            mList=MyApplication.getDoc().getDiaryManager().getList();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(new Date());
            for(int i=0;i<mList.size();i++){
                if(!mList.get(i).getDate().equals(date)){
                    mList.remove(i);
                    i--;
                }
            }
        }
        else{
            mList = new ArrayList();
            mList.add(new Diary("请登录","请登录","***"));
        }
    }

    @Override
    public void onDestroy() {
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //创建当前索引位置的View
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);

        //设置显示内容
        rv.setTextViewText(R.id.widget_date, mList.get(position).getDate());
        rv.setTextViewText(R.id.widget_week,mList.get(position).getWeek());
        rv.setTextViewText(R.id.widget_content,mList.get(position).getContent());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }
}
