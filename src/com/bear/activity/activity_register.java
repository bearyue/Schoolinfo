package com.bear.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.bear.util.MD5Util;
import com.bear.util.NotificationsUtil;
import com.bear.util.ipaddressUtil;
import com.bear.http.loginhttp;
import com.bear.http.registerhttp;
import com.example.school.R;
import android.view.View.OnClickListener;


/**
 * Created by bear on 2015/3/27.
 */
public class activity_register extends Activity{

    Button registerbtn ;
    ImageButton backbtn;

    EditText registername ;
    EditText registerpassward ;
    EditText Registerpassward2 ;
    EditText registeremail ;

    registerhttp register ;
    ProgressDialog mProgressDialog;

    MD5Util md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        init();
        onclick();

    }

    private void init() {
        registerbtn= (Button) findViewById(R.id.key_register2);
        backbtn= (ImageButton) findViewById(R.id.key_back);
        registername = (EditText) findViewById(R.id.registername);
        registerpassward = (EditText) findViewById(R.id.registerpassword);
        Registerpassward2 = (EditText) findViewById(R.id.confirmpassword);
        register=new registerhttp();
        md =new MD5Util();
    }

    private void onclick() {
        registerbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTask registertask= new RegisterTask();
                registertask.execute();
            }
        });

        backbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        });

    }

    private class RegisterTask extends AsyncTask<Void, Void, Integer> {
        private Exception mReason;
        int i=0;
        protected void onPreExecute() {
            showProgressDialog();
        }
        protected Integer doInBackground(Void... params) {
            loginhttp lg = new loginhttp();
            loginhttp check;
            try {
                String rname =registername.getText().toString().trim();
                String rpassword =registerpassward.getText().toString().trim();
                String rpassword2=Registerpassward2.getText().toString().trim();
                String mdstr =md.getMD5Str(rpassword);
                int result=99;
                System.out.println(rname+"     "+rpassword);
                if(rname==null||rname.length()<=0)
                {
                    result=4;
                }

                else if(rpassword==null||rpassword.length()<6)
                {
                    result=2;
                }
                else  if(!rpassword.equals(rpassword2))
                {
                    result=3;
                }
                else if(lg.Login(rname,mdstr,1)==0)
                {
                    result=1;
                }
                else{
                    if(register.register(rname, mdstr)){
                        result=5;
                    }
                    else{
                        result=6;
                        mReason=ipaddressUtil.errormessage;
                    }
                }
                return result;
            } catch (Exception e) {
                mReason = e;
                return 0;
            }
        }

        protected void onPostExecute(Integer result) {
            if(result==5){
                Toast.makeText(activity_register.this, "注册成功",
                        Toast.LENGTH_LONG).show();

                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
            else if(result==6){
                Toast.makeText(activity_register.this, "注册失败",
                        Toast.LENGTH_SHORT).show();
                NotificationsUtil.ToastReasonForFailure(activity_register.this, mReason);
            }
            else if(result==0){
                Toast.makeText(activity_register.this, "注册失败",
                        Toast.LENGTH_SHORT).show();
                NotificationsUtil.ToastReasonForFailure(activity_register.this, mReason);
            }
            else if(result==4){
                Toast.makeText(activity_register.this, "请输入用户名",
                        Toast.LENGTH_LONG).show();
            }
            else if(result==1){
                Toast.makeText(activity_register.this, "用户已存在",
                        Toast.LENGTH_LONG).show();
            }
            else if(result==2){
                Toast.makeText(activity_register.this, "密码长度不得小于6位",
                        Toast.LENGTH_LONG).show();
            }
            else if(result==3){
                Toast.makeText(activity_register.this, "密码不一致",
                        Toast.LENGTH_LONG).show();
            }



            dismissProgressDialog();
        }

        protected void onCancelled() {
            dismissProgressDialog();
        }
    }

    private ProgressDialog showProgressDialog() {
        if (mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("注册");
            dialog.setMessage("请稍候");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            mProgressDialog = dialog;
        }
        mProgressDialog.show();
        return mProgressDialog;
    }

    private void dismissProgressDialog() {
        try {
            mProgressDialog.dismiss();
        } catch (IllegalArgumentException e) {

        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                //exitByDoubleClick();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                break;
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }

}
