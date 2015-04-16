package com.bear.http;

import android.util.Log;
import com.bear.util.ipaddressUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear on 2015/4/12.
 */
public class shophttp {
    String uri = ipaddressUtil.IP+"/SchoolInfo/servlet/CollectionServlet";

    public boolean collect_shop(String shop_id,int u_id,int f){
        String result = "";
       // System.out.println("collect activity_shop:"+shop_id+" + "+u_id+"+"+f);
        String user_id = Integer.toString(u_id);
        String flag = Integer.toString(f);
        try
        {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            //Represents a collection of HTTP protocol and framework parameters
            HttpParams params = null;
            params = httpclient.getParams();
            //set timeout
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 35000);

            HttpResponse response;
            HttpPost httpost = new HttpPost(uri);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("user_id", user_id));
            nvps.add(new BasicNameValuePair("shop_id", shop_id));
            nvps.add(new BasicNameValuePair("flag",flag));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();

	      /* HTML POST result BODY */
            result = EntityUtils.toString(entity);

            result = result.trim().toLowerCase();

            if (entity != null)
            {
                entity.consumeContent();
            }

            if(result.equals("true"))
            {
                System.out.println("goodjob");
                return true;
            }
            else
            {
                System.out.println("result:"+result);
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("NetError");
            Log.i("Error", "Exception" + e.getMessage());
            return false;
        }
    }


}
