package com.example.school;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import com.bear.activity.activity_login;
import com.bear.activity.activity_my_collection;
import com.bear.util.SharedPreferencesUtil;
import com.bear.util.ipaddressUtil;

/**
 * Created by bear on 2015/3/25.
 */
public class FouthFragment extends Fragment {

   // Button cancellation_btn;
    SharedPreferencesUtil userinfo;
    private MainActivity activity;
    RelativeLayout my_info_item;
    RelativeLayout my_collection_item;
    TextView my_username;
    ImageButton user_icon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fouth, container,false);

    //    cancellation_btn = (Button)view.findViewById(R.id.cancellation_button);
        my_info_item= (RelativeLayout) view.findViewById(R.id.my_info);
        my_collection_item= (RelativeLayout) view.findViewById(R.id.my_collection);
        my_username = (TextView) view.findViewById(R.id.my_username);
        user_icon = (ImageButton) view.findViewById(R.id.user_icon);

        userinfo= new SharedPreferencesUtil(this.getActivity());
        if(!userinfo.getLoginState())
        {
           // cancellation_btn.setText("请登录");
        }else{
            String name =userinfo.getName();
            my_username.setText(name);
            user_icon.setBackgroundResource(R.drawable.logined_icon);
        }

        /*cancellation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellation_btn.setBackgroundResource(R.drawable.cancellation_bg);
                userinfo.setLoginState(false);
                startActivity(new Intent(activity,activity_login.class));
                activity.finish();
            }
        });*/

        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userinfo.getLoginState())
                {
                    startActivity(new Intent(activity, activity_login.class));
                    activity.finish();
                }
            }
        });

        my_info_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_info_item.setBackgroundResource(R.drawable.my_info_list);
                if(!userinfo.getLoginState()) {
                    Toast.makeText(FouthFragment.this.getActivity(), "请登录后查看", Toast.LENGTH_SHORT).show();  //网络错误
                }
            }
        });
        my_collection_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_collection_item.setBackgroundResource(R.drawable.my_info_list);
                if(!userinfo.getLoginState()) {
                    Toast.makeText(FouthFragment.this.getActivity(), "请登录后查看", Toast.LENGTH_SHORT).show();  //网络错误
                }else{
                    startActivity(new Intent(activity,activity_my_collection.class));
                }
            }
        });
        return view;
    }


    //绑定主ACTIVTTY,用于获取activity实例
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }


}
