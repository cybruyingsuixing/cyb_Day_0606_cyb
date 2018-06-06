package com.bway.day_0606_rikao;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
//杨运泽    大胖子
public class MainActivity extends AppCompatActivity implements View.OnClickListener{


      //aaaaaaaaaaaaa
    private  String url = "http://120.27.23.105/user/login";

    private EditText main_mobile,pwd;
    private Button btn_login,btn_reg;

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String json = (String) msg.obj;
                    //解析json--Entity
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(json, UserBean.class);
                    if (userBean.getCode().equals("0")){
                        Toast.makeText(MainActivity.this,"成功--", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }else {
                        Toast.makeText(MainActivity.this,"失败--",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:

                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化页面
        initViews();
    }


    //初始化页面
    private void initViews() {

        main_mobile = findViewById(R.id.main_mobile);
        pwd=findViewById(R.id.main_pwd);

        btn_login = findViewById(R.id.btn_login);
        btn_reg=findViewById(R.id.btn_reg);


        btn_login.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }


//点击跳转
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:

     //获取输入框内容
                final String m = main_mobile.getText().toString();
                final String p = pwd.getText().toString();

//开启线程
                new Thread(){
                    @Override
                    public void run() {

                        try {

                            URL u = new URL(url);
                          HttpURLConnection con= (HttpURLConnection) u.openConnection();
                          con.setConnectTimeout(5000);
                          con.setDoOutput(true);
                          con.setDoInput(true);

                            //URLEncoder--编码
                            String params = "mobile=" + URLEncoder.encode(m, "UTF-8") + "&password=" + URLEncoder.encode(p, "UTF-8") + "&source=android";
//将参数转成流
                            OutputStream ouput = con.getOutputStream();
                            //ouput.write();
                            ouput.flush();
                            ouput.close();

                            if(con.getResponseCode()==200){
                                InputStream input = con.getInputStream();
                                String json = ConUtils.in(input);
                                //发送handler请求
                                Message message = handler.obtainMessage();
                                message.what=0;
                                message.obj=json;
                                handler.sendMessage(message);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }.start();
                // Intent it = new Intent(MainActivity.this,LoginActivity.class);
               // startActivity(it);
                break;
            case R.id.btn_reg:
                Intent intent = new Intent(MainActivity.this,RegActivity.class);
                startActivity(intent);
                break;
        }

    }




}
