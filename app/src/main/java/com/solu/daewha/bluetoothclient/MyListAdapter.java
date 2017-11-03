package com.solu.daewha.bluetoothclient;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BluetoothDevice> list=new ArrayList<BluetoothDevice>();

    public MyListAdapter(Context context) {
        this.context =context;
        layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view=null;

        if(convertView == null){
            view=layoutInflater.inflate(R.layout.bluetooth_item , null);
        }else{
            view=convertView;
        }

        TextView txt_item=(TextView)view.findViewById(R.id.txt_item);
        txt_item.setText(list.get(i).getName());

        return view;
    }
}
