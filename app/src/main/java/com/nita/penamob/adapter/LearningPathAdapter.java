package com.nita.penamob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.LearningDetail;
import com.nita.penamob.helper.GeneralHelper;
import com.nita.penamob.model.LearningPathModel;
import com.nita.penamob.model.LessonsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningPathAdapter extends RecyclerView.Adapter<LearningPathAdapter.ViewHolder> {
    private final Context context;
    private List<LearningPathModel> listData = new ArrayList<>();
    private final OnClickListener onClickListener;
    private int layoutId;
    private GeneralHelper helper;

    public LearningPathAdapter(Context context, List<LearningPathModel> listData, int layoutId, GeneralHelper helper, OnClickListener onClickListener) {
        this.context = context;
        this.listData = listData;
        this.layoutId = layoutId;
        this.onClickListener = onClickListener;
        this.helper = helper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutId, parent, false);
        return new ViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LearningPathModel item = listData.get(position);

        List<LessonsModel> lesson = item.getLessons();
        int totalLesson = lesson.size();

        holder.topic.setText(item.getTopic());

        if (totalLesson > 0) {
            holder.total.setText(totalLesson + " pembelajaran");
        } else {
            holder.total.setText("Tidak ada pembelajaran");
        }

        if (item.isLocked()) {
            holder.arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.lock));
            holder.arrow.setImageTintList(ContextCompat.getColorStateList(context, R.color.grayLevel4));
            holder.topic.setTextColor(ContextCompat.getColor(context, R.color.grayLevel4));
            holder.total.setTextColor(ContextCompat.getColor(context, R.color.grayLevel4));
        }

        // set list lessons
        if (totalLesson > 0) {
            if (item.isOpened()) {
                holder.contentLessons.setVisibility(View.VISIBLE);
                holder.arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            } else {
                holder.contentLessons.setVisibility(View.GONE);
                holder.arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.arrow_down));
            }

            holder.contentLessons.removeAllViews();
            for (LessonsModel row: lesson) {
                View viewLessons = this.helper.inflateView(R.layout.item_theory);

                LinearLayout content = viewLessons.findViewById(R.id.content);
                TextView title = viewLessons.findViewById(R.id.lesson);
                TextView type = viewLessons.findViewById(R.id.type);
                TextView format = viewLessons.findViewById(R.id.format);
                TextView expired = viewLessons.findViewById(R.id.expired);

                title.setText(row.getLessons());
                type.setText(row.getType());
                format.setText(row.getFormat());
                expired.setText(row.getExpiredDate());

                if (row.getExpiredDate().isEmpty()) {
                    expired.setVisibility(View.GONE);
                }

                Map<String, String> params = new HashMap<>();
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params.clear();
                        switch (row.getType().toLowerCase()) {
                            case "materi":
                                params.put("type", "materi");
                                helper.startIntent(LearningDetail.class, false, params);
                                break;
                            case "tugas":
                                params.put("type", "tugas");
                                helper.startIntent(LearningDetail.class, false, params);
                                break;
                            case "kuis":
                                title.getText().toString().substring(100);
                                break;
                        }
                    }
                });

                holder.contentLessons.addView(viewLessons);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public LessonsModel getDetailLesson(int parentPosition, int position) {
        return this.listData.get(parentPosition).getLessons().get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView topic, total;
        OnClickListener onClickListener;
        ImageView arrow;
        LinearLayout contentLessons;
        RelativeLayout contentTopic;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic);
            total = itemView.findViewById(R.id.total_lesson);
            contentLessons = itemView.findViewById(R.id.content_lesson);
            arrow = itemView.findViewById(R.id.arrow);
            contentTopic = itemView.findViewById(R.id.contentTopic);

            this.onClickListener = onClickListener;

            contentTopic.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition(), v);
        }
    }

    public interface OnClickListener {
        void onClickListener(int position, View view);
    }
}