package com.comp90018.H1Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendPostJson extends Thread {

    private String url = "";
    private String jsonSring;
    private HttpURLConnection connection = null;
    private OutputStream outStream = null;
    private BufferedReader bufferedReader = null;
    private String res = "";
    private Handler handler = null;
    private Message msg = null;

    public SendPostJson(String url, String jsonString, Handler handler){
        this.handler = handler;
        this.url = url;
        this.jsonSring = jsonString;

    }

    @Override
    public void run(){
        Log.d("start post:", jsonSring);
        if(url.equals("") || url == null) {
            Log.d("No url:", jsonSring);
            return;
        }
        res = "";
        try{
            URL url_path = new URL(url.trim());
            connection = (HttpURLConnection) url_path.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            //获取连接
            connection.connect();
            outStream = connection.getOutputStream();
            outStream.write(jsonSring.getBytes());
            outStream.flush();
            outStream.close();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res += line;
            }
            Log.d("nework", res);
            bufferedReader.close();
            msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("result", res);
            msg.setData(bundle);
            handler.sendMessage(msg);

        }catch (Exception e){
            Log.d("nework", "quest problem");
            e.printStackTrace();
        }finally {
            try{
                if(outStream != null){
                    outStream.close();
                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
                if(connection != null){
                    connection.disconnect();
                }
            }catch (Exception e){
                Log.d("nework", "close problem");
                e.printStackTrace();
            }
        }
    }


    /**
     * start
     */
    public void request(){
        this.start();
    }

}