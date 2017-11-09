package com.solu.daewha.bluetoothclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zino on 2017-11-08.
 */

public class EyeFragment extends Fragment{
    String TAG=this.getClass().getName();
    ControlActivity controlActivity;
    DataThread dataThread;

    TextView txt_msg;
    ImageView status;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_eye,null);

        controlActivity = (ControlActivity) getActivity();
        dataThread = controlActivity.dataThread;

        txt_msg = (TextView)view.findViewById(R.id.txt_msg);
        status = (ImageView)view.findViewById(R.id.status);

        return view;
    }

    //경보 이미지로 바꾸기
    public void showStatus(String txt, int r){
        txt_msg.setText(txt);
        status.setImageResource(r);
    }
}
