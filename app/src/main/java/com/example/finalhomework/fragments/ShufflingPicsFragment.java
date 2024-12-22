package com.example.finalhomework.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.finalhomework.R;

public class ShufflingPicsFragment extends Fragment {
    private int ImgId;  // 图片资源ID
    private ImageView imageView;

    // 无参构造函数
    public ShufflingPicsFragment() {
        // 必须提供一个无参构造函数
    }

    // 带参构造函数 - 设置图片资源ID
    public static ShufflingPicsFragment newInstance(int imgId) {
        ShufflingPicsFragment fragment = new ShufflingPicsFragment();
        Bundle args = new Bundle();
        args.putInt("ImgId", imgId);  // 通过Bundle传递数据
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 获取传递过来的参数
        if (getArguments() != null) {
            ImgId = getArguments().getInt("ImgId");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shuffling_pics, container, false);
        imageView = view.findViewById(R.id.imageView_shufflingItem);
        imageView.setImageResource(ImgId);
        return view;
    }
}
