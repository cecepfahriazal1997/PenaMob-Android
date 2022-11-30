package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nita.penamob.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class Walkthrough extends BaseController implements View.OnClickListener {
    private ViewPager mViewPager;
    private DotsIndicator pageIndicatorView;
    private FancyButton back, next;
    private final String[] title = {"Peringatan Buka", "Peneliti", "Disclaimer"};
    private final String[] desc = {
            "Harus dimengerti aplikasi ini berisi materi profesional Hanya digunakan oleh petugas terlatih yang telah dilatih.",
            "Dr. H. Boy Subirosa Sabarguna, MARS\n" + "Yudi Rusmawan, S.Pd.I.",
            "Aplikasi ini berisi materi profesional, Hanya digunakan oleh petugas terlatih yang telah dilatih, Kami tidak bertanggung jawab atas pemakaian yang tidak sesuai."
    };

    private final int[] image = {R.drawable.logo, R.drawable.logo, R.drawable.logo};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough);

        mViewPager          = findViewById(R.id.viewPager);
        next                = findViewById(R.id.next);
        back                = findViewById(R.id.back);
        pageIndicatorView   = findViewById(R.id.indicator);

        setupViewPager(mViewPager);
        pageIndicatorView.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mViewPager.getCurrentItem() > 0) {
                    back.setVisibility(View.VISIBLE);

                    if (mViewPager.getCurrentItem() == title.length - 1) {
                        next.setText("Masuk Aplikasi");
                        next.setOnClickListener(Walkthrough.this::onClick);
                    } else {
                        next.setText("Selanjutnya");
                        next.setOnClickListener(Walkthrough.this::onClick);
                    }
                } else {
                    back.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        WalkthroughFragmentAdapter adapter = new WalkthroughFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i < title.length; i++) {
            adapter.addFrag(com.nita.penamob.fragment.Walkthrough.newInstance(
                    title[i],
                    desc[i],
                    image[i],
                    i));
        }

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (mViewPager.getCurrentItem() - 1 >= 0)
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1,true);
                break;
            case R.id.next:
                if (next.getText().toString().equalsIgnoreCase("selanjutnya")) {
                    if (mViewPager.getCurrentItem() + 1 <= mViewPager.getChildCount())
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                } else {
                    helper.startIntent(Login.class, false, null);
                }
                break;
        }
    }

    class WalkthroughFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public WalkthroughFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}