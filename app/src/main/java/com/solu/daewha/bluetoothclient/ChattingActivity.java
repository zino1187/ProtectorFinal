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

    }

     public void send(View view){
        String msg=edit_input.getText().toString();
        dataThread.send(msg);
        edit_input.setText("");
    }

}
