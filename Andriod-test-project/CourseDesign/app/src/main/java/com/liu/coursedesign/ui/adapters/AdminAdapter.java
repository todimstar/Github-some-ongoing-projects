package com.liu.coursedesign.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liu.coursedesign.R;
import com.liu.coursedesign.model.Knowledge;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 管理员知识列表适配器
 * 对应布局文件: item_admin_knowledge.xml
 */
public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private List<Knowledge> knowledgeList;
    private OnItemActionListener listener;


    public interface OnItemActionListener {
        void onEditClick(Knowledge knowledge);
        void onDeleteClick(Knowledge knowledge);
    }

    public AdminAdapter(List<Knowledge> knowledgeList, OnItemActionListener listener) {
        this.knowledgeList = knowledgeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_knowledge, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        Knowledge knowledge = knowledgeList.get(position);
        holder.bind(knowledge, listener);
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }

    public void updateData(List<Knowledge> newKnowledgeList) {
        this.knowledgeList = newKnowledgeList;
        notifyDataSetChanged();
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivThumbnail;
        private TextView tvTitle, tvDescription, tvDate;
        private ImageButton btnEdit, btnDelete;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Knowledge knowledge, OnItemActionListener listener) {
            tvTitle.setText(knowledge.getTitle());
            tvDescription.setText(knowledge.getDescription());
            tvDate.setText(knowledge.getUpdateTime());

            // 设置缩略图
            ivThumbnail.setImageResource(R.drawable.image_placeholder);

            // 设置点击事件
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(knowledge);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(knowledge);
                }
            });
        }
    }
}

