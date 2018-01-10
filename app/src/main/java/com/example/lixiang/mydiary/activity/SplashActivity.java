package com.example.lixiang.mydiary.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.model.User;
import com.example.lixiang.mydiary.util.ThemeUtils;
import com.example.lixiang.mydiary.util.cache.ACache;
import com.example.lixiang.mydiary.util.constant.Constant;

public class SplashActivity extends AppCompatActivity {
    private ACache aCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        aCache = ACache.get(this);

        new SplashTask().execute();

    }

    class SplashTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
            if(gesturePassword == null || "".equals(gesturePassword)) {
                if (!User.isLogin()){
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    intent.putExtra("from","SplashActivity");
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(SplashActivity.this, GestureLoginActivity.class);
                startActivity(intent);
            }

            finish();
        }
    }
}
