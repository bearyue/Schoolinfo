package com.bear.util;

import android.content.Context;
import com.example.school.SecondFragment;

import java.io.*;

/**
 * Created by bear on 2015/4/21.
 */
public class InternalFileUtil {
    private Context context;
    private String fileName;

    /**
     * ���캯��
     * @param context
     * @param fileName
     */
    public InternalFileUtil(Context context, String fileName){
        this.context = context;
        this.fileName = fileName;
    }

    /**
     * �ж��ļ��Ƿ����
     * @return
     */
    public boolean isExist(){
        String [] fileNameArray = context.fileList();
        for(int i = 0; i< fileNameArray.length; i++){
            if(fileNameArray[i].equals(fileName)){
                return true;
            }
        }
        return false;
    }

    /**
     * �����ļ�
     * @return
     */
    public boolean create(){
        boolean result = false;
        if(!isExist()){
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(fos != null){
                result = true;
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * д��
     * @param str
     * @return
     */
    public boolean write(String str){
        boolean result = true;
        FileOutputStream fos = null;

        if(isExist()){
            try {
                fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = false;
            }

            byte bt [] = (str+"\n").getBytes();

            if(fos != null){
                try {
                    fos.write(bt);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                }
                try{
                    fos.close();
                }catch (Exception e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * ��ȡһ��
     * @return
     */
    public String readLine(){

        String str = "";
        FileInputStream fis = null;

        if(isExist()){
            try {
                fis = context.openFileInput(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            try {
                str = br.readLine();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * ��ȡȫ��
     * @return
     */
    public String read(){
        String str = "";
        String line = "";
        FileInputStream fis = null;

        if(isExist()){
            try {
                fis = context.openFileInput(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            try {
                while((line = br.readLine()) != null){
                    str += line;
                }
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return str;
    }

    /**
     * ����ļ�
     */
    public void clean(){
        delete();
        create();
    }

    /**
     *  ɾ���ļ�
     */
    public void delete(){
        context.deleteFile(fileName);
    }
}
