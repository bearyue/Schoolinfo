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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bear.model.School;
import com.bear.model.Shop;
import com.bear.util.BitmapCache;
import com.bear.util.GsonUtil;
import com.bear.util.ipaddressUtil;
import com.example.school.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bear on 2015/4/24.
 */
public class activity_schoolinfo extends Activity {

    private static final String URLSTRING = ipaddressUtil.IP + "/SchoolInfo/servlet/SchoolInfoServlet";
    ImageButton back_btn;
    TextView text_title;
    ListView lv;

    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    MyAdapter adapter =null;
    List<School> schoolinfolist=null;
    ProgressDialog mProgressDialog;

    int flag =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shcoolinfo);
        Bundle bundle = getIntent().getExtras();
        flag = bundle.getInt("flag");
        adapter =new MyAdapter(this);

        initview();
        lv.setAdapter(adapter);
        new GetData().execute(URLSTRING);
    }

    private void initview() {
        back_btn = (ImageButton) findViewById(R.id.schoolinfo_back);
        text_title = (TextView) findViewById(R.id.schoolinfo_title);
        lv = (ListView) findViewById(R.id.schoolinfo_listview);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(flag==1){
            text_title.setText("学院（部）");
        }else{
            text_title.setText("职能处室");
        }
    }


    private class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.schoolinfo_item,null);
                holder = new ViewHolder();
                /**得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.s_name);
                holder.other = (TextView) convertView.findViewById(R.id.s_other);
                holder.website = (RelativeLayout) convertView.findViewById(R.id.web_layout);
                holder.phone = (RelativeLayout) convertView.findViewById(R.id.phone_layout);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.website.setBackgroundResource(R.drawable.schoolinfo_btnbg);
                    String address = dataList.get(position).get("school_website").toString();
                    Intent intent = new Intent(getApplicationContext(), activity_web.class);
                    intent.putExtra("address",address);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
            });

            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.phone.setBackgroundResource(R.drawable.schoolinfo_btnbg);
                    String phonenumber = dataList.get(position).get("school_tel").toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonenumber));
                    startActivity(intent);
                }
            });

            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(dataList.get(position).get("school_name").toString());
            holder.other.setText(dataList.get(position).get("school_other").toString());
            return convertView;
        }
    }

    /**存放控件*/
    public final class ViewHolder{
        public TextView title;
        public TextView other;
        public RelativeLayout website;
        public RelativeLayout phone;
    }

    private class GetData extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            showProgressDialog();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                result = GsonUtil.getjson(params[0], 0);
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
                schoolinfolist = GsonUtil.getSchoolinfoListFromJson(result);
                System.out.println("shoplist =  " + schoolinfolist.toString());
                dataList = getadapterdata(schoolinfolist);
                System.out.println("finish   " + dataList.size());
                adapter.notifyDataSetChanged();
            }
            dismissProgressDialog();
            super.onPostExecute(result);
        }
    }

    public List<Map<String, Object>> getadapterdata(List list) {
        List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            School school = (School) list.get(i);
            map.put("school_id", school.getSchool_id());
            map.put("school_name", school.getSchool_name());
            map.put("school_website", school.getSchool_website());
            map.put("school_tel", school.getSchool_tel());
            map.put("school_other", school.getSchool_other());
            map.put("school_type", school.getSchool_type());
            if(school.getSchool_type()==1){
                data1.add(map);
            }else{
                data2.add(map);
            }
        }
        if(flag==1){
            return data1;
        }else{
            return data2;
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
