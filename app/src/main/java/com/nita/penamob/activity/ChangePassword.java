package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class ChangePassword extends BaseController implements View.OnClickListener {
    private EditText password, passwordConfirm;
    private FancyButton submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        findView();
        init();
    }

    private void findView() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.password_confirm);
        submit = findViewById(R.id.submit);
    }

    private void init() {
        title.setText("Ubah Password");
        back.setOnClickListener(this::onClick);
        submit.setOnClickListener(this::onClick);
    }

    private void saveData() {
        try {
            Map<String, String> param = new HashMap<>();
            param.put("password", password.getText().toString());
            param.put("retype_password", passwordConfirm.getText().toString());

            service.apiService(service.changePassword, param, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    helper.showToast(hashMap.get("message"), 0);
                    if (hashMap.get("status").equals("true")) {
                        finish();
                    }
                    return null;
                }
            });
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if (passwordConfirm.getText().toString().equals(password.getText().toString()))
                    saveData();
                else helper.showToast("Konfirmasi password tidak sesuai!", 0);
                break;
        }
    }
}