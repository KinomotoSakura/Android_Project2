package com.example.lixiang.mydiary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVUser;
import com.example.lixiang.mydiary.MyApplication;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.activity.FeedbackActivity;
import com.example.lixiang.mydiary.model.User;

public class Controller {
    private Context context;
    private Activity mActivity;
    private LinearLayout mLlySetting;
    private LinearLayout mLlyFeedBack;
    private LinearLayout mLlyLicense;
    private LinearLayout mLlyAbout;
    private LinearLayout mLlyLogout;
    private LinearLayout mLlyExit;

    Controller(Context context){
        this.context = context;
        mActivity = (Activity)context;
        init();
        isLogin();
    }

    private void init(){
        mLlySetting = (LinearLayout)mActivity.findViewById(R.id.lly_setting);
        mLlySetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mLlySetting.setBackgroundResource(R.drawable.nav_item_pressed);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    mLlySetting.setBackgroundResource(R.drawable.nav_item_normal);
                    Intent intent = new Intent(mActivity,SettingActivity.class);
                    mActivity.startActivity(intent);
                }
                return true;
            }
        });

        mLlyFeedBack = (LinearLayout)mActivity.findViewById(R.id.lly_feedback);
        mLlyFeedBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLlyFeedBack.setBackgroundResource(R.drawable.nav_item_pressed);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    mLlyFeedBack.setBackgroundResource(R.drawable.nav_item_normal);
                    Intent intent = new Intent(mActivity, FeedbackActivity.class);
                    mActivity.startActivity(intent);
                }
                return true;
            }
        });

        mLlyLicense = (LinearLayout) mActivity.findViewById(R.id.lly_share);
        mLlyLicense.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLlyLicense.setBackgroundResource(R.drawable.nav_item_pressed);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    mLlyLicense.setBackgroundResource(R.drawable.nav_item_normal);
                    String subject = "My Diary";
                    String content = "My Diary真是好";
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    context.startActivity(Intent.createChooser(shareIntent, "分享到"));
                }
                return true;
            }
        });

        mLlyAbout = (LinearLayout)mActivity.findViewById(R.id.lly_about);
        mLlyAbout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLlyAbout.setBackgroundResource(R.drawable.nav_item_pressed);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    mLlyAbout.setBackgroundResource(R.drawable.nav_item_normal);
                    Intent intent = new Intent(mActivity, AboutActivity.class);
                    mActivity.startActivity(intent);
                }
                return true;
            }
        });

        mLlyLogout = (LinearLayout)mActivity.findViewById(R.id.lly_logout);
        mLlyLogout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLlyLogout.setBackgroundResource(R.drawable.nav_item_pressed);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    mLlyLogout.setBackgroundResource(R.drawable.nav_item_normal);
                    AVUser.logOut();
                    MyApplication.getDoc().deleteFile();
                    Intent intent = new Intent(mActivity,LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
                return true;
            }
        });

        mLlyExit = (LinearLayout)mActivity.findViewById(R.id.lly_exit);
        mLlyExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLlyExit.setBackgroundResource(R.drawable.nav_item_pressed);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    mLlyExit.setBackgroundResource(R.drawable.nav_item_normal);
                    mActivity.finish();
                }
                return true;
            }
        });
    }

    public void isLogin(){

        if(User.isLogin()){
            mLlyLogout.setVisibility(View.VISIBLE);
        }else{
            mLlyLogout.setVisibility(View.GONE);
        }
    }

}
