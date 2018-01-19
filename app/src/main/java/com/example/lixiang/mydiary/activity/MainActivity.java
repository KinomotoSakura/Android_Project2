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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.bonet.views.BtCalendarView;
import com.bonet.views.OnDateSelectedListener;
import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.adapter.MyPagerAdapter;
import com.example.lixiang.mydiary.adapter.RecyclerViewAdapter;
import com.example.lixiang.mydiary.listener.loadDataListener;
import com.example.lixiang.mydiary.model.Document;
import com.example.lixiang.mydiary.model.Diary;
import com.example.lixiang.mydiary.MyApplication;
import com.example.lixiang.mydiary.util.ThemeUtils;
import com.example.lixiang.mydiary.view.PopupDialogFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private ImageButton switch_btn;
    public static String TOP_STATES = "TOP";
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
        Intent toWidget=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(toWidget);
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
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
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
        mRVAdapter.setItemListener(new RecyclerViewAdapter.ItemOnLongClickListener() {
            @Override
            public void itemLongClick(final Diary diary) {
                Bundle bundle = new Bundle();
                bundle.putInt(TOP_STATES, diary.getTop());
                PopupDialogFragment popupDialog = new PopupDialogFragment();
                popupDialog.setArguments(bundle);
                popupDialog.setItemOnClickListener(new PopupDialogFragment.DialogItemOnClickListener() {
                    @Override
                    public void onTop() { //置顶
                        diary.setTop(1);
                        diary.setTime(System.currentTimeMillis());
                        refreshView();
                    }
                    @Override
                    public void onCancel() { //取消
                        diary.setTop(0);
                        diary.setTime(System.currentTimeMillis());
                        refreshView();
                    }
                });
                popupDialog.show(getFragmentManager(), "popup");
            }
        });
        recyclerView.setAdapter(mRVAdapter);
    }
    private void refreshView() {
        //如果不调用sort方法，是不会进行排序的，也就不会调用compareTo
        Collections.sort(mDocument.getDiaryManager().getList());
        mRVAdapter.setList(MyApplication.getDoc().getDiaryManager().getList());
        mRVAdapter.notifyDataSetChanged();
    }

    protected void initCalenderView(){
        final CardView cardView = (CardView)mLlyCalender.findViewById(R.id.lly_item);
        final TextView tvDate = (TextView) mLlyCalender.findViewById(R.id.tv_date);
        final TextView tvWeek = (TextView) mLlyCalender.findViewById(R.id.tv_week);
        final TextView tvContent = (TextView) mLlyCalender.findViewById(R.id.tv_content);
        final TextView tvToday = (TextView) mLlyCalender.findViewById(R.id.tv_today);
        final ImageView pen = (ImageView) mLlyCalender.findViewById(R.id.pen);
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.setCurMod(MyApplication.MOD_EDIT);
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                startActivityForResult(intent,MyApplication.MOD_EDIT);
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        tvToday.setText(formatter.format(new Date()));
        tvContent.setText("选个日期吧 <(￣︶￣)>");

        BtCalendarView bc = (BtCalendarView) mLlyCalender.findViewById(R.id.calendar);
        bc.initializeAsGrid();
        bc.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date(year - 1900, month, day);
                String date = formatter.format(d);
                tvDate.setText(date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
                if (w < 0) w = 0;
                tvWeek.setText(week[w]);

                Diary diary = mDocument.getDiaryManager().getDiary(date);
                if (diary != null) {
                    tvContent.setText("这天，你的置顶日记为--"+diary.getContent());
                } else {
                    tvContent.setText("这天很懒，没有留下日记╮(╯▽╰)╭");
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

        switch_btn = (ImageButton) findViewById(R.id.switch_button);
        switch_btn.setTag("0");
        switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch_btn.getTag() == "0") {
                    switch_btn.setImageResource(R.drawable.book_easyicon);
                    mViewPager.arrowScroll(View.FOCUS_RIGHT);
                    switch_btn.setTag("1");
                } else {
                    switch_btn.setImageResource(R.drawable.calendar_easyicon);
                    mViewPager.arrowScroll(View.FOCUS_LEFT);
                    switch_btn.setTag("0");
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) { }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                //arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕，arg0==0的时表示什么都没做。
                if(arg0 == 2){
                    if (mViewPager.getCurrentItem()==0) {
                        switch_btn.setTag("0");
                        switch_btn.setImageResource(R.drawable.calendar_easyicon);
                    }else{
                        switch_btn.setTag("1");
                        switch_btn.setImageResource(R.drawable.book_easyicon);
                    }
                }
            }
        });
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

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
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
		//Toast.makeText(getApplicationContext(),"Load succeed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed() {

    }
}
