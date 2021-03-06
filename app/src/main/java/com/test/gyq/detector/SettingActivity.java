package com.test.gyq.detector;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

/**
 * Created by alphabeting on 2016/10/1.
 */

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private Toolbar mToolbar;

    private SelectWindow selectWindow;

    private EditText wifi_name,wifi_pwd,wifi_ip,wifi_port,local_ip,local_port;

    static String patterns="",wifiName="",wifiIp="",wifiPwd="",wifiPort="",
            localIp="",localPort="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final TableLayout pattern = (TableLayout) this.findViewById(R.id.pattern);
        final TableLayout about = (TableLayout) this.findViewById(R.id.about);
        wifi_name = (EditText)findViewById(R.id.wifi_name);
        wifi_pwd = (EditText)findViewById(R.id.wifi_pwd);
        wifi_ip = (EditText)findViewById(R.id.wifi_ip);
        wifi_port = (EditText)findViewById(R.id.wifi_port);
        local_ip = (EditText)findViewById(R.id.local_ip);
        local_port = (EditText)findViewById(R.id.local_port);
        wifi_name.setText(pref.getString("wifi_name",""));
        wifi_pwd.setText(pref.getString("wifi_pwd",""));
        wifi_ip.setText(pref.getString("wifi_ip",""));
        wifi_port.setText(pref.getString("wifi_port",""));
        local_ip.setText(pref.getString("local_ip",""));
        local_port.setText(pref.getString("local_port",""));
        patterns = pref.getString("patterns","");
        pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                selectWindow = new SelectWindow(SettingActivity.this,itemsOnClick);
                selectWindow.showAtLocation(SettingActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        about.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final AlertDialog.Builder aboutDialog = new AlertDialog.Builder(SettingActivity.this);
                aboutDialog.setIcon(R.drawable.ic_launcher);
                aboutDialog.setTitle(R.string.app_name);
                aboutDialog.setMessage("版本:1.0\n" +
                                          "      Copyright© 2018");
                aboutDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                    }
                });
                aboutDialog.show();
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        wifi_name = (EditText)findViewById(R.id.wifi_name);
        wifi_pwd = (EditText)findViewById(R.id.wifi_pwd);
        wifi_ip = (EditText)findViewById(R.id.wifi_ip);
        wifi_port = (EditText)findViewById(R.id.wifi_port);
        local_ip = (EditText)findViewById(R.id.local_ip);
        local_port = (EditText)findViewById(R.id.local_port);
        wifiName = wifi_name.getText().toString();
        wifiPwd = wifi_pwd.getText().toString();
        wifiIp = wifi_ip.getText().toString();
        wifiPort = wifi_port.getText().toString();
        localIp = local_ip.getText().toString();
        localPort = local_port.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("wifi_name",wifiName);
        intent.putExtra("wifi_pwd",wifiPwd);
        intent.putExtra("wifi_ip",wifiIp);
        intent.putExtra("wifi_port",wifiPort);
        intent.putExtra("local_ip",localIp);
        intent.putExtra("local_port",localPort);
        intent.putExtra("patterns",patterns);
        setResult(RESULT_OK,intent);

        editor = pref.edit();
        editor.putString("wifi_name",wifiName);
        editor.putString("wifi_pwd",wifiPwd);
        editor.putString("wifi_ip",wifiIp);
        editor.putString("wifi_port",wifiPort);
        editor.putString("local_ip",localIp);
        editor.putString("local_port",localPort);
        editor.putString("patterns",patterns);
        editor.commit();
        finish();
    }

    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(R.string.wifi_setting);
    }

    private  View.OnClickListener itemsOnClick = new View.OnClickListener(){
        public  void  onClick(View v){
            selectWindow.dismiss();
            switch(v.getId()){
                case R.id.UDP:
                    patterns = "UDP";
                    editor = pref.edit();
                    editor.putString("patterns",patterns);
                    editor.commit();
                    break;
                case R.id.TCP:
                    patterns = "TCP";
                    editor = pref.edit();
                    editor.putString("patterns",patterns);
                    editor.commit();
                    break;
                case R.id.cancel:
                    selectWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };




}
