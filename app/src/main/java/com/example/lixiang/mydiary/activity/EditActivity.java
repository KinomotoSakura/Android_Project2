package com.example.lixiang.mydiary.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lixiang.mydiary.MyApplication;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.model.Diary;
import com.example.lixiang.mydiary.model.Document;
import com.example.lixiang.mydiary.utils.ThemeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;

public class EditActivity extends AppCompatActivity {
    private EditText mEditText;
    private TextView mDate;
    private Button mClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }
    protected void initView(){
        Document mDocument = MyApplication.getDoc();
        int diaryCnt = mDocument.getDiaryManager().getDiaryCnt();
        Diary diary = mDocument.getDiaryManager().getDiary(diaryCnt - 1);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        mDate = (TextView) findViewById(R.id.edit_date);
        mDate.setText("今天 "+ formatter.format(new Date()));
        mEditText = (EditText) findViewById(R.id.edit_text);
        mClear = (Button) findViewById(R.id.edit_clear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });
        Intent intent = getIntent();
        String data = intent.getStringExtra("diary_content");
        mEditText.setText(data);
        if (MyApplication.getCurMod() == MyApplication.MOD_EDIT){
            mEditText.setEnabled(true);
            setTitle("编辑日记");
        }else{
            mEditText.setKeyListener(null);
            setTitle("查看日记");
            mClear.setVisibility(GONE);
            mDate.setText(diary.getDate()+" "+diary.getWeek());
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem item = menu.findItem(R.id.action_ok);
        if (MyApplication.getCurMod() == MyApplication.MOD_VIEW){
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_ok:
                prepareData();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void prepareData(){
        String data = mEditText.getText().toString();
        if (data.equals("")){
            Toast.makeText(EditActivity.this,"日记为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("data_return", data);
        setResult(RESULT_OK,intent);
    }
}
