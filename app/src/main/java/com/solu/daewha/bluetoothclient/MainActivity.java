package com.solu.daewha.bluetoothclient;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String TAG=this.getClass().getName();
    ListView listView;
    MyListAdapter myListAdapter;

    //블루투스 관련
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    public static final int REQUEST_ACTIVE=1;   //활성화
    public static final int REQUEST_AUTH=2;     //권한허용

    String UUID="00001101-0000-1000-8000-00805f9b34fb";
    Thread connectThread;
    public static BluetoothSocket socket;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        myListAdapter = new MyListAdapter(this);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(this);



        checkSupportBluetooth();
        requestActive();
    }
    /*---------------------------------------------------------
    Show Message dialogue
    ---------------------------------------------------------*/
    public void showAlert(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg).show();
    }


    /*---------------------------------------------------------
    1.이 디바이스가 블루투스를 지원하는지 체크한다
    ---------------------------------------------------------*/
    public void checkSupportBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            showAlert("알림", "이 기기는 블루투스를 지원하지 않습니다");
            this.finish();
        }
    }

    /*---------------------------------------------------------
    2.활성화를 요청한다
    ---------------------------------------------------------*/
    public void requestActive(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ACTIVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ACTIVE){
            if(resultCode == this.RESULT_CANCELED){
                showAlert("안내","앱을 이용하시려면 블루투스를 켜세요");
                return;
            }
        }
    }

    /*---------------------------------------------------------
    4.앱이 블루투스 사용권한을 취득하지 않았으면 요청한다
    ---------------------------------------------------------*/
    public void checkAuth(){
        int accessPermission=ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(accessPermission  == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_AUTH);
        }else{
            scanDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_AUTH:
                if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_DENIED){
                    showAlert("경고","블루투스 사용 권한을 허용해 주세요");
                }break;
        }
    }

    /*---------------------------------------------------------
        5.검색중단 및 상세보기 화면으로 전환한다
     ---------------------------------------------------------*/
    public void goDetail(BluetoothDevice device){
        bluetoothAdapter.cancelDiscovery();

        connect();
    }


    /*---------------------------------------------------------
        3.검색을 시작한다
        ---------------------------------------------------------*/
    public void scanDevice(){
        bluetoothAdapter.startDiscovery();

        BroadcastReceiver broadcastReceiver;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();

                if(action.equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    myListAdapter.list.add(bluetoothDevice);
                    myListAdapter.notifyDataSetChanged();
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter); //리시버 등록

    }

    public void scan(View view){
        checkAuth();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        device=myListAdapter.list.get(i);

        Log.d(TAG, device.getName());

        //만약 연결에 성공하면 액티비티를 전환시킨다
        goDetail(device);
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

                    //데이터 주고 받는 쓰레드 작동 시작
                    Log.d(TAG, "접속성공");

                    Intent intent  = new Intent(MainActivity.this, ControlActivity.class);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "접속실패");

                }
            }
        };
        connectThread.start();
    }
}
