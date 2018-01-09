package com.example.lixiang.mydiary;

import android.app.Application;
import android.view.Window;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.example.lixiang.mydiary.model.DiaryLc;
import com.example.lixiang.mydiary.model.Document;
import com.example.lixiang.mydiary.model.User;

import java.io.IOException;

public class MyApplication extends Application{

    private static Document mDocument;
    public static final  int MOD_EDIT = 0;
    public static final  int MOD_VIEW = 1;
    private static int mCurMod = MOD_EDIT;

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(User.class);
        AVObject.registerSubclass(DiaryLc.class);
        AVOSCloud.initialize(this, "d8sfcRI2r8wsnakEnSyljGm2-gzGzoHsz", "SmTS3sgmLq9hwlaKEUOWPxCg");

        initDoc();
    }

    protected void initDoc(){
        try {
            mDocument = new Document(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document getDoc(){
        return mDocument;
    }

    public static int getCurMod(){
        return mCurMod;
    }

    public static void setCurMod(int mod){
        mCurMod = mod;
    }
}
