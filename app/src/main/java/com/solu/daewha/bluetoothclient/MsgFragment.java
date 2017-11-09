package com.solu.daewha.bluetoothclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by zino on 2017-11-08.
 */

public class MsgFragment extends Fragment implements View.OnClickListener{
    ControlActivity controlActivity;
    DataThread dataThread;
    EditText edit_input;
    Button bt_send;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_msg,null);

        edit_input = (EditText)view.findViewById(R.id.edit_input);
        bt_send=(Button)view.findViewById(R.id.bt_send);
        bt_send.setOnClickListener(this);

        controlActivity = (ControlActivity) getActivity();
        dataThread = controlActivity.dataThread;

        return view;
    }

    @Override
    public void onClick(View view) {
        String msg=edit_input.getText().toString();
        dataThread.send("msg:"+msg+"\n");
        edit_input.setText("");
    }

}
