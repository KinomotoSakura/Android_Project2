package com.example.lixiang.mydiary.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.example.lixiang.mydiary.MyApplication;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.activity.EditActivity;
import com.example.lixiang.mydiary.activity.MainActivity;
import com.example.lixiang.mydiary.activity.SplashActivity;


public class CalendarWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        //获取Widget的组件名
        ComponentName thisWidget = new ComponentName(context,
                CalendarWidget.class);

        //创建一个RemoteView
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.calendar_widget);

        //绑定CalendarWidgetUpdate
        Intent intent = new Intent(context, CalendarWidgetUpdate.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

        //设置适配器
        remoteViews.setRemoteAdapter(R.id.calendarWidget, intent);

        //默认为空的View
        remoteViews.setEmptyView(R.id.calendarWidget, R.layout.none_data);

        //点击唤醒app
        MyApplication.setCurMod(MyApplication.MOD_EDIT);
        Intent toApp = new Intent(context,EditActivity.class);
        //startActivityForResult(toApp,MyApplication.MOD_EDIT);

        PendingIntent pendingIntent=PendingIntent.getActivity(
                context,0,toApp,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widgetIconLayout,pendingIntent);

        //更新Widget
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
    //创建Widget
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    //溢出Widget
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    //receive intent
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int action=intent.getIntExtra("Action",-1);
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context,CalendarWidget.class);
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.calendar_widget);
        switch (action){
            case -1://默认刷新

                //调用Serivce中Factory的onDataSetChanged()
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                        R.id.calendarWidget);
                break;
            case 0://显示模式切换：显示日记
                remoteViews.setViewVisibility(R.id.calendarWidget,View.VISIBLE);
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                        R.id.calendarWidget);
                mgr.updateAppWidget(cn,remoteViews);
                break;
            case 1://显示模式切换：显示按钮
                remoteViews.setViewVisibility(R.id.calendarWidget, View.GONE);
                mgr.updateAppWidget(cn,remoteViews);
        }

    }

}