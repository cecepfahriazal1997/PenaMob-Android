package com.nita.penamob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.model.GridMenuModel;

import java.util.ArrayList;
import java.util.List;

public class GridMenuAdapter extends RecyclerView.Adapter<GridMenuAdapter.ViewHolder> {
    private final Context context;
    private List<GridMenuModel> listData = new ArrayList<>();
    private final OnClickListener onClickListener;
    private int layoutId;

    public GridMenuAdapter(Context context, List<GridMenuModel> listData, int layoutId, OnClickListener onClickListener) {
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
        GridMenuModel item = listData.get(position);
        holder.title.setText(item.getTitle());
        holder.total.setText(item.getTotal());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, total;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener, int layoutId) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            total = itemView.findViewById(R.id.total);

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