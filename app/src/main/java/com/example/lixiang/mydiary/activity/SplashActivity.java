package com.example.lixiang.mydiary.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.model.User;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new SplashTask().execute();

    }

    class SplashTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{

                Thread.sleep(5000);

            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            SharedPreferences preference_setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (!User.isLogin()){

                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);

            } else {

                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("from","SplashActivity");
                startActivity(intent);
            }
            finish();
        }
    }
}
