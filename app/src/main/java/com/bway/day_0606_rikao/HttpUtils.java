package com.bway.day_0606_rikao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {


    private static final String TAG = "HttpUtils-----";
    //单例模式
    private static HttpUtils httpUtils = new HttpUtils();

    private static final int SUCCESS = 0;
    private static final int ERROR = 1;
    private HttpListener httpListener;


    //设置handler事件处理机制


    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case SUCCESS:
                    String json = (String) msg.obj;
                    httpListener.getSuccess(json);
                    break;

                case ERROR:
                    String error = (String) msg.obj;
                    httpListener.getError(error);
                    break;
            }


        }
    };


    //构造方法私有化
    private HttpUtils() {
    }

//判断

    public static HttpUtils getHttpUtils() {

        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;


    }


    //封装get方法
    public void get(final String url) {

        //开启线程

        new Thread() {
            @Override
            public void run() {

                try {
                    URL u = new URL(url);

                    HttpURLConnection con = (HttpURLConnection) u.openConnection();
                    con.setConnectTimeout(5000);

                    if (con.getResponseCode() == 200) {

                        InputStream inputStream = con.getInputStream();
                        String json = ConUtils.in(inputStream);

                        //发送消息
                        Message me = new Message();
                        me.what = SUCCESS;
                        me.obj = json;
                        //发送handler请求
                        handler.sendMessage(me);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    //发送消息
                    Message me = new Message();
                    me.what = ERROR;
                    me.obj = e.getMessage();
                    //发送handler请求
                    handler.sendMessage(me);
                }

            }
        }.start();


    }


    //接口回调
    public interface HttpListener {

        void getSuccess(String json);

        void getError(String error);
    }


//设置外部访问的方法

    public void setHttpListener(HttpListener httpListener) {

        this.httpListener = httpListener;
    }

    //判断是否有网的方法
    public boolean hasNet(Context context) {

        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        return networkInfo != null;

    }


}
