package com.danmo.ithouse.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.danmo.ithouse.R;
import com.danmo.ithouse.base.BaseActivity;
import com.danmo.ithouse.base.BaseViewPagerAdapter;
import com.danmo.ithouse.fragment.sub.calendar.NewsEventFragment;
import com.danmo.ithouse.fragment.sub.calendar.NewsPushFragment;
import com.danmo.ithouse.fragment.sub.calendar.ReadHistoryFragment;
import com.danmo.ithouse.fragment.sub.calendar.TodayNewsFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/12/22.
 */

public class CalendarActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener {

    private Toolbar mToolbar;
    private CalendarView mCalendarView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToolbar = mRootView.findViewById(R.id.canlendar_toolbar);
        setSupportActionBar(mToolbar);
        mCalendarView = findViewById(R.id.calendarView);
        mTabLayout = mRootView.findViewById(R.id.calendar_tab_nav);
        mViewPager = mRootView.findViewById(R.id.calendar_viewpager);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity.this.onBackPressed();
            }
        });
        mToolbar.setTitle(mCalendarView.getCurYear() + "骞? + mCalendarView.getCurMonth() + "鏈? + mCalendarView.getCurDay() + "鏃?);

        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);

//        List<Calendar> schemes = new ArrayList<>();
//        int year = mCalendarView.getCurYear();
//        int month = mCalendarView.getCurMonth();
//        schemes.add(getSchemeCalendar(year, month, 3, 0, "鍋?));
//        schemes.add(getSchemeCalendar(year, month, 6, 0, "浜?));
//        schemes.add(getSchemeCalendar(year, month, 9, 0, "璁?));
//        schemes.add(getSchemeCalendar(year, month, 13, 0, "璁?));
//        schemes.add(getSchemeCalendar(year, month, 14, 0, "璁?));
//        schemes.add(getSchemeCalendar(year, month, 15, 0, "鍋?));
//        schemes.add(getSchemeCalendar(year, month, 18, 0, "璁?));
//        schemes.add(getSchemeCalendar(year, month, 25, 0, "鍋?));
//        mCalendarView.setSchemeDate(schemes);

        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(this, getSupportFragmentManager(), getPagers());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0, true);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//濡傛灉鍗曠嫭鏍囪棰滆壊銆佸垯浼氫娇鐢ㄨ繖涓鑹?        calendar.setScheme(text);
        return calendar;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CalendarActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onDateSelected(Calendar calendar) {

    }

    @Override
    public void onYearChange(int year) {

    }

    private List<BaseViewPagerAdapter.PagerInfo> getPagers() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("sub_tab", "tab_calendar");

        List<BaseViewPagerAdapter.PagerInfo> list = new ArrayList<>();
        list.add(new BaseViewPagerAdapter.PagerInfo("瑕侀椈鎺ㄩ€?, NewsPushFragment.class,
                bundle));
        list.add(new BaseViewPagerAdapter.PagerInfo("褰撴棩鏂伴椈", TodayNewsFragment.class,
                bundle));
        list.add(new BaseViewPagerAdapter.PagerInfo("浜嬩欢", NewsEventFragment.class,
                bundle));
        list.add(new BaseViewPagerAdapter.PagerInfo("娴忚鍘嗗彶", ReadHistoryFragment.class,
                bundle));
        return list;

    }
}

