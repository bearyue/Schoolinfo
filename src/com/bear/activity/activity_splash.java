package com.bear.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import com.bear.util.SharedPreferencesUtil;
import com.example.school.MainActivity;
import com.example.school.R;

import static java.lang.Thread.sleep;

/**
 * Created by bear on 2015/3/27.
 */
public class activity_splash extends InstrumentedActivity{

    LinearLayout ls;
    SharedPreferencesUtil userinfo;
    boolean loginstate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ls= (LinearLayout) findViewById(R.id.splash);
      //  ls.setBackgroundResource(R.drawable.splash);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        System.out.println("init jpush");
        initsp();
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 1700);
    }

    private void initsp() {
        userinfo =new SharedPreferencesUtil(activity_splash.this);
        loginstate=userinfo.getLoginState();

    }

    private class splashhandler implements Runnable {
        public void run() {
            if (loginstate)
            {
                   startActivity(new Intent(getApplication(), MainActivity.class));
                   overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
            else{
                startActivity(new Intent(getApplication(),activity_login.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
            activity_splash.this.finish();
        }
    }
}
