package com.nita.penamob.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;
import com.nita.penamob.fragment.Account;
import com.nita.penamob.fragment.Home;
import com.nita.penamob.fragment.Report;
import com.nita.penamob.fragment.Courses;
import com.nita.penamob.helper.GeneralHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends BaseController implements View.OnClickListener {
    private TabLayout tabLayout;
    public ViewPager viewPager;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler handler;
    public int tabIconColor;
    public int tabIconColor2;
    public int position = 0;
    private final int[] tabIcons = {
            R.drawable.dashboard,
            R.drawable.courses,
            R.drawable.printer,
            R.drawable.account
    };

    private final String[] title = {
            "Dashboard",
            "Kelas",
            "Laporan",
            "Akun"
    };

    public Service clientApiService;
    public GeneralHelper functionHelper;
    private ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        this.clientApiService = service;
        this.functionHelper = helper;
        tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.primary);
        tabIconColor2 = ContextCompat.getColor(getApplicationContext(), R.color.grayLevel5);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        changeColorIcon();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), title[0]);
        adapter.addFragment(new Courses(), title[1]);
        adapter.addFragment(new Report(), title[2]);
        adapter.addFragment(new Account(), title[3]);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    public void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }

    @Override
    public void onClick(View v) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabIcons.length; i++) {
            if (tabLayout.getTabAt(0).isSelected()) {
                setTabIcon(0, tabIcons[0], tabIconColor);
            }
            setTabIcon(i, tabIcons[i], tabIconColor2);
        }
    }

    private void changeColorIcon() {
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        position = tab.getPosition();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        tab.getIcon().setColorFilter(tabIconColor2, PorterDuff.Mode.SRC_IN);
                    }
                }
        );
    }

    public void setTabIcon(int pos, int icon, int color) {
        tabLayout.getTabAt(pos).setIcon(icon);
        if (color != 0)
            tabLayout.getTabAt(pos).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        helper.showToast("Please click button back again to exit the Application !", 0);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                handler.removeCallbacksAndMessages(null);
            }
        }, 2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}