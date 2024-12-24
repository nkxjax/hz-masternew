package com.example.finalhomework.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.finalhomework.AttractionListActivity;
import com.example.finalhomework.FunActivitiesActivity;
import com.example.finalhomework.GuideHZ;
import com.example.finalhomework.KnowJiuZhaiActivity;
import com.example.finalhomework.LoginActivity;
import com.example.finalhomework.NewsActivity;
import com.example.finalhomework.OverAllActivity;
import com.example.finalhomework.R;
import com.example.finalhomework.TicketActivity;
import com.example.finalhomework.util_classes.MyPagerAdapter;
import com.example.finalhomework.util_classes.News;
import com.example.finalhomework.util_classes.NewsDBHelper;
import com.example.finalhomework.util_classes.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private ListView broadcastListView;
    private View view;
    //从strings.xml中获取数据
    private String[] broadcasts;
    private String[] bcDateList;
    private ViewPager viewPager_shuffling;
    private MyPagerAdapter shufflingAdapter;
    private NewsDBHelper newsDBHelper;

    //声明图片资源
    int[] shufflingImgIds = new int[]{R.drawable.shuffling_pic1, R.drawable.shuffling_pic2, R.drawable.shuffling_pic3};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsDBHelper = new NewsDBHelper(getContext());

        // 获取新闻数据并设置适配器
        View view = initViews(inflater,container,savedInstanceState);
        return view;
    }

    private View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        //初始化
        broadcastListView = view.findViewById(R.id.listView_broadcast);//获取ListView
        broadcasts = getResources().getStringArray(R.array.broadcast_list);
        bcDateList = getResources().getStringArray(R.array.broadcastDate_list);
        viewPager_shuffling = view.findViewById(R.id.viewPager_shuffling);
        shufflingAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        view.findViewById(R.id.imageButton_overall).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton_knowjiuzhai).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton_avtivities).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton_ticketbook).setOnClickListener(this::onClick);

        //设置轮播图viewPager_shuffling
        for (int i = 0; i < shufflingImgIds.length; i++) {
            shufflingAdapter.addFragment(ShufflingPicsFragment.newInstance(shufflingImgIds[i]));
        }

        viewPager_shuffling.setPageTransformer(true,new ZoomOutPageTransformer());
        viewPager_shuffling.setAdapter(shufflingAdapter);

        //设置公告ListView
        //创建键值对列表
        // 假设你有一个新闻列表，每个新闻对象包含 title 和 date
        List<News> newsList = newsDBHelper.getAllNews();

        List<Map<String, Object>> newsListItems = new ArrayList<Map<String, Object>>();

// 添加数据到列表
        for (int i = 0; i < newsList.size(); i++) {
            // 创建一个单个键值对变量
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("newsTitle", newsList.get(i).getTitle());  // 使用新闻的标题
            listItem.put("newsDate", newsList.get(i).getPublishTime());    // 使用新闻的日期

            newsListItems.add(listItem);  // 添加至列表
        }

        SimpleAdapter newsAdapter = new SimpleAdapter(getContext(), newsListItems, R.layout.broadcast_item,
                new String[]{"newsTitle", "newsDate"},
                new int[]{R.id.textView_bc_content, R.id.textView_bc_date});

        broadcastListView.setAdapter(newsAdapter);


        broadcastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取 Activity 的引用
                Intent intent = new Intent(getActivity(), NewsActivity.class);

                // 使用不同的新闻 ID，根据 position 来选择不同的新闻 ID
                switch (position) {
                    case 0:  // 第一个点击项，显示 ID 为 1 的新闻
                        intent.putExtra("newsId", 1);
                        break;
                    case 1:  // 第二个点击项，显示 ID 为 2 的新闻
                        intent.putExtra("newsId", 2);
                        break;
                    case 2:  // 第三个点击项，显示 ID 为 3 的新闻
                        intent.putExtra("newsId", 3);
                        break;
                    default:
                        // 如果需要处理其他位置的点击
                        break;
                }

                // 启动 NewsActivity，并传递新闻 ID
                startActivity(intent);
            }



        });
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.imageButton_overall:
                intent = new Intent(getContext(), OverAllActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButton_knowjiuzhai:
                intent = new Intent(getContext(), GuideHZ.class);
                startActivity(intent);
                break;

            case R.id.imageButton_avtivities:
                intent = new Intent(getContext(), FunActivitiesActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButton_ticketbook:
                intent = new Intent(getContext(), AttractionListActivity.class);
                startActivity(intent);
                break;
        }
    }
}