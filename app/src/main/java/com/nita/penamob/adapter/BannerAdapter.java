package com.nita.penamob.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.nita.penamob.R;
import com.nita.penamob.activity.PreviewLink;
import com.nita.penamob.model.BannerModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends SliderViewAdapter<BannerAdapter.SliderAdapterVH> {

    private Context context;
    private List<BannerModel> mBannerModels = new ArrayList<>();

    public BannerAdapter(Context context, List<BannerModel> BannerModels) {
        this.mBannerModels = BannerModels;
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        BannerModel data = mBannerModels.get(position);

        Glide.with(context)
                .load(data.getImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PreviewLink.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("title", data.getTitle())
                        .putExtra("url", data.getImage()));
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mBannerModels.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        AppCompatImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image);
            this.itemView = itemView;
        }
    }

}
