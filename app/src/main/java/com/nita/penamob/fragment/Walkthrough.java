package com.nita.penamob.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import com.nita.penamob.R;

public class Walkthrough extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_DRAWABLE = "drawable";

    private String TITLE;
    private String DESC;
    private int DRAWABLE;

    private TextView titleWalkthrough, description;
    private ImageView imageWalkthrough;

    public Walkthrough() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            TITLE               = getArguments().getString(ARG_TITLE);
            DESC                = getArguments().getString(ARG_DESC);
            DRAWABLE            = getArguments().getInt(ARG_DRAWABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_walkthrough, container, false);

        titleWalkthrough            = v.findViewById(R.id.title);
        description                 = v.findViewById(R.id.description);
        imageWalkthrough            = v.findViewById(R.id.image);

        titleWalkthrough.setText(TITLE);
        description.setText(DESC);
        imageWalkthrough.setImageResource(DRAWABLE);
        return v;
    }

    public static Walkthrough newInstance(CharSequence title, CharSequence description,
                                                  @DrawableRes int imageDrawable,
                                                  int position) {
        Walkthrough slide = new Walkthrough();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title.toString());
        args.putString(ARG_DESC, description.toString());
        args.putInt(ARG_DRAWABLE, imageDrawable);
        slide.setArguments(args);

        return slide;
    }
}