package com.bear.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.*;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.school.MainActivity;
import com.example.school.R;

/**
 * Created by bear on 2015/4/8.
 */
public class activity_web extends Activity {

    String address;
    WebView webView;
    WebView webView1 = null;
    ImageButton closebtn;
    ProgressBar web_pb;
    public static activity_web web_instance =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web);
        web_instance=this;
        Bundle bundle = getIntent().getExtras();
        address = bundle.getString("address");
        //Toast.makeText(this, "address: "+address, Toast.LENGTH_SHORT).show();
        web_pb= (ProgressBar) findViewById(R.id.web_pb);
        closebtn = (ImageButton) findViewById(R.id.news_close);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        });
        init(address);
    }

    private void init(String address){
        webView = (WebView) findViewById(R.id.webView);
        //����֧��javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //����ʹ�û���
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // ���ÿ���֧������
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        // webView.setInitialScale(80);//Ϊ25%����С���ŵȼ�
        //����Ӧ��Ļ
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        //WebView����web��Դ
        webView.loadUrl(address);
        //����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
                if(url.indexOf("tel:")<0){              //�ж��Ƿ�Ϊ�绰
                    view.loadUrl(url);
                }else{
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                }
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    web_pb.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == web_pb.getVisibility()) {
                        web_pb.setVisibility(View.VISIBLE);
                    }
                    web_pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    //��д�������������ص��߼�
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//������һҳ��
                return true;
            }
            else
            {
                //clearWebViewCache();
                finish();//�˳�����
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clearWebViewCache() {
        webView.clearCache(true);
    }

}
