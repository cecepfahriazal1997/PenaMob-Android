package com.nita.penamob.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.model.QuizModel;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private final Context context;
    private List<QuizModel> listData = new ArrayList<>();
    private final OnClickListener onClickListener;
    private int layoutId;

    public QuizAdapter(Context context, List<QuizModel> listData, int layoutId, OnClickListener onClickListener) {
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
        Drawable badgeWhite = ContextCompat.getDrawable(context, R.drawable.badge_white);
        Drawable badgePrimary = ContextCompat.getDrawable(context, R.drawable.badge_primary);
        int black = ContextCompat.getColor(context, R.color.black);
        int white = ContextCompat.getColor(context, R.color.white);

        QuizModel item = listData.get(position);

        for (int i = 0; i < item.countOption() - 1; i++) {
            holder.option1.setTextColor(black);
            holder.option2.setTextColor(black);
            holder.option3.setTextColor(black);
            holder.option4.setTextColor(black);
            holder.option5.setTextColor(black);

            holder.option1.setBackground(badgeWhite);
            holder.option2.setBackground(badgeWhite);
            holder.option3.setBackground(badgeWhite);
            holder.option4.setBackground(badgeWhite);
            holder.option5.setBackground(badgeWhite);
        }

        if (item.getOption(0).equals("1")) {
            holder.option1.setBackground(badgePrimary);
            holder.option1.setTextColor(white);
        } else if (item.getOption(1).equals("1")) {
            holder.option2.setBackground(badgePrimary);
            holder.option2.setTextColor(white);
        } else if (item.getOption(2).equals("1")) {
            holder.option3.setBackground(badgePrimary);
            holder.option3.setTextColor(white);
        } else if (item.getOption(3).equals("1")) {
            holder.option4.setBackground(badgePrimary);
            holder.option4.setTextColor(white);
        } else if (item.getOption(4).equals("1")) {
            holder.option5.setBackground(badgePrimary);
            holder.option5.setTextColor(white);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView percentage, question, option1, option2, option3, option4, option5;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener, int layoutId) {
            super(itemView);
            percentage = itemView.findViewById(R.id.percentage);
            question = itemView.findViewById(R.id.question);
            option1 = itemView.findViewById(R.id.option_1);
            option2 = itemView.findViewById(R.id.option_2);
            option3 = itemView.findViewById(R.id.option_3);
            option4 = itemView.findViewById(R.id.option_4);
            option5 = itemView.findViewById(R.id.option_5);

            this.onClickListener = onClickListener;

            option1.setOnClickListener(this);
            option2.setOnClickListener(this);
            option3.setOnClickListener(this);
            option4.setOnClickListener(this);
            option5.setOnClickListener(this);
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