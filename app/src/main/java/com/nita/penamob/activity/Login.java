package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.json.JSONObject;

import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class Login extends BaseController implements View.OnClickListener {
    private FancyButton signin;
    private EditText username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findView();
    }

    private void findView() {
        signin      = findViewById(R.id.submit);
        username    = findViewById(R.id.username);
        password    = findViewById(R.id.password);

        signin.setOnClickListener(this::onClick);
    }

    private void signin() {
        try {
            service.bodyResponse = "user";
            param.clear();
            param.put("username", username.getText().toString());
            param.put("password", password.getText().toString());
            service.apiService(service.login, param, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        helper.showToast(hashMap.get("message"), 1);
                        if (hashMap.get("status").equals("true")) {
                            JSONObject data = new JSONObject(hashMap.get("data"));
                            helper.saveSession("key", data.getString("key"));
                            helper.saveSession("name", data.getString("name"));

                            helper.startIntent(Dashboard.class, false, null);
                            finish();
                        }
                    } catch (Exception er) {
                        er.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                signin();
                break;
        }
    }
}