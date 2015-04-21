package com.bear.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bear.http.shophttp;
import com.bear.model.Collection;
import com.bear.model.Shop;
import com.bear.util.BitmapCache;
import com.bear.util.GsonUtil;
import com.bear.util.SharedPreferencesUtil;
import com.bear.util.ipaddressUtil;
import com.example.school.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bear on 2015/4/11.
 */
public class activity_shop extends Activity {
    private static final String URLSTRING = ipaddressUtil.IP + "/SchoolInfo/servlet/ShopServlet";
    private static final String URLSTRING1 = ipaddressUtil.IP + "/SchoolInfo/servlet/CollectionServlet";

    ImageButton back_btn;
    ImageButton location_btn;
    ExpandableListView lv;
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    MyExpandableAdapter expandadapter = null;
    String getjson;
    List<Shop> shoplist=null;
    ProgressDialog mProgressDialog;
    Spinner sp;
    int flag = 0;
    boolean collection_btn_state = false;
    SharedPreferencesUtil userinfo;
    int user_id = 0;
    boolean loginstate = false;
    int is_collected = 0;
    boolean is_first =true;

    List<Collection> collect_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shop);

        back_btn = (ImageButton) findViewById(R.id.shop_back);
        location_btn = (ImageButton) findViewById(R.id.location_btn);
        sp = (Spinner) findViewById(R.id.shop_spinner);
        lv = (ExpandableListView) findViewById(R.id.shop_list);
        userinfo = new SharedPreferencesUtil(this);
        loginstate = userinfo.getLoginState();
        user_id = userinfo.getId();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_btn.setBackgroundResource(R.drawable.location_btn);
                String web_address="http://yuntu.amap.com/share/V3aUb2";
                Intent intent = new Intent(getApplication(), activity_location_web.class);
                intent.putExtra("address",web_address);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        });

        setarrayadapter();
        expandadapter = new MyExpandableAdapter(getApplication());
        lv.setGroupIndicator(null);
        lv.setAdapter(expandadapter);
        closelist();//监听扩展list，每次只允许一个展开。

        new GetCollectionList().execute(URLSTRING1);
    }

    private void setarrayadapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setPrompt("下拉菜单");
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                close_all_list();
                switch (position) {
                    case 0:
                        flag = 0;
                        if(is_first){
                            new GetDataTask().execute(URLSTRING);
                            is_first=false;
                        }else if (shoplist!=null){
                            dataList=getadapterdata(shoplist);
                        }
                        expandadapter.notifyDataSetChanged();
                        System.out.println(position);
                        break;
                    case 1:
                        flag = 1;
                        if(shoplist!=null){
                            dataList=getadapterdata(shoplist);
                        }
                        expandadapter.notifyDataSetChanged();
                        break;
                    case 2:
                        flag = 2;
                        if(shoplist!=null){
                            dataList=getadapterdata(shoplist);
                        }
                        expandadapter.notifyDataSetChanged();
                        break;
                    case 3:
                        flag = 3;
                        if(shoplist!=null){
                            dataList=getadapterdata(shoplist);
                        }
                        expandadapter.notifyDataSetChanged();
                        break;
                    case 4:
                        flag = 4;
                        if(shoplist!=null){
                            dataList=getadapterdata(shoplist);
                        }
                        expandadapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //将用户收藏表，与店铺表遍历，更改已收藏店铺的收藏按钮标志位
    private void AddDate(){
        for(int i=0;i<collect_list.size();i++){
            for(int j=0;j<shoplist.size();j++)
            if(shoplist.get(j).getShop_id()==collect_list.get(i).getShop_id()){
                shoplist.get(j).setCollection_state(1);
            }
        }
    }
    private class GetDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                result = GsonUtil.getjson(params[0], flag);
                System.out.println("json: " + result);

            } catch (Exception e) {
                System.out.println(e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(getApplicationContext(), ipaddressUtil.errormessage.toString(), Toast.LENGTH_SHORT).show();  //网络错误
            } else {
                shoplist = GsonUtil.getShopListFromJson(result);
                System.out.println("shoplist =  " + shoplist.toString());
                AddDate();
                dataList = getadapterdata(shoplist);
                System.out.println("finish   " + dataList.size());
                expandadapter.notifyDataSetChanged();
                close_all_list();
            }
            dismissProgressDialog();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            dismissProgressDialog();
        }
    }

    public List<Map<String, Object>> getadapterdata(List list) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> typedata = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> type1data = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> type2data = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> type3data = new ArrayList<Map<String, Object>>();
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
            map.put("collection_state", shop.getCollection_state());//标志位
            data.add(map);
            if(shop.getShop_type()==0){
                typedata.add(map);
            }else if(shop.getShop_type()==1){
                type1data.add(map);
            }
            else if(shop.getShop_type()==2){
                type2data.add(map);
            }
            else if(shop.getShop_type()==3){
                type3data.add(map);
            }
        }
        if(flag==0){
            return data;
        }else if(flag==1){
            return typedata;
        }else if(flag==2){
            return type1data;
        }else if(flag==3){
            return type2data;
        }else {
            return type3data;
        }

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

            final String imgUrl = ipaddressUtil.IP+dataList.get(groupPosition).get("shop_img").toString();   //list[position];
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
                    if (!loginstate) {
                        Toast.makeText(getApplicationContext(), "请登录后操作", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Integer.parseInt(dataList.get(groupPosition).get("collection_state").toString()) == 0) {
                            collection_btn_state = true;
                            new Collect_Shop().execute(s_id);                                  //对数据库进行更新
                            System.out.println(shoplist.get(position).getShop_id()+"set 1");
                            shoplist.get(position).setCollection_state(1);                    //对当前shoplist的collection_state进行修改，否则分类后收藏按钮状态不一
                           Map<String, Object> map = dataList.get(groupPosition);
                            map.put("collection_state", 1);
                            dataList.set(groupPosition, map);
                        } else {
                            collection_btn_state = false;
                            shoplist.get(position).setCollection_state(0);
                            System.out.println(shoplist.get(position).getShop_id()+"set 0");
                            new Collect_Shop().execute(s_id);
                            Map<String, Object> map = dataList.get(groupPosition);
                            map.put("collection_state", 0);
                            dataList.set(groupPosition, map);
                        }
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
        public TextView shop_tel;
        public TextView shop_other;
        public ImageButton tel_btn;
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
    //得到已收藏的表
    private class GetCollectionList extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String result = GsonUtil.getjson(params[0],2,user_id);
            System.out.println("collectionflag list ="+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null){
                Toast.makeText(getApplicationContext(),"网络错误", Toast.LENGTH_SHORT).show();  //网络错误
            }else{
                collect_list=GsonUtil.getCollectionListFromJson(result);
            }
            super.onPostExecute(result);
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

    private void closelist() {
        lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                for (int i = 0; i < expandadapter.getGroupCount(); i++) {
                    if (i != groupPosition
                            && lv.isGroupExpanded(groupPosition)) {
                        lv.collapseGroup(i);

                    }
                }
            }
        });
    }

    private void close_all_list() {
        for (int i = 0; i < expandadapter.getGroupCount(); i++) {
            if (lv.isGroupExpanded(i)) {
                lv.collapseGroup(i);
            }
        }
    }

    private ProgressDialog showProgressDialog() {

        if (mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("加载中");
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
}