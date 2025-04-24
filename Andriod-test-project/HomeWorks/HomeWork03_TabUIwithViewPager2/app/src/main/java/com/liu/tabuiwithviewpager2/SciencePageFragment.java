package com.liu.tabuiwithviewpager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SciencePageFragment extends Fragment {

    private static final String ARG_IMAGE_RES_ID = "image_res_id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";

    public static SciencePageFragment newInstance(int imageResId, String title, String description) {
        SciencePageFragment fragment = new SciencePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_science_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.science_image);
        TextView titleView = view.findViewById(R.id.science_title);
        TextView descriptionView = view.findViewById(R.id.science_description);

        if (getArguments() != null) {
            imageView.setImageResource(getArguments().getInt(ARG_IMAGE_RES_ID));
            titleView.setText(getArguments().getString(ARG_TITLE));
            descriptionView.setText(getArguments().getString(ARG_DESCRIPTION));
        }
    }
}