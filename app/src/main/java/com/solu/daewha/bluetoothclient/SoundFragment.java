package com.solu.daewha.bluetoothclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by zino on 2017-11-08.
 */

public class SoundFragment extends Fragment  implements CompoundButton.OnCheckedChangeListener{
    String TAG=this.getClass().getName();
    ControlActivity controlActivity;
    DataThread dataThread;
    Switch sw;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sound,null);
        sw=(Switch)view.findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(this);

        controlActivity = (ControlActivity) getActivity();
        dataThread = controlActivity.dataThread;

        return view;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            Log.d(TAG, "on");
            dataThread.send("sound:on\n");
        }else{
            Log.d(TAG, "off");
            dataThread.send("sound:off\n");
        };
    }

}
