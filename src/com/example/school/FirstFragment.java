package com.example.school;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bear.model.News;
import com.bear.util.BitmapCache;
import com.bear.util.GsonUtil;
import com.bear.util.InternalFileUtil;
import com.bear.util.ipaddressUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by bear on 2015/3/25.
 */
public class FirstFragment extends Fragment implements View.OnClickListener {
    private static final String URLSTRING = ipaddressUtil.IP+"/SchoolInfo/servlet/NewsServlet";


    LinearLayout news_selector;
    LinearLayout notice_selector;
    TextView news_text;
    TextView notice_text;
    ListView lv;
    LinkedList<Map<String, Object>> dataList= new LinkedList<Map<String, Object>>();
    LinkedList<News> list=null;
    LinkedList<News> updatelist;
    MyAdapter adapter =null;
    boolean open =true;
    int flag =0;
    int type=0;
    int listsize=0;
    private MainActivity activity;
    int is_refresh_success=0;

    private String filename="newstext";
    InternalFileUtil ifile;
    LinkedList<News> savelist=null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_first, container,false);

        news_selector= (LinearLayout) view.findViewById(R.id.news_selector);;
        notice_selector= (LinearLayout) view.findViewById(R.id.notice_selector);
        news_text = (TextView) view.findViewById(R.id.news_selector_text);
        notice_text = (TextView) view.findViewById(R.id.notice_selector_text);
        lv= (ListView) view.findViewById(R.id.news_listview);
        adapter = new MyAdapter(FirstFragment.this.getActivity());
        lv.setAdapter(adapter);

        initonclick();
        ifile = new InternalFileUtil(this.getActivity(),filename);
        ifile.create();
        loadsavelist();
        if(open){
            new GetDataTask().execute(URLSTRING);
            open=false;
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(FirstFragment.this.getActivity(),
               //         "������item"+dataList.get(position).get("news_id").toString(), Toast.LENGTH_SHORT).show();
                String address = dataList.get(position).get("news_address").toString();
                activity.start_webactivity(address);
            }
        });
         return view;
    }

    private void loadsavelist() {
        if(ifile.read()!=""){
            savelist = GsonUtil.getNewsListFromJson(ifile.read());
            System.out.println("savelist:"+ifile.read());
            dataList = getadapterdata(savelist);
            adapter.notifyDataSetChanged();
        }
    }


    private void initonclick() {
        news_selector.setOnClickListener(this);
        notice_selector.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.news_selector:
                news_selector.setBackgroundResource(R.drawable.blue_selected);
                notice_selector.setBackgroundResource(R.drawable.white_unselected);
                news_text.setTextColor(Color.WHITE);
                notice_text.setTextColor(Color.BLACK);
                type=0;
                flag=0;
               // new GetDataTask().execute(URLSTRING);*/
                if(list!=null){
                    dataList=getadapterdata(list);
                }
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case R.id.notice_selector:
                notice_selector.setBackgroundResource(R.drawable.blue_selected);
                news_selector.setBackgroundResource(R.drawable.white_unselected);
                notice_text.setTextColor(Color.WHITE);
                news_text.setTextColor(Color.BLACK);
                type=1;
                flag=1;
                if(list!=null){
                    dataList=getadapterdata(list);
                }
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
               // new GetDataTask().execute(URLSTRING);*/
                break;

        }
    }

    private class GetDataTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result =null;
            try
            {
                System.out.println("parmas result: "+params[0]);
                //�ֵ�һ�μ��ػ���ˢ��
                if(listsize==0){
                    result= GsonUtil.getjson(params[0],-1);
                }else {
                    result= GsonUtil.getjson(params[0],listsize);
                }
                System.out.println("json result: "+result);
            } catch (Exception e) {
                System.out.println(e);
            }
            if(result!=null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null)
            {
                is_refresh_success=0;
                Toast.makeText(FirstFragment.this.getActivity(),"�������", Toast.LENGTH_SHORT).show();  //�������
            }else{
                is_refresh_success=1;
                if(listsize==0){
                    ifile.clean();
                    ifile.write(result);
                    list=GsonUtil.getNewsListFromJson(result);
                    listsize=list.size();
                    dataList=getadapterdata(list);
                }else{
                    updatelist=GsonUtil.getNewsListFromJson(result);
                    loadmoredate(updatelist);
                }
                System.out.println("finish   " + dataList.size());
                adapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }
    }

    public LinkedList<Map<String, Object>> getadapterdata(LinkedList list) {
        LinkedList<Map<String, Object>> newsdata = new LinkedList<Map<String,Object>>();
        LinkedList<Map<String, Object>> noticedata = new LinkedList<Map<String,Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            News news = (News) list.get(i);
            map.put("news_id", news.getNews_id());
            map.put("news_title", news.getNews_content());
            map.put("news_time", news.getNews_time());
            map.put("news_address",news.getNews_address());
            map.put("news_img",news.getNews_img());
            map.put("news_type",news.getNews_type());
            if(news.getNews_type()==0)
            {
                newsdata.add(map);
            }
            else{
                noticedata.add(map);
            }
        }
        if(flag==0){
            return newsdata;
        }
        else{
            return  noticedata;
        }

    }

    //ˢ�°�ť���ؼ���������
    public void loadmoredate(LinkedList updatelist){
        listsize=listsize+updatelist.size();
        for (int i = 0; i <updatelist.size() ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            News news = (News) updatelist.get(i);
            map.put("news_id", news.getNews_id());
            map.put("news_title", news.getNews_content());
            map.put("news_time", news.getNews_time());
            map.put("news_address",news.getNews_address());
            map.put("news_img",news.getNews_img());
            map.put("news_type",news.getNews_type());
            if(flag==0&&news.getNews_type()==0){
                dataList.addFirst(map);
                list.addFirst(news);
            }
            else if(flag==1&&news.getNews_type()==1)
            {
                dataList.addFirst(map);
                list.addFirst(news);
            }
            else {
                listsize--;
            }
        }
    }

    public void refreshlist(){
        new GetDataTask().execute(URLSTRING);

        if(is_refresh_success==1){
            Handler delay = new Handler();
            delay.postDelayed(new NewsUpdateToast(),2700);
        }
    }

    /** �½�һ����̳�BaseAdapter��ʵ����ͼ�����ݵİ�
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//�õ�һ��LayoutInfalter�����������벼��
        private RequestQueue queue;
        private ImageLoader imageLoader;

        /**���캯��*/
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new BitmapCache());
        }

        @Override
        public int getCount() {
            return dataList.size();//��������ĳ���
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**������ϸ���͸÷���*/
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //�۲�convertView��ListView�������
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                if(type==0){
                    convertView = mInflater.inflate(R.layout.news_listitem_1,null);
                    holder = new ViewHolder();
                    /**�õ������ؼ��Ķ���*/
                    holder.title = (TextView) convertView.findViewById(R.id.news_title);
                    holder.time = (TextView) convertView.findViewById(R.id.news_time);
                    holder.news_img = (NetworkImageView) convertView.findViewById(R.id.news_img);
                    holder.news_imageview = (ImageView) convertView.findViewById(R.id.news_imgview);
                    convertView.setTag(holder);//��ViewHolder����
                }
                else{
                    convertView = mInflater.inflate(R.layout.news_listitem_2,null);
                    holder = new ViewHolder();
                    /**�õ������ؼ��Ķ���*/
                    holder.title = (TextView) convertView.findViewById(R.id.notice_title);
                    holder.time = (TextView) convertView.findViewById(R.id.notice_time);
                    convertView.setTag(holder);//��ViewHolder����
                }
            }
            else{
                holder = (ViewHolder)convertView.getTag();//ȡ��ViewHolder����
            }
            /**����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е�����*/
            holder.title.setText(dataList.get(position).get("news_title").toString());
            holder.time.setText(dataList.get(position).get("news_time").toString());
            if(type==0){
                final String imgUrl = dataList.get(position).get("news_img").toString();   //list[position];

                Picasso.with(getActivity()).load(imgUrl).placeholder(R.drawable.loading).into(holder.news_imageview);

                /*if (imgUrl != null && !imgUrl.equals("")) {
                    holder.news_img.setDefaultImageResId(R.drawable.loading);
                    holder.news_img.setErrorImageResId(R.drawable.error);
                    holder.news_img.setImageUrl(imgUrl, imageLoader);
                }*/
            }
            return convertView;
        }

    }
    /**��ſؼ�*/
    public final class ViewHolder{
        public TextView title;
        public TextView time;
        public NetworkImageView news_img;
        public ImageView news_imageview;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    private class NewsUpdateToast implements Runnable {
        @Override
        public void run() {

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String time =timeFormat.format(new Date());
            Toast.makeText(FirstFragment.this.getActivity(),"����ʱ�䣺"+time, Toast.LENGTH_SHORT).show();
        }
    }
}
