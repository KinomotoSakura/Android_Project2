package com.example.lixiang.mydiary.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.bonet.views.BtCalendarView;
import com.bonet.views.OnDateSelectedListener;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.adapter.MyPagerAdapter;
import com.example.lixiang.mydiary.adapter.RecyclerViewAdapter;
import com.example.lixiang.mydiary.callback.OnLogInCallback;
import com.example.lixiang.mydiary.listener.loadDataListener;
import com.example.lixiang.mydiary.model.User;
import com.example.lixiang.mydiary.model.Document;
import com.example.lixiang.mydiary.model.Diary;
import com.example.lixiang.mydiary.MyApplication;
import com.example.lixiang.mydiary.utils.ThemeUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements loadDataListener {
    private Document mDocument;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private ArrayList<View> mViewList;
    private View mLlyRecyclerView;
    private View mLlyCalender;
    private RecyclerViewAdapter mRVAdapter;
    private Controller mController;
    private ActionBarDrawerToggle mDrawerToggle;
    public static Activity instance = null;
    private String week [] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadData();

        initDrawerLayout();
        initViewPager();
        initRecyclerView();
        initCalenderView();

        AVAnalytics.trackAppOpened(getIntent());
    }

    public void init(){
        onCreate(null);
    }

    protected void loadData(){
        mDocument = MyApplication.getDoc();
        Intent intent = getIntent();
        String str = "OtherActivity";
        // This check is very important in avoiding system crashing;
        if(intent.getStringExtra("from") != null){
            str = intent.getStringExtra("from");
        }
        if (str.equals("LoginActivity")){
            mDocument.deleteFile();
            mDocument.load(this);
        }else if(str.equals("OtherActivity") && intent.getStringExtra("from") != null){
            try{
                mDocument.readFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            mDocument.deleteFile();
            mDocument.load(this);
        }
    }

    protected void initDrawerLayout(){
        mController = new Controller(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,0,0){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    protected void initRecyclerView(){
        RecyclerView recyclerView = (RecyclerView)mLlyRecyclerView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVAdapter = new RecyclerViewAdapter(MyApplication.getDoc().getDiaryManager().getList());
        mRVAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view,Diary diary) {
                MyApplication.setCurMod(MyApplication.MOD_VIEW);
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("diary_content",diary.getContent());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mRVAdapter);

    }

    protected void initCalenderView(){
        final CardView cardView = (CardView)mLlyCalender.findViewById(R.id.lly_item);
        final TextView tvDate = (TextView) mLlyCalender.findViewById(R.id.tv_date);
        final TextView tvWeek = (TextView) mLlyCalender.findViewById(R.id.tv_week);
        final TextView tvContent = (TextView) mLlyCalender.findViewById(R.id.tv_content);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.setCurMod(MyApplication.MOD_VIEW);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("diary_content", tvContent.getText());
                startActivity(intent);
            }
        });

        int diaryCnt = mDocument.getDiaryManager().getDiaryCnt();
        if(diaryCnt == 0){
            cardView.setVisibility(View.INVISIBLE);
        }
        else {
            Diary diary = mDocument.getDiaryManager().getDiary(diaryCnt - 1);
            cardView.setVisibility(View.VISIBLE);
            tvDate.setText(diary.getDate());
            tvWeek.setText(diary.getWeek());
            tvContent.setText(diary.getContent());
        }

        BtCalendarView bc = (BtCalendarView) mLlyCalender.findViewById(R.id.calendar);
        bc.initializeAsGrid();
        bc.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(new Date(year - 1900, month, day));
                Diary diary = mDocument.getDiaryManager().getDiary(date);
                if (diary != null) {
                    cardView.setVisibility(View.VISIBLE);
                    tvDate.setText(diary.getDate());
                    tvWeek.setText(diary.getWeek());
                    tvContent.setText(diary.getContent());
                } else {
                    cardView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    protected void initViewPager(){
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        LayoutInflater inflater = getLayoutInflater();
        mLlyRecyclerView = inflater.inflate(R.layout.recyclerview,null);
        mLlyCalender = inflater.inflate(R.layout.calendar,null);

        mViewList = new ArrayList<View>();
        mViewList.add(mLlyRecyclerView);
        mViewList.add(mLlyCalender);
        mViewPager.setAdapter(new MyPagerAdapter( mViewList));
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mDocument.writeFile();
            mDocument.getDiaryManager().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case MyApplication.MOD_EDIT:
                if(resultCode == RESULT_OK) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(new Date());
                    int iWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    String returnedData = data.getStringExtra("data_return");
                    Diary diary = mDocument.getDiaryManager().createDiary(date, week[iWeek-1], returnedData, true);
                    mDocument.getDiaryManager().addDairy(diary);
                    mRVAdapter.setList(mDocument.getDiaryManager().getList());
                    mRVAdapter.notifyDataSetChanged();

                    //更新widget
                    Intent toWidget=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
                    sendBroadcast(toWidget);
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ComponentName componentName = getComponentName();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mRVAdapter.setList(mDocument.getDiaryManager().getList());
                mRVAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch(item.getItemId()) {
            case R.id.action_add:
                MyApplication.setCurMod(MyApplication.MOD_EDIT);
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                startActivityForResult(intent,MyApplication.MOD_EDIT);
                break;
            case R.id.action_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void succeed() {
        mRVAdapter.setList(MyApplication.getDoc().getDiaryManager().getList());
        mRVAdapter.notifyDataSetChanged();
		Toast.makeText(getApplicationContext(),"Load succeed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed() {

    }
}
