package com.nita.penamob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nita.penamob.R;
import com.nita.penamob.model.BannerModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends SliderViewAdapter<BannerAdapter.SliderAdapterVH> {
    private final Context context;
    private List<BannerModel> mBannerModels = new ArrayList<>();

    public BannerAdapter(Context context, List<BannerModel> list) {
        this.context = context;
        this.mBannerModels = list;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        BannerModel sliderItem = mBannerModels.get(position);
    }

    @Override
    public int getCount() {
        return mBannerModels.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        RoundedImageView image;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image           = itemView.findViewById(R.id.title);
            this.itemView   = itemView;
        }
    }
}