package com.bear.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cn.jpush.android.api.JPushInterface;
import com.bear.util.SharedPreferencesUtil;
import com.example.school.MainActivity;
import com.example.school.R;

/**
 * Created by bear on 2015/4/20.
 */
public class activity_setting extends Activity implements View.OnClickListener {
    Button cancellation_btn;
    ImageButton back_btn;
    SharedPreferencesUtil userinfo;
    SharedPreferencesUtil setting;
    Switch notice_sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        userinfo= new SharedPreferencesUtil(this.getApplication());
        setting= new SharedPreferencesUtil(this.getApplication());
        initview();
        initonclick();
        if(!userinfo.getLoginState())
        {
            cancellation_btn.setText("ÇëµÇÂ¼");
        }
        if(!setting.getisPush()){
            notice_sw.setChecked(false);
        }
        notice_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // Toast.makeText(getApplicationContext(), "¿ª", Toast.LENGTH_SHORT).show();
                    JPushInterface.resumePush(getApplicationContext());
                    setting.setIsPush(true);
                }else {
                   // Toast.makeText(getApplicationContext(), "¹Ø", Toast.LENGTH_SHORT).show();
                    JPushInterface.stopPush(getApplicationContext());
                    setting.setIsPush(false);
                }
            }
        });
    }

    private void initview() {
        cancellation_btn= (Button) findViewById(R.id.cancellation);
        back_btn= (ImageButton) findViewById(R.id.setting_back);
        notice_sw = (Switch) findViewById(R.id.notice_switch);
    }

    private void initonclick() {
        cancellation_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancellation:
                cancellation_btn.setBackgroundResource(R.drawable.cancellation_bg);
                userinfo.setLoginState(false);
                startActivity(new Intent(this,activity_login.class));
                finish();
                MainActivity.instance.finish();
                break;
            case R.id.setting_back:
                finish();
                break;
        }
    }
}
