package com.solu.daewha.bluetoothclient;


import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by zino on 2017-11-08.
 */

public class ControlActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    String TAG=this.getClass().getName();
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    BluetoothSocket socket;
    Handler handler;
    DataThread dataThread;
    Thread ledThread;

    EyeFragment eyeFragment;
    MsgFragment msgFragment;
    LedFragment ledFragment;
    SoundFragment soundFragment;
    String msg="";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        //프레그먼트 참조
        eyeFragment=(EyeFragment) myPagerAdapter.getItem(0);
        msgFragment=(MsgFragment) myPagerAdapter.getItem(1);
        ledFragment=(LedFragment) myPagerAdapter.getItem(2);
        soundFragment=(SoundFragment) myPagerAdapter.getItem(3);

        socket = MainActivity.socket;

        handler = new Handler(){
            public void handleMessage(Message message) {
                msg=message.getData().getString("msg");

                if(msg.equals("danger")){
                    eyeFragment.showStatus("위험" ,R.drawable.danger);
                }else{
                    eyeFragment.showStatus("안전", R.drawable.safe);
                }
            }
        };

        ledThread = new Thread(){
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msg="safe";

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", msg);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        };
        ledThread.start();

        //데이터 쓰레드 작동시작
        dataThread = new DataThread(this, socket);
        dataThread.start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected is "+position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
