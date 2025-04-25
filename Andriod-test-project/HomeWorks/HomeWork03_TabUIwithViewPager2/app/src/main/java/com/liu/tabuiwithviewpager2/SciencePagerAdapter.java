package com.liu.tabuiwithviewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class SciencePagerAdapter extends FragmentStateAdapter {

    private final List<ScienceContent> contentList;

    public SciencePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<ScienceContent> contentList) {
        super(fragmentActivity);
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ScienceContent content = contentList.get(position);
        return SciencePageFragment.newInstance(content.getImageResId(), content.getTitle(), content.getDescriptionResId());
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    // 用于 TabLayout 获取标题
    public String getPageTitle(int position) {
        return contentList.get(position).getTitle();
    }
}