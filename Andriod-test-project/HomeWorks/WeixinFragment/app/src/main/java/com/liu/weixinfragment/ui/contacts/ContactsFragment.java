package com.liu.weixinfragment.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.liu.weixinfragment.R;

public class ContactsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        TextView textView = view.findViewById(R.id.text_contacts);
        textView.setText(R.string.hello_contacts_fragment); // 使用 strings.xml 中的字符串
        return view;
    }
}