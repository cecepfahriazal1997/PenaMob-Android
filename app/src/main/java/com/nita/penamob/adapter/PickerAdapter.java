package com.nita.penamob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.model.PickerModel;

import java.util.ArrayList;
import java.util.List;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ViewHolder> {
    private final Context context;
    private List<PickerModel> listData = new ArrayList<>();
    private final OnClickListener onClickListener;
    private int layoutId;

    public PickerAdapter(Context context, List<PickerModel> listData, int layoutId, OnClickListener onClickListener) {
        this.context = context;
        this.listData = listData;
        this.layoutId = layoutId;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutId, parent, false);
        return new ViewHolder(view, onClickListener, layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickerModel item = listData.get(position);
        holder.title.setText(item.getTitle());

        if (item.isSelected()) {
            holder.content.setBackgroundColor(ContextCompat.getColor(context, R.color.primaryDark));
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        RelativeLayout content;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener, int layoutId) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);

            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onClickListener(int position);
    }
}