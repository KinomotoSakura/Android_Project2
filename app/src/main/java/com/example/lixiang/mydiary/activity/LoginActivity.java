package com.example.lixiang.mydiary.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.callback.OnLogInCallback;
import com.example.lixiang.mydiary.model.User;
import com.example.lixiang.mydiary.model.Document;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private Button mBtnRegister;
    private EditText mEtUser;
    private EditText mEtPwd;
    private String mUser;
    private String mPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("请登录");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInput()){

                    return;
                }
                User.logInInBackground(mUser, mPwd, new OnLogInCallback() {
                    @Override
                    public void done(User user, AVException e) {
                        if(e == null){

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("from","LoginActivity");
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mBtnRegister = (Button) findViewById(R.id.register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView tvFp = (TextView) findViewById(R.id.tv_forget_password);
        tvFp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mEtUser = (EditText) findViewById(R.id.et_user);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
    }

    private boolean checkInput() {
        mUser = mEtUser.getText().toString();
        mPwd = mEtPwd.getText().toString();
        if (mUser.equals("") || mPwd.equals("")){
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
