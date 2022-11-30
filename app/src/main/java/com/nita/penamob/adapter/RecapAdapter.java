package com.nita.penamob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.model.RecapModel;

import java.util.ArrayList;
import java.util.List;

public class RecapAdapter extends RecyclerView.Adapter<RecapAdapter.ViewHolder> {
    private final Context context;
    private List<RecapModel> listData = new ArrayList<>();
    private final OnClickListener onClickListener;
    private int layoutId;

    public RecapAdapter(Context context, List<RecapModel> listData, int layoutId, OnClickListener onClickListener) {
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
        RecapModel item = listData.get(position);
        holder.title.setText(item.getTitle());
        holder.score.setText(item.getScore());
        holder.passGrade.setText("> " + item.getPassGrade());

        if (item.getStatus().equals("lolos")) {
            holder.status.setText("Lulus");
            holder.contentStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_success));
        } else {
            holder.status.setText("Gagal");
            holder.contentStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_danger));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, score, passGrade, status;
        OnClickListener onClickListener;
        RelativeLayout contentStatus;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener, int layoutId) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            score = itemView.findViewById(R.id.score);
            passGrade = itemView.findViewById(R.id.passing_grade);
            status = itemView.findViewById(R.id.status);
            contentStatus = itemView.findViewById(R.id.content_status);

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