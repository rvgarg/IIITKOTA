package com.example.iiitkota;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkCheck extends AsyncTask<Boolean, Void, Boolean> {

    Context context;
    public NetworkCheck(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (!aBoolean){
            Toast.makeText(context,"You are not connected to Internet !!!!",Toast.LENGTH_SHORT).show();
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.connection_manager);
            dialog.setTitle("Not Connected ???");
            dialog.findViewById(R.id.btncancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.findViewById(R.id.btnsubt).setOnClickListener(v -> {
                boolean mobile, wifi;
                mobile = ((Switch) dialog.findViewById(R.id.mobileDataState)).isChecked();
                wifi = ((Switch) dialog.findViewById(R.id.wifiState)).isChecked();
                if (wifi) {
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                }
                if (mobile) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
                dialog.dismiss();
            });
            dialog.show();

        }
        super.onPostExecute(aBoolean);
    }

    @Override
    protected Boolean doInBackground(Boolean... voids) {
        if (voids[0] || voids[1]) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);
                urlc.connect();
                boolean url = (urlc.getResponseCode() == 200);
                return url;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }
}
