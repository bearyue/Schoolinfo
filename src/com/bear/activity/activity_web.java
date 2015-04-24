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
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //优先使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置可以支持缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        // webView.setInitialScale(80);//为25%，最小缩放等级
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        //WebView加载web资源
        webView.loadUrl(address);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if(url.indexOf("tel:")<0){              //判断是否为电话
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

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                //clearWebViewCache();
                finish();//退出程序
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clearWebViewCache() {
        webView.clearCache(true);
    }

}
