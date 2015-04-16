package com.bear.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bear.http.shophttp;
import com.bear.model.Shop;
import com.bear.util.BitmapCache;
import com.bear.util.GsonUtil;
import com.bear.util.SharedPreferencesUtil;
import com.bear.util.ipaddressUtil;
import com.example.school.R;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bear on 2015/4/13.
 */
public class activity_my_collection extends Activity implements View.OnClickListener {
    private static final String URLSTRING = ipaddressUtil.IP + "/SchoolInfo/servlet/CollectionServlet";

    ImageButton back_btn;
    ImageView fresh_btn;
    Animation operatingAnim;
    SharedPreferencesUtil userinfo;
    int user_id;
    ExpandableListView lv;
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    boolean collection_btn_state =true;
    List<Shop> shoplist=null;
    int is_collected= 1;
    int is_refresh_success=0;

    MyExpandableAdapter expandadapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollection);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.refresh_button);//更新按钮动画
        LinearInterpolator lin = new LinearInterpolator();//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果
        operatingAnim.setInterpolator(lin);
        userinfo = new SharedPreferencesUtil(this);
        user_id=userinfo.getId();
        expandadapter = new MyExpandableAdapter(this);
        initview();
        initonclick();
        new GetDataTask().execute(URLSTRING);
        lv.setGroupIndicator(null);
        lv.setAdapter(expandadapter);
    }

    private void initview() {
        fresh_btn = (ImageView) findViewById(R.id.collection_refresh_button );
        back_btn = (ImageButton) findViewById(R.id.collection_back);
        lv = (ExpandableListView) findViewById(R.id.my_collection_list);

    }

    private void initonclick() {
       back_btn.setOnClickListener(this);
       fresh_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collection_back:
                finish();
                break;
            case R.id.collection_refresh_button:
                if (operatingAnim != null) {
                    fresh_btn.startAnimation(operatingAnim);
                    new GetDataTask().execute(URLSTRING);
                    if(is_refresh_success==1){
                        Handler delay = new Handler();
                        delay.postDelayed(new NewsUpdateToast(),1000);
                    }
                }
                break;
        }
    }

    private class GetDataTask extends AsyncTask<String,Void,String>{
        String result;
        @Override
        protected String doInBackground(String... params) {
            try {
                result = GsonUtil.getjson(params[0],3,user_id);
                System.out.println("collection list ="+result);
            } catch (Exception e) {
                System.out.println(e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null){
                is_refresh_success=0;
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();  //网络错误
            }else {
                    is_refresh_success=1;
                    shoplist=GsonUtil.getShopListFromJson(result);
                    dataList = getadapterdata(shoplist);
                    expandadapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }
    }

    public List<Map<String, Object>> getadapterdata(List list) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Shop shop = (Shop) list.get(i);
            map.put("shop_id", shop.getShop_id());
            map.put("shop_name", shop.getShop_name());
            map.put("shop_address", shop.getShop_address());
            map.put("shop_tel", shop.getShop_tel());
            map.put("shop_other", shop.getShop_other());
            map.put("shop_type", shop.getShop_type());
            map.put("shop_img", shop.getShop_img());
            map.put("collection_state", 1);//标志位
            data.add(map);
        }
        return  data;
    }

    public class MyExpandableAdapter extends BaseExpandableListAdapter {
        private LayoutInflater mInflater;
        private RequestQueue queue;
        private ImageLoader imageLoader;

        public MyExpandableAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new BitmapCache());
        }

        @Override
        public int getGroupCount() {
            return dataList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getgroupView " + groupPosition + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.shoplist_item, null);
                holder = new ViewHolder();
                /**得到各个控件的对象*/
                holder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
                holder.shop_address = (TextView) convertView.findViewById(R.id.shop_address);
                holder.shop_other = (TextView) convertView.findViewById(R.id.shop_other);
                holder.shop_img = (NetworkImageView) convertView.findViewById(R.id.shop_image);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.shop_name.setText(dataList.get(groupPosition).get("shop_name").toString());
            holder.shop_address.setText(dataList.get(groupPosition).get("shop_address").toString());
            // holder.shop_tel.setText(dataList.get(position).get("shop_tel").toString());
            holder.shop_other.setText(dataList.get(groupPosition).get("shop_other").toString());

            final String imgUrl = dataList.get(groupPosition).get("shop_img").toString();   //list[position];
            if (imgUrl != null && !imgUrl.equals("")) {
                holder.shop_img.setDefaultImageResId(R.drawable.loading);
                holder.shop_img.setErrorImageResId(R.drawable.error);
                holder.shop_img.setImageUrl(imgUrl, imageLoader);
            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildViewHolder childHolder;
            Log.v("MyexpandViewBase", "getchildView ");
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.shop_childlist_item, null);
                childHolder = new ChildViewHolder();
                /**得到各个控件的对象*/
                childHolder.call_btn = (RelativeLayout) view.findViewById(R.id.call_button);
                childHolder.collection_btn = (RelativeLayout) view.findViewById(R.id.collection_button);
                childHolder.share_btn = (RelativeLayout) view.findViewById(R.id.share_button);
                childHolder.collection_btn_icon = (ImageView) view.findViewById(R.id.collection_button_icon);
                view.setTag(childHolder);//绑定ViewHolder对象
            } else {
                childHolder = (ChildViewHolder) view.getTag();//取出ViewHolder对象
            }

            //根据标记对点开的item进行按钮状态的改变
            if (Integer.parseInt(dataList.get(groupPosition).get("collection_state").toString()) == 0) {
                childHolder.collection_btn_icon.setBackgroundResource(R.drawable.button_collection);
            } else {
                childHolder.collection_btn_icon.setBackgroundResource(R.drawable.button_collection_pressed);
            }

            childHolder.call_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phonenumber = dataList.get(groupPosition).get("shop_tel").toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonenumber));
                    startActivity(intent);
                    lv.collapseGroup(groupPosition);
                }
            });

            childHolder.share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = dataList.get(groupPosition).get("shop_name").toString();
                    String other = dataList.get(groupPosition).get("shop_other").toString();
                    shareToFriend(name, other);
                    lv.collapseGroup(groupPosition);
                }
            });

            childHolder.collection_btn.setOnClickListener(new View.OnClickListener() {
                String s_id = dataList.get(groupPosition).get("shop_id").toString();
                int position = Integer.parseInt(s_id)-1;
                /**
                 s_id-1的值刚好是，点击的item在shoplist中位置。分类后，无法通过groupposition获得当前点击的item在shoplist中的位置，所以用此方法代替
                 */
                @Override
                public void onClick(View v) {
                        if (Integer.parseInt(dataList.get(groupPosition).get("collection_state").toString()) == 0) {
                            collection_btn_state = true;
                            new Collect_Shop().execute(s_id);                                  //对数据库进行更新
                            Map<String, Object> map = dataList.get(groupPosition);
                            map.put("collection_state", 1);
                            dataList.set(groupPosition, map);
                        } else {
                            collection_btn_state = false;
                            new Collect_Shop().execute(s_id);
                            Map<String, Object> map = dataList.get(groupPosition);
                            map.put("collection_state", 0);
                            dataList.set(groupPosition, map);
                        }
                    lv.collapseGroup(groupPosition);
                }
            });
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public final class ViewHolder {
        public TextView shop_name;
        public TextView shop_address;
        public TextView shop_other;
        public NetworkImageView shop_img;
    }

    public final class ChildViewHolder {
        public RelativeLayout call_btn;
        public RelativeLayout collection_btn;
        public RelativeLayout share_btn;
        public ImageView collection_btn_icon;
    }

    //收藏店铺、取消收藏
    private class Collect_Shop extends AsyncTask<String, Void, Boolean> {
        shophttp sp = new shophttp();
        boolean result = false;

        @Override
        protected void onPreExecute() {
            if (collection_btn_state) {
                is_collected = 0;
            } else {
                is_collected = 1;
            }
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {
            result = sp.collect_shop(params[0], user_id, is_collected);
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(getApplicationContext(), "出错了，请稍候再试", Toast.LENGTH_SHORT).show();
            } else {
                if (collection_btn_state) {
                    Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void shareToFriend(String name, String other) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, "我看到" + name + "的最新优惠：" + other + "，一起参与吧！ (分享自北理XX)");
        startActivity(intent);
    }

    private class NewsUpdateToast implements Runnable {
        @Override
        public void run() {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String time =timeFormat.format(new Date());
            Toast.makeText(getApplicationContext(),"更新时间："+time, Toast.LENGTH_SHORT).show();
        }
    }
}
