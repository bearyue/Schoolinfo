

package com.bear.util;



import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class NotificationsUtil {

    public static void ToastReasonForFailure(Context context, Exception e) {
        if(e==null)
        {

        }
        else if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络延时，请重新登录!", Toast.LENGTH_SHORT).show();
            
        } else if (e instanceof SocketException) {
            Toast.makeText(context, "服务器没响应", Toast.LENGTH_SHORT).show();

        } else if (e instanceof IOException) {
            Toast.makeText(context, "请检查你的网络", Toast.LENGTH_SHORT).show();

        } else if (e instanceof NullPointerException) {
        	
        }
        else {
            Toast.makeText(context, "不可预期异常!",
                    Toast.LENGTH_SHORT).show();
           
        }
    }
}
