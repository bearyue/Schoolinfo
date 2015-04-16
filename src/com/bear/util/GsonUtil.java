package com.bear.util;

import java.net.URI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import com.bear.model.Collection;
import com.bear.model.News;
import com.bear.model.Shop;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.bear.model.Message;
import com.google.gson.Gson;

public class GsonUtil {
	public static String getjson(String url,int f)
	{
		String beanlisttojson = null;
		HttpClient httpClient= new DefaultHttpClient();
        String flag ;
        flag = Integer.toString(f);

        Context mcontext=null;
		HttpPost request;
		try {
            //Represents a collection of HTTP protocol and framework parameters
            HttpParams params = null;
            params = httpClient.getParams();
            //set timeout
            HttpConnectionParams.setConnectionTimeout(params, 3500);
            HttpConnectionParams.setSoTimeout(params, 40000);
		//	request= new HttpPost(new URI(url));
            HttpResponse response;
            HttpPost httpost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("flag", flag));
            System.out.println(flag);
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            response= httpClient.execute(httpost);
            System.out.println("getjsonp post");
			if(response.getStatusLine().getStatusCode()==200)
			{
				HttpEntity entity=response.getEntity();
				if (entity!=null) {
					beanlisttojson=EntityUtils.toString(entity,"utf-8");
					System.out.println("getdata="+beanlisttojson);
					return beanlisttojson;
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("NetError");
            Log.i("Error", "Exception" + e.getMessage());
            ipaddressUtil.errormessage=e;
		}
		System.out.println("json=null ");
		return beanlisttojson;
	}

	public static String getjson(String url,int f,int f2)
	{
		String beanlisttojson = null;
		HttpClient httpClient= new DefaultHttpClient();
        String flag ;
        String flag2 ;
        flag = Integer.toString(f);
        flag2 = Integer.toString(f2);

        Context mcontext=null;
		HttpPost request;
		try {
            //Represents a collection of HTTP protocol and framework parameters
            HttpParams params = null;
            params = httpClient.getParams();
            //set timeout
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 35000);
		//	request= new HttpPost(new URI(url));
            HttpResponse response;
            HttpPost httpost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("flag", flag));
            nvps.add(new BasicNameValuePair("flag2", flag2));
            System.out.println(flag);
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            response= httpClient.execute(httpost);
            System.out.println("getjsonp2 post");
			if(response.getStatusLine().getStatusCode()==200)
			{
				HttpEntity entity=response.getEntity();
				if (entity!=null) {
					beanlisttojson=EntityUtils.toString(entity,"utf-8");
					System.out.println("getdata2="+beanlisttojson);
					return beanlisttojson;
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("NetError");
            Log.i("Error", "Exception" + e.getMessage());
            ipaddressUtil.errormessage=e;
		}
		System.out.println("json=null 2");
		return beanlisttojson;
	}

	public static List<Message> getListFromJson(String json) {
        //运行时记得将GSON的jar包设置export，要不然程序崩溃，下面这句出错；
        //具体设置为：工程properties->java build path->order and export ->勾选需要的包
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Message>>() {
        }.getType();
        Gson gson = new Gson();
        List<Message> list = gson.fromJson(json,type);
        return list;
    }

    public static List<Shop> getShopListFromJson(String json) {
        //运行时记得将GSON的jar包设置export，要不然程序崩溃，下面这句出错；
        //具体设置为：工程properties->java build path->order and export ->勾选需要的包
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Shop>>() {
        }.getType();
        Gson gson = new Gson();
        List<Shop> list = gson.fromJson(json,type);
        return list;
    }
    public static List<Collection> getCollectionListFromJson(String json) {
        //运行时记得将GSON的jar包设置export，要不然程序崩溃，下面这句出错；
        //具体设置为：工程properties->java build path->order and export ->勾选需要的包
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Collection>>() {
        }.getType();
        Gson gson = new Gson();
        List<Collection> list = gson.fromJson(json,type);
        return list;
    }

    public static LinkedList<News> getNewsListFromJson(String json) {
        //运行时记得将GSON的jar包设置export，要不然程序崩溃，下面这句出错；
        //具体设置为：工程properties->java build path->order and export ->勾选需要的包
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<LinkedList<News>>() {
        }.getType();
        Gson gson = new Gson();
        LinkedList<News> list = gson.fromJson(json,type);
        return list;
    }

}
