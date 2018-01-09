package com.example.lixiang.mydiary.widget;


import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Aroya on 1/8/2018.
 */


public class CalendarWidgetUpdate extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CalendarFactory(this.getBaseContext(), intent);
    }
}
