package com.solu.daewha.bluetoothclient;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by zino on 2017-11-04.
 */

public class DataThread extends Thread{
    String TAG=this.getClass().getName();

    ChattingActivity chattingActivity;
    BluetoothSocket socket;
    BufferedReader buffr;
    BufferedWriter buffw;
    boolean flag=true;

    public DataThread(ChattingActivity chattingActivity,BluetoothSocket socket) {
        this.chattingActivity=chattingActivity;
        this.socket = socket;
        try {
            buffr = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //듣기
    public void listen(){
        String msg=null;
        try {
            msg=buffr.readLine();

            Log.d(TAG, msg);

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            message.setData(bundle);
            chattingActivity.handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //말하기
    public void send(String msg){
        try {
            buffw.write(msg);
            buffw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while(flag){
            listen();
        }
    }

}