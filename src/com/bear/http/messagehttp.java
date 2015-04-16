package com.bear.http;

import android.util.Log;
import android.widget.Toast;
import com.bear.util.ipaddressUtil;
import com.example.school.MainActivity;
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
 * Created by bear on 2015/3/30.
 */

public class messagehttp {
    String uri = ipaddressUtil.IP+"/SchoolInfo/servlet/MessageUpdateServlet";
    public void  updategoodtimes(String  user_id,String message_id)
    {
        //String uriAPI = IPAddress.IP+"/LBS_FOR_SISE/register.jsp";

        String result = "";
        System.out.println("update:  "+message_id+" + "+user_id);
        String flag="1";
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
            nvps.add(new BasicNameValuePair("goodtimes_message_id", message_id));
            nvps.add(new BasicNameValuePair("goodtimes_user_id", user_id));
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
            }
            else
            {
                System.out.println("result:"+result);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("NetError");
            Log.i("Error", "Exception" + e.getMessage());
        }
    }

    public boolean insert_new_message(int user_id,String message_content)
    {
        String result = "";
        System.out.println("new message:  "+message_content+" + "+user_id);
        String u_id = Integer.toString(user_id);
        String flag="2";
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
            nvps.add(new BasicNameValuePair("user_id", u_id));
            nvps.add(new BasicNameValuePair("new_message_content", message_content));
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
