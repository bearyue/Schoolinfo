package com.bear.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bear.http.messagehttp;
import com.bear.util.SharedPreferencesUtil;
import com.example.school.R;

/**
 * Created by bear on 2015/3/31.
 */
public class activity_new_message extends Activity implements View.OnClickListener {

    Button cancel_button ;
    Button send_button;
    EditText text_content;
    String content;
    SharedPreferencesUtil userinfo;
    int uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_message);

        initview();
        onclick();

    }

    private void onclick() {
        cancel_button.setOnClickListener(this);
        send_button.setOnClickListener(this);
    }

    private void initview() {
        cancel_button= (Button) findViewById(R.id.cancel_button);
        send_button= (Button) findViewById(R.id.send_message_button);
        text_content= (EditText) findViewById(R.id.text_content);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cancel_button:
               /* Intent intent =new Intent();
                Bundle bundle =new Bundle();
                bundle.putString("content","hehe");
                intent.putExtras(bundle);
                activity_new_message.this.setResult(RESULT_OK,intent);*/
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.send_message_button:
                content =text_content.getText().toString();
                System.out.println("getcontent = "+content);
                Toast.makeText(getApplicationContext(), "正在发送.....", Toast.LENGTH_SHORT).show();
                SendMessageTask sendmessagetask=new SendMessageTask();
                sendmessagetask.execute(content);

                break;
        }


    }


    private class SendMessageTask extends AsyncTask<String,Void,Boolean> {
        messagehttp newmessage =new messagehttp();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            userinfo=new SharedPreferencesUtil(activity_new_message.this);
            uid = userinfo.getId();
            if(newmessage.insert_new_message(uid,params[0]))
            {
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                Intent intent =new Intent();
                Bundle bundle =new Bundle();
                bundle.putString("content",content);
                bundle.putInt("uid",uid);
                intent.putExtras(bundle);
                activity_new_message.this.setResult(RESULT_OK,intent);

               // finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "发送失败，请检查网络", Toast.LENGTH_SHORT).show();

            }
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

            finish();//退出程序
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

}
