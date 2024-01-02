package com.nita.penamob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.json.JSONObject;

import java.util.Map;

public class Profile extends BaseController implements View.OnClickListener {
    private EditText identityNumber, name, placeBirth, dateBirth, email, phone;
    private RoundedImageView image;
    private String imageUrl="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        initView();
        init();
        fetch();
    }

    private void initView() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);

        this.image = findViewById(R.id.image);
        this.identityNumber = findViewById(R.id.identity_number);
        this.name = findViewById(R.id.name);
        this.placeBirth = findViewById(R.id.placebirth);
        this.dateBirth = findViewById(R.id.datebirth);
        this.email = findViewById(R.id.email);
        this.phone = findViewById(R.id.phone);
    }

    private void init() {
        title.setText("Profile");
        back.setOnClickListener(this::onClick);

        identityNumber.setEnabled(false);
        name.setEnabled(false);
        placeBirth.setEnabled(false);
        dateBirth.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
    }

    private void fetch() {
        try {
            service.apiService(service.profile, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject data = new JSONObject(hashMap.get("data"));
                            identityNumber.setText(data.getString("identity_number"));
                            name.setText(data.getString("name"));
                            placeBirth.setText(data.getString("place_birth"));
                            dateBirth.setText(helper.convertDateString(data.getString("date_birth"), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                            email.setText(data.getString("email"));
                            phone.setText(data.getString("phone"));

                            if (!data.getString("picture").isEmpty()) {
                                Glide.with(getBaseContext())
                                        .load(data.getString("picture"))
                                        .placeholder(R.drawable.placeholder)
                                        .into(image);
                                image.setOnClickListener(Profile.this::onClick);
                                imageUrl = data.getString("picture");
                            }
                        }
                    } catch (Exception e) {
                    }
                    return null;
                }
            });
        } catch (Exception err) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.image:
                startActivity(new Intent(getBaseContext(), PreviewLink.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("title", "Foto Profile")
                        .putExtra("url", imageUrl));
                break;
        }
    }
}