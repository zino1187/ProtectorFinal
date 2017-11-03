package com.solu.daewha.bluetoothclient;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by zino on 2017-11-04.
 */

public class ChattingActivity extends AppCompatActivity{
    MainActivity mainActivity;
    BluetoothDevice device;
    String UUID="00001101-0000-1000-8000-00805f9b34fb";
    Thread connectThread;
    BluetoothSocket socket;
    Handler handler;
    TextView txt_area;
    EditText edit_input;
    DataThread dataThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatting);
        txt_area=(TextView)findViewById(R.id.txt_area);
        edit_input=(EditText)findViewById(R.id.edit_input);

        Intent intent=this.getIntent();
        device=intent.getParcelableExtra("device");

        handler = new Handler(){
            public void handleMessage(Message message) {
                String msg=message.getData().getString("msg");
                txt_area.append(msg+"\n");

                txt_area.invalidate();
            }
        };

        connect();
    }


    /*---------------------------------------------------------
       접속한다
    ---------------------------------------------------------*/
    public void connect(){

        try {
            socket=device.createInsecureRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectThread = new Thread(){
            @Override
            public void run() {
                try {
                    socket.connect();

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","접속 성공");
                    message.setData(bundle);
                    handler.sendMessage(message);

                    //데이터 주고 받는 쓰레드 작동 시작
                    dataThread = new DataThread(ChattingActivity.this, socket);
                    dataThread.start();

                } catch (IOException e) {
                    e.printStackTrace();

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","접속 실패");
                    message.setData(bundle);
                    handler.sendMessage(message);

                }

            }
        };

        connectThread.start();

    }

    public void send(View view){
        String msg=edit_input.getText().toString();
        dataThread.send(msg);
        edit_input.setText("");
    }

}
