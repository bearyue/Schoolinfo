package com.example.school;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.bear.activity.activity_map;
import com.bear.activity.activity_schoolinfo;
import com.bear.activity.activity_shop;
import com.bear.activity.activity_web;

/**
 * Created by bear on 2015/3/25.
 */
public class ThirdFragment extends Fragment {

    RelativeLayout first;
    RelativeLayout second;
    RelativeLayout third;
    RelativeLayout fouth;
    private MainActivity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_third, container,false);

        first= (RelativeLayout) view.findViewById(R.id.first);
        second= (RelativeLayout) view.findViewById(R.id.second);
        third= (RelativeLayout) view.findViewById(R.id.third);
        fouth= (RelativeLayout) view.findViewById(R.id.fouth);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setBackgroundResource(R.drawable.third_tab_bg);
                Intent intent = new Intent(activity, activity_schoolinfo.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second.setBackgroundResource(R.drawable.third_tab_bg);
                Intent intent = new Intent(activity, activity_schoolinfo.class);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                third.setBackgroundResource(R.drawable.third_tab_bg);
                startActivity(new Intent(activity, activity_shop.class));
            }
        });
        fouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fouth.setBackgroundResource(R.drawable.third_tab_bg);
                startActivity(new Intent(activity, activity_map.class));
            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;

    }
}
