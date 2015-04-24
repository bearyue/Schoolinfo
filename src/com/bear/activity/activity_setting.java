package com.bear.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cn.jpush.android.api.JPushInterface;
import com.bear.util.SharedPreferencesUtil;
import com.example.school.MainActivity;
import com.example.school.R;
import java.io.File;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
/**
 * Created by bear on 2015/4/20.
 */
public class activity_setting extends Activity implements View.OnClickListener {
    Button cancellation_btn;
    RelativeLayout clean_item;
    ImageButton back_btn;
    SharedPreferencesUtil userinfo;
    SharedPreferencesUtil setting;
    Switch notice_sw;
    TextView sizetext;

    String web_cache_dir=null;
    String picasso_cache_dir=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        userinfo= new SharedPreferencesUtil(this.getApplication());
        setting= new SharedPreferencesUtil(this.getApplication());
        web_cache_dir =getApplication().getDir("webview",MODE_PRIVATE).toString()+"/Cache";
        picasso_cache_dir =getApplicationContext().getCacheDir().toString()+"/picasso-cache";

        initview();
        initonclick();
        if(!userinfo.getLoginState())
        {
            cancellation_btn.setText("请登录");
        }
        if(!setting.getisPush()){
            notice_sw.setChecked(false);
        }
        notice_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // Toast.makeText(getApplicationContext(), "开", Toast.LENGTH_SHORT).show();
                    JPushInterface.resumePush(getApplicationContext());
                    setting.setIsPush(true);
                }else {
                   // Toast.makeText(getApplicationContext(), "关", Toast.LENGTH_SHORT).show();
                    JPushInterface.stopPush(getApplicationContext());
                    setting.setIsPush(false);
                }
            }
        });

        sizetext.setText(GetCacheSize()+"MB");
    }

    private void initview() {
        cancellation_btn= (Button) findViewById(R.id.cancellation);
        back_btn= (ImageButton) findViewById(R.id.setting_back);
        notice_sw = (Switch) findViewById(R.id.notice_switch);
        clean_item= (RelativeLayout) findViewById(R.id.clean_item);
        sizetext = (TextView) findViewById(R.id.size_text);
    }

    private void initonclick() {
        cancellation_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        clean_item.setOnClickListener(this);
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
            case R.id.clean_item:
                CleanConfirm();
                break;
        }
    }

    private String GetCacheSize(){
        DecimalFormat df=new DecimalFormat(".##");
        long s1 =getFolderSize(new File(web_cache_dir));
        long s2 =getFolderSize(new File(picasso_cache_dir));
        double size1=Double.parseDouble(s1 + "");
        double size2=Double.parseDouble(s2+"");
        size1=(size1+size2)/1024/1024;
        String cachesize =df.format(size1);
        //Toast.makeText(getApplicationContext(), cachesize+"MB", Toast.LENGTH_SHORT).show();
        return cachesize;
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public static long getFolderSize(File file){
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public void CleanConfirm() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("确认要删除所有缓存吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        if (activity_web.web_instance != null) {
                            activity_web.web_instance.clearWebViewCache();
                        } else {
                            deleteFilesByDirectory(new File(web_cache_dir));
                        }
                        deleteFilesByDirectory(new File(picasso_cache_dir));
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

}
