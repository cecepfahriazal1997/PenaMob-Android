package com.nita.penamob.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.adapter.BannerAdapter;
import com.nita.penamob.adapter.GridMenuAdapter;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.BannerModel;
import com.nita.penamob.model.GridMenuModel;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Home extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private GridMenuAdapter adapter;
    private TextView name;
    private List<GridMenuModel> lists = new ArrayList<>();

    private SliderView sliderView;

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = ((Dashboard) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        findView(rootView);
        this.fetchData();
        this.init();

        return rootView;
    }

    private void findView(View rootView) {
        sliderView = rootView.findViewById(R.id.imageSlider);
        listMenu = rootView.findViewById(R.id.list_menu);
        name = rootView.findViewById(R.id.name);
    }

    private void init() {
        name.setText(parent.functionHelper.getSession("name"));
        initBanner();
    }

    private void fetchData() {
        this.setData(new String[]{"0", "0", "0", "0"});
        parent.clientApiService.apiService(parent.clientApiService.dashboard, null, null, true, new Service.hashMapListener() {
            @Override
            public String getHashMap(Map<String, String> hashMap) {
                try {
                    JSONObject data = new JSONObject(hashMap.get("data"));
                    setData(new String[]{data.getString("quiz"), data.getString("assignment"), data.getString("lesson"), data.getString("courses")});
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private void initBanner() {
        try {
            parent.clientApiService.apiService(parent.clientApiService.listBanner, null, null, false, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        List<BannerModel> listBanner = new ArrayList<>();

                        if (hashMap.get("status").equals("true")) {
                            JSONArray data = new JSONArray(hashMap.get("data"));

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject detail = data.getJSONObject(i);
                                BannerModel dataBanner = new BannerModel();

                                dataBanner.setTitle(detail.getString("title"));
                                dataBanner.setImage(detail.getString("picture"));

                                listBanner.add(dataBanner);
                            }
                        }
                        BannerAdapter bannerAdapter = new BannerAdapter(parent.getApplicationContext(), listBanner);
                        sliderView.setSliderAdapter(bannerAdapter);
                        bannerAdapter.notifyDataSetChanged();
                        sliderView.startAutoCycle();
                    } catch (Exception error) {
                        Log.e("banner", error.getLocalizedMessage());
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            Log.e("banner", e.getLocalizedMessage());
        }
    }

    private void setData(String[] value) {
        String title[] = {
                "Total Kuis",
                "Total Tugas",
                "Total Materi",
                "Total Kelas"
        };

        lists.clear();

        for (int i = 0; i < title.length; i++) {
            GridMenuModel item = new GridMenuModel(title[i], value[i]);
            lists.add(item);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(parent.getApplicationContext(), 2);
        adapter = new GridMenuAdapter(parent.getApplicationContext(), lists, R.layout.grid_menu, new GridMenuAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                parent.viewPager.setCurrentItem(1);
            }
        });

        listMenu.setLayoutManager(layoutManager);
        listMenu.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_) {
                this.fetchData();
            }
        }
    }
}