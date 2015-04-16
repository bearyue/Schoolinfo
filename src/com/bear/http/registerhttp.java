package com.bear.http;

import android.util.Log;
import com.bear.util.ipaddressUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear on 2015/3/27.
 */
public class registerhttp {
    public boolean register(String strname, String strpassword)
    {

        //String uriAPI = IPAddress.IP+"/LBS_FOR_SISE/register.jsp";
        String uri = ipaddressUtil.IP+"/SchoolInfo/servlet/RegisterServlet";
        String result = "";
        System.out.println(strname+"3"+strpassword);

        try
        {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            //Represents a collection of HTTP protocol and framework parameters
            HttpParams params = null;
            params = httpclient.getParams();
            //set timeout
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpResponse response;
            HttpPost httpost = new HttpPost(uri);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("rname", strname));
            nvps.add(new BasicNameValuePair("rpassword", strpassword));
            System.out.println(strpassword);
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
                //System.out.println("gggg"+result);
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("NetError");
            Log.i("Error", "Exception" + e.getMessage());
            ipaddressUtil.errormessage=e;
            return false;
        }
    }


}
