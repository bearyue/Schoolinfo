

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
            Toast.makeText(context, "������ʱ�������µ�¼!", Toast.LENGTH_SHORT).show();
            
        } else if (e instanceof SocketException) {
            Toast.makeText(context, "������û��Ӧ", Toast.LENGTH_SHORT).show();

        } else if (e instanceof IOException) {
            Toast.makeText(context, "�����������", Toast.LENGTH_SHORT).show();

        } else if (e instanceof NullPointerException) {
        	
        }
        else {
            Toast.makeText(context, "����Ԥ���쳣!",
                    Toast.LENGTH_SHORT).show();
           
        }
    }
}
