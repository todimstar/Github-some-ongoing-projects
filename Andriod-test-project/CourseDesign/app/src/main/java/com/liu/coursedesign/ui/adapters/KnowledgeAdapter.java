package com.liu.coursedesign.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.liu.coursedesign.R;
import com.liu.coursedesign.model.Knowledge;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * 知识卡片适配器
 * 对应布局文件: item_knowledge_card_modern.xml
 */
public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.KnowledgeViewHolder> {

    private List<Knowledge> knowledgeList;

    public KnowledgeAdapter(List<Knowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }

    @NonNull
    @Override
    public KnowledgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_knowledge_card_modern, parent, false);
        return new KnowledgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeViewHolder holder, int position) {
        Knowledge knowledge = knowledgeList.get(position);
        holder.bind(knowledge);
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }

    /**
     * 更新数据
     */
    public void updateData(List<Knowledge> newKnowledgeList) {
        this.knowledgeList = newKnowledgeList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder类
     */
    public static class KnowledgeViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivKnowledgeImage;
        private TextView tvTitle, tvDescription, tvUpdateTime;
        private Chip chipCategory;
        private MaterialButton btnFavorite, btnShare;

        public KnowledgeViewHolder(@NonNull View itemView) {
            super(itemView);

            // 绑定视图组件
            ivKnowledgeImage = itemView.findViewById(R.id.ivKnowledgeImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUpdateTime = itemView.findViewById(R.id.tvUpdateTime);
            chipCategory = itemView.findViewById(R.id.chipCategory);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnShare = itemView.findViewById(R.id.btnShare);
        }

        /**
         * 绑定数据到视图
         */
        public void bind(Knowledge knowledge) {
            // 设置文本内容
            tvTitle.setText(knowledge.getTitle());
            tvDescription.setText(knowledge.getDescription());
            tvUpdateTime.setText(knowledge.getUpdateTime());
            chipCategory.setText(knowledge.getCategory());

            // 加载图片（这里使用占位符，实际项目中使用图片加载库如Glide）
            if (knowledge.getImagePath() != null && !knowledge.getImagePath().isEmpty()) {
                // 使用Glide或Picasso加载图片
                // Glide.with(itemView.getContext()).load(knowledge.getImagePath()).into(ivKnowledgeImage);
                ivKnowledgeImage.setImageResource(R.drawable.image_placeholder_modern);
            } else {
                ivKnowledgeImage.setImageResource(R.drawable.image_placeholder_modern);
            }

            // 设置点击事件
            btnFavorite.setOnClickListener(v -> {
                // 收藏功能
                android.widget.Toast.makeText(itemView.getContext(), "已收藏", android.widget.Toast.LENGTH_SHORT).show();
            });

            btnShare.setOnClickListener(v -> {
                // 分享功能
                shareKnowledge(knowledge);
            });
        }

        private void shareKnowledge(Knowledge knowledge) {
            android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    "分享一个编程知识：" + knowledge.getTitle() + "\n" + knowledge.getDescription());
            itemView.getContext().startActivity(android.content.Intent.createChooser(shareIntent, "分享知识"));
        }
    }
}

