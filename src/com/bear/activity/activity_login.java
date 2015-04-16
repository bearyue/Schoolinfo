package com.bear.activity;

import android.app.ProgressDialog;
import android.hardware.usb.UsbRequest;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;
import com.bear.util.NotificationsUtil;
import com.bear.util.SharedPreferencesUtil;
import com.bear.util.ipaddressUtil;
import com.bear.http.loginhttp;
import com.example.school.*;
import com.example.school.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class activity_login extends Activity implements View.OnClickListener {
	
	
	Button loginbtn ;
    Button registerbtn;
    EditText unametext;
    EditText upwdtext;
    Button testbtn;
    ProgressDialog mProgressDialog;
    String uname;
    String upassword;
    SharedPreferencesUtil userinfo;
    int getuserid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //SharedPreferencesUtil userinfo =new SharedPreferencesUtil(activity_login.this);
        initview();
        initonclick();
        //String u= userinfo.getName();
      //  Toast.makeText(getApplicationContext(), "sp :"+u, Toast.LENGTH_SHORT).show();  //密码错误

	}



    private void initview() {
        loginbtn = (Button) findViewById(R.id.key_login);
        registerbtn = (Button) findViewById(R.id.key_register);
        testbtn = (Button) findViewById(R.id.test_button);
        unametext= (EditText) findViewById(R.id.username);
        upwdtext= (EditText) findViewById(R.id.userpassword);
    }

    private void initonclick() {
        loginbtn.setOnClickListener(this);
        registerbtn.setOnClickListener(this);
        testbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        uname=unametext.getText().toString();
        upassword=upwdtext.getText().toString();
        switch (v.getId())
        {
            case R.id.key_login:
                if(usernameEditTextFieldIsValid())
                {
                    if(userpasswordEditTextFieldIsValid())
                    {
                        System.out.println(uname+""+upassword);
                       LoginTask logintask= new LoginTask();
                        logintask.execute(uname,upassword);
                        break;
                    }
                    Toast.makeText(getApplicationContext(), "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(getApplicationContext(), "用户名不能为空",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.key_register:
                Intent registerintent = new Intent(this,activity_register.class);
                startActivity(registerintent);
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
              //  finish();
                break;
            case R.id.test_button:
                Intent mainintant = new Intent(this,MainActivity.class);
                startActivity(mainintant);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
        }
    }

    //异步处理
    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        private Exception mReason;

        @Override
        protected void onPreExecute() {

            showProgressDialog();
        }

        int b=3;
        @Override
        protected Boolean doInBackground(String... params) {

            loginhttp lg = new loginhttp();
            int result1 =lg.Login(params[0], params[1],0);
            int result2 =lg.Login(params[0], params[1], 1);


            System.out.println("u+p=" + params[0] + "+" + params[1]);
            try {
                if (result1==0) {
                    getuserid=lg.getId();
                    return  true;
                }else if(result1==2)
                {
                   mReason=ipaddressUtil.errormessage;
                    return false;
                }
                else if(result2==0){
                    mReason=null;
                    b=0;//密码错误
                    System.out.println("password wrong");
                    return  false;
                    }
                    else if (result2==1){
                        b=1;    //用户不存在
                        return false;
                    }
                    else{
                    mReason=ipaddressUtil.errormessage;
                    b=3;
                    return false;
                    }

            } catch (Exception e) {
                mReason = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            userinfo =new SharedPreferencesUtil(activity_login.this);
            if (result) {
                userinfo.setName(uname);
                userinfo.setPasswd(upassword);
                userinfo.setLoginState(true);
                userinfo.setId(getuserid);
              //  Toast.makeText(getApplicationContext(), "userid= "+userinfo.getId(), Toast.LENGTH_SHORT).show();  //密码错误
                Intent intent = new Intent(activity_login.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            } else {
                if(b==0)
                {
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();  //密码错误
                }
                else if(b==1)
                {
                    Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
                }
                NotificationsUtil.ToastReasonForFailure(activity_login.this, mReason);
            }
            dismissProgressDialog();
        }

        @Override
        protected void onCancelled() {
           dismissProgressDialog();
        }
    }


    private ProgressDialog showProgressDialog() {
        if (mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("登录");
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


   //判断用户名密码是否为空
    private boolean usernameEditTextFieldIsValid() {

        return !TextUtils.isEmpty(unametext.getText());
    }

    private boolean userpasswordEditTextFieldIsValid() {
        return !TextUtils.isEmpty(upwdtext.getText());
    }



    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                 exit();
                //exitByDoubleClick();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                break;
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }

	long exitTime=0;
    //双击返回键退出
    private void exit()
    {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), "quit",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }



}
