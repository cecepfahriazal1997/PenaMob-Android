package com.nita.penamob.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.activity.Login;
import com.nita.penamob.activity.Profile;
import com.nita.penamob.adapter.ProfileMenuAdapter;
import com.nita.penamob.model.ProfileMenuModel;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class Account extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private ProfileMenuAdapter adapter;
    private List<ProfileMenuModel> lists = new ArrayList<>();
    private FancyButton logout;

    public Account() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = ((Dashboard) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        findView(rootView);
        this.setMenu();

        return rootView;
    }

    private void findView(View rootView) {
        listMenu = rootView.findViewById(R.id.list_menu);
        logout = rootView.findViewById(R.id.logout);

        logout.setOnClickListener(this::onClick);
    }

    private void setMenu() {
        String title[] = {
                "Profile",
                "Tentang Aplikasi",
                "Ubah Password",
                "Term & Condition",
        };
        int icon[] = {
                R.drawable.person_fill,
                R.drawable.info,
                R.drawable.lock,
                R.drawable.security,
        };

        lists.clear();

        for (int i = 0; i < title.length; i++) {
            ProfileMenuModel item = new ProfileMenuModel(icon[i], title[i]);
            lists.add(item);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter = new ProfileMenuAdapter(parent.getApplicationContext(), lists, R.layout.item_account, new ProfileMenuAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                switch (position) {
                    case 0:
                        parent.functionHelper.startIntent(Profile.class, false, null);
                        break;
                }
            }
        });

        listMenu.setLayoutManager(layoutManager);
        listMenu.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                parent.functionHelper.clearSession();
                parent.functionHelper.startIntent(Login.class, false, null);
                break;
        }
    }
}