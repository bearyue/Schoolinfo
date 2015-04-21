package com.example.school;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Matrix;
import android.support.v4.app.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import com.bear.activity.activity_new_message;
import com.bear.activity.activity_setting;
import com.bear.activity.activity_web;
import com.bear.util.InternalFileUtil;
import com.bear.util.SharedPreferencesUtil;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
//setheaderscroll 950
//overscrollhelper 132-135
    private ViewPager mViewPager ;
    private ArrayList<Fragment> fragmentList ;

    //Tab
    private LinearLayout tab_first;
    private LinearLayout tab_second;
    private LinearLayout tab_third;
    private LinearLayout tab_fouth;

    private ImageButton tab_first_img;
    private ImageButton tab_second_img;
    private ImageButton tab_third_img;
    private ImageButton tab_fouth_img;

    private ImageButton tab_newmessagebtn;
    private ImageView news_refresh_button;
    Animation operatingAnim;
    ImageButton top10_btn;
    boolean top10pressed =false;
    private TextView tab_first_text;

    private TextView tab_second_text;
    private TextView tab_third_text;
    private TextView tab_fouth_text;
    private TextView title;
    Fragment first_Fragment;

    Fragment second_Fragment;
    Fragment third_Fragment;
    Fragment fouth_Fragment;
    private ImageView cursor;

    private int currIndex;//��ǰҳ�����
    private int bmpW;//����ͼƬ���
    private int offset;//ͼƬ�ƶ���ƫ����
    //���ݸ�Fragment������
    String sendcontent;

    int senduid;
    private SecondFragment secondfragment_object;

    private FirstFragment firstfragment_object;
    private FragmentTransaction transaction;
    long exitTime=0;

    private ImageButton setting_btn;

    MyFragmentPagerAdapter myFragmentPagerAdapter;
    SharedPreferencesUtil userinfo;
    public static MainActivity instance = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
        instance = this;
        setStyleCustom();
            initviewpager();
            initviews();
            initonclick();
            initcursor();
          //  setselect(0);
        news_refresh_button.startAnimation(operatingAnim);
    }

    //fregment����
    public String sendmessagetofragment() {
        return "what ";
    }
    //fouth_fragmentע����ת



    private void initviewpager() {
        mViewPager= (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setOffscreenPageLimit(3);//��ViewPager�໺��һ��ҳ��
        fragmentList = new ArrayList<Fragment>();
        first_Fragment= new FirstFragment();
        second_Fragment= new SecondFragment();
        third_Fragment= new ThirdFragment();
        fouth_Fragment= new FouthFragment();
        fragmentList.add(first_Fragment);
        fragmentList.add(second_Fragment);
        fragmentList.add(third_Fragment);
        fragmentList.add(fouth_Fragment);
        //����������
        //mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList));
        myFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //��ȡfagmentʵ������һ

        mViewPager.setCurrentItem(0);//���õ�ǰ��ʾ��ǩҳΪ��һҳ
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                int one = offset *2 +bmpW;//��������ҳ���ƫ����
                resetimg();
                Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//ƽ�ƶ���
                currIndex = arg0;
                animation.setFillAfter(true);//������ֹʱͣ�������һ֡����Ȼ��ص�û��ִ��ǰ��״̬
                animation.setDuration(150);//��������ʱ��0.2��
                cursor.startAnimation(animation);//����ImageView����ʾ������

                switch (currentItem)
                {
                    case 0:
                        tab_first_img.setImageResource(R.drawable.tab_first_selected);
                        title.setText(R.string.first);
                        news_refresh_button.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        tab_second_img.setImageResource(R.drawable.tab_second_selected);
                        title.setText(R.string.second);
                        tab_newmessagebtn.setVisibility(View.VISIBLE);
                        top10_btn.setVisibility(View.VISIBLE);
                        secondfragment_object=(SecondFragment)myFragmentPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                        break;
                    case 2:
                        tab_third_img.setImageResource(R.drawable.tab_third_selected);
                        title.setText(R.string.third);
                        break;
                    case 3:
                        tab_fouth_img.setImageResource(R.drawable.tab_fouth_selected);
                        title.setText("");
                        setting_btn.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });//ҳ��仯ʱ�ļ�����

    }


    private void initviews() {
        mViewPager= (ViewPager) findViewById(R.id.id_viewpager);
        tab_first= (LinearLayout) findViewById(R.id.tab_first);
        tab_first_img= (ImageButton) findViewById(R.id.tab_first_img);

       // tab_first_text= (TextView) findViewById(R.id.tab_first_text);

        tab_second= (LinearLayout) findViewById(R.id.tab_second);
        tab_second_img= (ImageButton) findViewById(R.id.tab_second_img);
       // tab_second_text= (TextView) findViewById(R.id.tab_second_text);

        tab_third= (LinearLayout) findViewById(R.id.tab_third);
        tab_third_img= (ImageButton) findViewById(R.id.tab_third_img);
     //   tab_third_text= (TextView) findViewById(R.id.tab_third_text);

        tab_fouth= (LinearLayout) findViewById(R.id.tab_fouth);
        tab_fouth_img= (ImageButton) findViewById(R.id.tab_fouth_img);
      //  tab_fouth_text= (TextView) findViewById(R.id.tab_fouth_text);

        title= (TextView) findViewById(R.id.title);
        tab_newmessagebtn= (ImageButton) findViewById(R.id.new_message_button);
        news_refresh_button= (ImageView) findViewById(R.id.news_refresh_button);

        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.refresh_button);//���°�ť����
        LinearInterpolator lin = new LinearInterpolator();//setInterpolator��ʾ������ת���ʡ�LinearInterpolatorΪ����Ч��
        operatingAnim.setInterpolator(lin);

        top10_btn= (ImageButton) findViewById(R.id.top10_button);
        setting_btn= (ImageButton) findViewById(R.id.setting);
    }

    private void initonclick() {
        tab_first.setOnClickListener(this);
        tab_second.setOnClickListener(this);
        tab_third.setOnClickListener(this);
        tab_fouth.setOnClickListener(this);
        tab_newmessagebtn.setOnClickListener(this);
        news_refresh_button.setOnClickListener(this);
        top10_btn.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tab_first:
                System.out.println("First");
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tab_second:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_third:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_fouth:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.new_message_button:
                userinfo=new SharedPreferencesUtil(this);
                if(userinfo.getLoginState())
                {
                    Intent intent =new Intent(this,activity_new_message.class);
                    startActivityForResult(intent,1);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "���¼�󷢲�", Toast.LENGTH_SHORT).show();  //�������
                }
                break;
            case R.id.news_refresh_button:
                if (operatingAnim != null) {
                    news_refresh_button.startAnimation(operatingAnim);
                }
                firstfragment_object= (FirstFragment) myFragmentPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                firstfragment_object.refreshlist();
                break;
            case R.id.top10_button:
                if(!top10pressed){
                    top10_btn.setBackgroundResource(R.drawable.top10_button_pressed);
                    tab_newmessagebtn.setVisibility(View.INVISIBLE);
                    top10pressed=true;
                }else{
                    top10_btn.setBackgroundResource(R.drawable.top10_button);
                    tab_newmessagebtn.setVisibility(View.VISIBLE);
                    top10pressed=false;
                }
                secondfragment_object.gettop10();
                break;
            case R.id.setting:
                Intent intent =new Intent(this,activity_setting.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    public void resetimg()
    {
        tab_first_img.setImageResource(R.drawable.tab_first);
        tab_second_img.setImageResource(R.drawable.tab_second);
        tab_third_img.setImageResource(R.drawable.tab_third);
        tab_fouth_img.setImageResource(R.drawable.tab_fouth);
        tab_newmessagebtn.setVisibility(View.INVISIBLE);
        news_refresh_button.clearAnimation();
        news_refresh_button.setVisibility(View.INVISIBLE);
        top10_btn.setVisibility(View.INVISIBLE);
        setting_btn.setVisibility(View.INVISIBLE);
    }

    public void initcursor(){
        cursor = (ImageView)findViewById(R.id.cursor);
        bmpW = cursor.getLayoutParams().width;// ��ȡ�ײ������ߵĿ��
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW/4 - bmpW)/2;

        //imgageview����ƽ�ƣ�ʹ�»���ƽ�Ƶ���ʼλ�ã�ƽ��һ��offset��
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);
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


    //˫�����ؼ��˳�
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

    //��activity_new_message�������
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK)
        {
            Bundle bundle =new Bundle();
            bundle=data.getExtras();
            sendcontent =bundle.getString("content");
            System.out.println("result:" + sendcontent);
            //��ȡfagmentʵ������һ
           // fragment=(SecondFragment)myFragmentPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
            //��ȡʵ��������
            // fragment = (SecondFragment)getSupportFragmentManager().findFragmentByTag(
            //       "android:switcher:" + R.id.id_viewpager + ":1"+mViewPager.getCurrentItem());
            Toast.makeText(getApplicationContext(), "���ͳɹ���", Toast.LENGTH_SHORT).show();
            secondfragment_object.refreshlist();//�ѻ�õ����ݴ���fragment���ظ���
            System.out.println("getresult");
        }
        System.out.println("getnoresult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    //first_fragment�� web address ����activity����start web activity
    public void start_webactivity (String address){
        String web_address=address;
        Intent intent = new Intent(this, activity_web.class);
        intent.putExtra("address",web_address);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

    /**
     *����֪ͨ����ʽ - ����֪ͨ��Layout
     */
    private void setStyleCustom(){
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(this,R.layout.customer_notitfication_layout,R.id.notitfication_icon, R.id.notitfication_title, R.id.notitfication_text);
        builder.layoutIconDrawable = R.drawable.n_icon;
        builder.statusBarDrawable = R.drawable.n_icon;
       // builder.notificationDefaults=Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE|Notification.FLAG_SHOW_LIGHTS;
        builder.notificationDefaults=Notification.DEFAULT_ALL;
        JPushInterface.setDefaultPushNotificationBuilder(builder);
        //Toast.makeText(this,"Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }




}

	


