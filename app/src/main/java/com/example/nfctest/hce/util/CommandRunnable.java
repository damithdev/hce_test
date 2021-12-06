package com.example.nfctest.hce.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CommandRunnable implements Runnable{

    private static final String TAG = "CommandRunnable";
    private Context context;
    private String desc;
    private byte[] command;
    private byte[] response;
    public CommandRunnable(Context _context,String _desc,byte[] command, byte[] response){
        this.context = _context;
        this.desc = _desc;
        this.command = command;
        this.response = response;
    }
    @Override
    public void run() {
        Log.d(TAG, desc+ ": Received:");
        Log.d(TAG, "Received:"+ ByteUtil.bytes2HexStr(command));
        Log.d(TAG,"Response:"+ByteUtil.bytes2HexStr(response));
        sendMessage(desc);
    }

    private void sendMessage(String msg) {
        // The string "my-message" will be used to filer the intent
        Intent intent = new Intent("my-message");
        // Adding some data
        intent.putExtra("log-msg", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
