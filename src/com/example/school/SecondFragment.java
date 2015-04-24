package com.example.school;

import java.util.*;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.util.Log;
import android.widget.*;
import com.bear.model.Message;
import com.bear.http.messagehttp;
import com.bear.util.InternalFileUtil;
import com.bear.util.SharedPreferencesUtil;
import com.bear.util.ipaddressUtil;
import com.bear.util.GsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bear on 2015/3/25.
 */
public class SecondFragment extends Fragment {

    private static final String URLSTRING = ipaddressUtil.IP + "/SchoolInfo/servlet/MessageServlet";

    public PullToRefreshListView mPullRefreshListView;
    public ImageButton goodbutton;
    LinkedList<Map<String, Object>> dataList = new LinkedList<Map<String, Object>>();
    MyAdapter adapter = null;
    SharedPreferencesUtil userinfo;
    String updatemessage;
    private MainActivity activity;

    int mposition = 0;
    int top10=0;

    List<Message> list;
    List<Message> savelist;

    private String filename="messagetext";
    InternalFileUtil ifile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_second, container, false);
       // new GetDataTask().execute(URLSTRING);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(Mode.BOTH);
        userinfo=new SharedPreferencesUtil(this.getActivity());
        adapter = new MyAdapter(SecondFragment.this.getActivity());
        System.out.println("set adapter");
        mPullRefreshListView.setAdapter(adapter);
        ifile = new InternalFileUtil(this.getActivity(),filename);
        ifile.create();
        loadsavelist();

        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //����ʱ��
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel  
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                //�޸�����ˢ��label
                refreshView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pull_down_load));
                // Do work to refresh the list here.
                mposition = 0;
                new GetDataTask().execute(URLSTRING);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //����ʱ��
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                //�޸�����ˢ��label
                refreshView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_up_load));
                // Do work to refresh the list here.
                // new GetDataTask().execute();
                if(top10==1){

                }else{
                    mposition = mposition + 5;
                    new GetDataTask().execute(URLSTRING);
                }

            }

        });
        //  mPullRefreshListView.setRefreshing();
        System.out.println("return view");
        return view;
    }

    private void loadsavelist() {
        if(ifile.read()!=""){
            savelist = GsonUtil.getListFromJson(ifile.read());
            System.out.println("savelist:"+ifile.read());
            dataList = getadapterdata(savelist);
            adapter.notifyDataSetChanged();
        }
    }

    public void setvalue(String um) {
        updatemessage = um;
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {

        //��̨������  
        @Override
        protected String doInBackground(String... params) {
            // Simulates a background job.  
            System.out.println("show");
            String result = null;
            try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                result = GsonUtil.getjson(params[0], mposition,top10);
                System.out.println("secondfragment getresult: " + result);
            } catch (Exception e) {
                System.out.println(e);
            }
            return result;
        }

        //�����Ƕ�ˢ�µ���Ӧ����������addFirst������addLast()�������¼ӵ����ݼӵ�LISTView��  
        //����AsyncTask��ԭ��onPostExecute���result��ֵ����doInBackground()�ķ���ֵ  
        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast.makeText(SecondFragment.this.getActivity(), "�������", Toast.LENGTH_SHORT).show();  //�������
                mPullRefreshListView.onRefreshComplete();
            } else {
                list = GsonUtil.getListFromJson(result);
                System.out.println("change");
                if (mposition == 0) {
                    dataList = getadapterdata(list);
                } else {
                    loadmoredate(list);
                }
                ifile.clean();
                ifile.write(result);
                System.out.println("finish   " + dataList.size()+"+"+ifile.read());

                //֪ͨ�������ݼ��Ѿ��ı䣬�������֪ͨ����ô������ˢ��mListItems�ļ���
                adapter.notifyDataSetChanged();
                // Call onRefreshComplete when the list has been refreshed.
                mPullRefreshListView.onRefreshComplete();
            }
            super.onPostExecute(result);
        }
    }


    public LinkedList<Map<String, Object>> getadapterdata(List list) {
        LinkedList<Map<String, Object>> data = new LinkedList<Map<String, Object>>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Message Message = (Message) list.get(i);
            map.put("goodtimes", Message.getMessage_gtimes());
            map.put("Message_content", Message.getMessage_content());
            map.put("time", Message.getMessage_time());
            map.put("message_id", Message.getMessage_id());
            map.put("user_id", Message.getUser_id());
            map.put("button_image", 0);//��־λ
            data.add(map);
        }
        return data;
    }

    //�������ظ���
    public void loadmoredate(List list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Message Message = (Message) list.get(i);
            map.put("goodtimes", Message.getMessage_gtimes());
            map.put("Message_content", Message.getMessage_content());
            map.put("time", Message.getMessage_time());
            map.put("message_id", Message.getMessage_id());
            map.put("user_id", Message.getUser_id());
            map.put("button_image", 0);//��־λ
            dataList.addLast(map);
        }
    }


    /**
     * �½�һ����̳�BaseAdapter��ʵ����ͼ�����ݵİ�
     */
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//�õ�һ��LayoutInfalter�����������벼��


        /**
         * ���캯��
         */
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
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

        boolean buttonstate = false;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            //�۲�convertView��ListView�������
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.messagelistitem, null);
                holder = new ViewHolder();
                /**�õ������ؼ��Ķ���*/
                holder.content = (TextView) convertView.findViewById(R.id.message_content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.goodtimes = (TextView) convertView.findViewById(R.id.goodtimes);
                holder.goodbutton = (ImageButton) convertView.findViewById(R.id.good_button);
                convertView.setTag(holder);//��ViewHolder����
            } else {
                holder = (ViewHolder) convertView.getTag();//ȡ��ViewHolder����
            }
            /**����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е�����*/
            System.out.println("set text");
            Log.v("MyListViewBase", "�����˰�ť");
            holder.goodtimes.setText(dataList.get(position).get("goodtimes").toString());
            holder.content.setText((String) dataList.get(position).get("Message_content"));
            holder.time.setText((String) dataList.get(position).get("time"));
            if (Integer.parseInt(dataList.get(position).get("button_image").toString()) == 1) {
                holder.goodbutton.setBackgroundResource(R.drawable.thumbs_up2);
            } else {
                holder.goodbutton.setBackgroundResource(R.drawable.thumbs_up);
            }

            //�Ƿ�ɵ��

            /**ΪButton��ӵ���¼�*/
            holder.goodbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!userinfo.getLoginState()){
                        Toast.makeText(SecondFragment.this.getActivity(), "���¼�����", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String u_id = dataList.get(position).get("user_id").toString();
                        String m_id = dataList.get(position).get("message_id").toString();
                        Log.v("MyListViewBase", "�����˰�ť��message_id Ϊ��" + u_id + "  + " + m_id);//��ӡButton�ĵ����Ϣ

                        if (Integer.parseInt(dataList.get(position).get("button_image").toString()) == 0) {//���·���������
                            new UpDateTask().execute(Integer.toString(userinfo.getId()), m_id);
                            int up1 = Integer.parseInt(dataList.get(position).get("goodtimes").toString());
                            up1 = up1 + 1;
                            holder.goodbutton.setBackgroundResource(R.drawable.thumbs_up2);
                            Map<String, Object> map = dataList.get(position);
                            map.put("goodtimes", up1);
                            map.put("button_image", 1);
                            dataList.set(position, map);
                            holder.goodtimes.setText(Integer.toString(up1));
                        } else {
                        }
                        buttonstate = true;
                    }
                }
            });
            System.out.println("rolling ........");
            return convertView;
        }

    }

    /**
     * ��ſؼ�
     */
    public final class ViewHolder {
        public TextView content;
        public TextView time;
        public TextView goodtimes;
        public ImageButton goodbutton;

    }

    private class UpDateTask extends AsyncTask<String, Void, Boolean> {
        messagehttp gt = new messagehttp();

        @Override
        protected Boolean doInBackground(String... params) {
            Log.v("background", "doinback");
            try {
                gt.updategoodtimes(params[0], params[1]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    //����ACTIVTTY,���ڻ�ȡactivityʵ��
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    //��mainactivity���õ���
    public void refreshlist() {
        mposition = 0;
       // mPullRefreshListView.getRefreshableView().setSelection(0);
      //  new GetDataTask().execute(URLSTRING);
        top10=0;
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        mPullRefreshListView.getRefreshableView().smoothScrollToPosition(0);
        mPullRefreshListView.setRefreshing();
        mPullRefreshListView.setMode(Mode.BOTH);
        System.out.println("setrefreshing....");
    }

    public void  gettop10(){
        mPullRefreshListView.getRefreshableView().smoothScrollToPosition(0);
        mposition=0;
        if(top10==0){
            top10=1;
            mPullRefreshListView.setMode(Mode.PULL_FROM_START);
            //new GetDataTask().execute(URLSTRING);
            //mPullRefreshListView.setRefreshing();
            mPullRefreshListView.getRefreshableView().setSelection(0);
            mPullRefreshListView.setRefreshing();
        }else{
            top10=0;
            //new GetDataTask().execute(URLSTRING);
            mPullRefreshListView.setRefreshing();
            mPullRefreshListView.setMode(Mode.BOTH);
        }

    }

}
