package com.bear.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bear on 2015/4/2.
 */
public class SharedPreferencesUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public final static String USERINFO = "UserInfo";

    public SharedPreferencesUtil(Context context) {
        sp = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    // 用户的密码
    public void setPasswd(String passwd) {
        editor.putString("password", passwd);
        editor.commit();
    }

    public String getPasswd() {
        return sp.getString("password", "");
    }

    // 用户的id
    public void setId(int id) {
        editor.putInt("id", id);
        editor.commit();
    }

    public int getId() {
        return sp.getInt("id", 0);
    }

    //登录状态
    public void setLoginState(boolean state)
    {
        editor.putBoolean("loginstate",state);
        editor.commit();
    }

    public  boolean getLoginState()
    {
        return  sp.getBoolean("loginstate",false);
    }

    // 用户名
    public String getName() {
        return sp.getString("username", "");
    }

    public void setName(String name) {
        editor.putString("username", name);
        editor.commit();
    }

    // 用户的邮箱
    public String getEmail() {
        return sp.getString("email", "");
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }


    // 是否第一次运行本应用
    public void setIsFirst(boolean isFirst) {
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public boolean getisFirst() {
        return sp.getBoolean("isFirst", true);
    }
}
