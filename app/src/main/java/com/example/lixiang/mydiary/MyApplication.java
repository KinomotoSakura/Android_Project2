package com.example.lixiang.mydiary;

import android.app.Application;

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

    public void onCreate() {
        super.onCreate();
    
        //@Override
        AVObject.registerSubclass(User.class);
        AVObject.registerSubclass(DiaryLc.class);
        AVOSCloud.initialize(this, "nK17LMPhBdxjkxmO8gqe5gRL-gzGzoHsz", "eteDVv9C8TCU8Csd2pHHCEpv");

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
