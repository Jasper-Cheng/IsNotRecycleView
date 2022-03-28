package com.example.isnotrecycleview;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.isnotrecycleview.Data.ViewData;
import com.example.isnotrecycleview.View.IsNotRecycleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private IsNotRecycleView isNotRecycleView;
    private List<ViewData> dateList=new ArrayList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

        isNotRecycleView=findViewById(R.id.notRecycleView);

        for(int i=0;i<12;i++){
            dateList.add(new ViewData());
        }

        List<Integer> list=new ArrayList<>();
        list.add(0);
        list.add(3);
        isNotRecycleView.setDataList(dateList);
//        isNotRecycleView.setShowIndex(list);
//        isNotRecycleView.setCertainlyCount(4);

    }
}