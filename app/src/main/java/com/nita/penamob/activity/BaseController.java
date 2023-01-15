package com.nita.penamob.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nita.penamob.api.Service;
import com.nita.penamob.helper.GeneralHelper;

import java.util.HashMap;
import java.util.Map;

public class BaseController extends AppCompatActivity {
    protected GeneralHelper helper;
    protected Service service;
    protected ProgressDialog pDialog;
    protected Map<String, String> param = new HashMap<>();
    protected ImageButton back;
    protected TextView title;
    String[] permissionImage = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pDialog = new ProgressDialog(this);
        helper = new GeneralHelper(this);
        service = new Service(this, pDialog);
        helper.setupProgressDialog(pDialog, "Loading data ...");
    }

    protected void checkIsLogin() {
        if (helper.getSession("key") != null && !helper.getSession("key").isEmpty()) {
            helper.startIntent(Dashboard.class, false, null);
        } else {
            helper.startIntent(Login.class, true, null);
        }
    }
}