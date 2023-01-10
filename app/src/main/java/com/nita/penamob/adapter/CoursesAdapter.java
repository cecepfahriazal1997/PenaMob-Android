package com.nita.penamob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nita.penamob.R;
import com.nita.penamob.model.CoursesModel;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private final Context context;
    private List<CoursesModel> listData = new ArrayList<>();
    private final OnClickListener onClickListener;
    private int layoutId;

    public CoursesAdapter(Context context, List<CoursesModel> listData, int layoutId, OnClickListener onClickListener) {
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
        CoursesModel item = listData.get(position);
        holder.category.setText(item.getCategory());
        holder.title.setText(item.getTitle());
        holder.participant.setText(item.getParticipant() + " Peserta");
        holder.teacher.setText(item.getTeacher());

        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Glide.with(context)
                    .load(item.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.cover);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView category, title, participant, teacher;
        RoundedImageView cover;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener, int layoutId) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            participant = itemView.findViewById(R.id.participant);
            teacher = itemView.findViewById(R.id.teacher);
            cover = itemView.findViewById(R.id.image);

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