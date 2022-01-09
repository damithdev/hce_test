package com.example.nfctest;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.example.nfctest.hce.DemoActions;
import com.example.nfctest.hce.HostCardEmulatorService;
import com.example.nfctest.hce.util.ByteUtil;
import com.example.nfctest.hce.util.StaticEntry;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nfctest.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private TextView textViewLog;
    private TextView textViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        Intent intent = new Intent(MainActivity.this, HostCardEmulatorService.class);
        textViewLog = (TextView) findViewById(R.id.textview_first);
        textViewLog.setText("====COMMAND LOG==== \n");

        textViewStatus = (TextView) findViewById(R.id.textview_service_state);
        textViewStatus.setText("PENDING");

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyServiceRunning(HostCardEmulatorService.class)){
                    stopNFCService(intent);
                }else{
                    startNFCService(intent);
                }
            }
        });
        startNFCService(intent);

        DemoActions actions = new DemoActions();
        actions.generateICCKeyPair();
        parseIssuerPrivateKey();

    }

    public void parseIssuerPrivateKey(){
        try {
            InputStream inputStream = getAssets().open("K507221199");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String data = ByteUtil.bytes2HexStr(buffer);
            Log.d("Issuer Private Key:",data);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private String getAppSignature(Context context) {

        String hash = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            File file = new File(info.applicationInfo.sourceDir);
            MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
            hash = getFileChecksum(shaDigest, file);
            //hash = calculateMD5(file);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return hash;

    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    private void startNFCService(Intent intent){
        startService(intent);
        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_SHORT).show();
        binding.fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        textViewStatus.setText("Service Running");
        textViewStatus.setBackgroundColor(Color.GREEN);
    }

    private void stopNFCService(Intent intent){
        stopService(intent);
        Toast.makeText(getApplicationContext(),"Service Stopped",Toast.LENGTH_SHORT).show();
        binding.fab.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        textViewStatus.setText("Service Stopped");
        textViewStatus.setBackgroundColor(Color.RED);

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("my-message"));
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String log = intent.getStringExtra("log-msg");
            Log.d(TAG,log);

            if(textViewLog != null)textViewLog.append("\n \n"+log);
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
    }
}