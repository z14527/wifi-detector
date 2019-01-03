package com.example.alphabeting.sockets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import static android.app.Activity.RESULT_OK;

public class MotorSettingActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    static boolean dataStore = true;
    private Toolbar mToolbar;
    private MotorSelectWindow motorSelectWindow=null;
    private MotorSpeedSelectWindow motorSpeedSelectWindow=null;
    private MotorDirectionSelectWindow motorDirectionSelectWindow=null;
    private MotorPulseSelectWindow motorPulseSelectWindow=null;
    private LaserChannelSelectWindow laserChannelSelectWindow=null;
    private ValvePositionSelectWindow valvePositionSelectWindow=null;
    static String motor_pattern="";
    static String motor_speed="";
    static String motor_direction="";
    static String motor_pulse="";
    static String laser_channel="";
    static String valve_pos="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motor_setting);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        Button doPusleBtn = (Button)findViewById(R.id.doPulse);
        doPusleBtn.setWidth(width/2);
        Button closePusleBtn = (Button)findViewById(R.id.closePulse);
        closePusleBtn.setWidth(width/2);
        Button openLaserBtn = (Button)findViewById(R.id.openLaser);
        openLaserBtn.setWidth(width/2);
        Button closeLaserBtn = (Button)findViewById(R.id.closeLaser);
        closeLaserBtn.setWidth(width/2);
        Button readADBtn = (Button)findViewById(R.id.readAD);
        readADBtn.setWidth(width/2);
        Button readIDBtn = (Button)findViewById(R.id.readID);
        readIDBtn.setWidth(width/2);
        final TableRow motor_pattern = (TableRow) this.findViewById(R.id.patternMotor);
        motor_pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                motorSelectWindow = new MotorSelectWindow(MotorSettingActivity.this,itemsOnClick);
                motorSelectWindow.showAtLocation(MotorSettingActivity.this.findViewById(R.id.mainMotor), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        final TableRow speed_pattern = (TableRow) this.findViewById(R.id.patternSpeed);
        speed_pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                motorSpeedSelectWindow = new MotorSpeedSelectWindow(MotorSettingActivity.this,itemsOnClick);
                motorSpeedSelectWindow.showAtLocation(MotorSettingActivity.this.findViewById(R.id.mainMotor), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        final TableRow direction_pattern = (TableRow) this.findViewById(R.id.patternDirection);
        direction_pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                motorDirectionSelectWindow = new MotorDirectionSelectWindow(MotorSettingActivity.this,itemsOnClick);
                motorDirectionSelectWindow.showAtLocation(MotorSettingActivity.this.findViewById(R.id.mainMotor), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        final TableRow pulse_pattern = (TableRow) this.findViewById(R.id.patternPulse);
        pulse_pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                motorPulseSelectWindow = new MotorPulseSelectWindow(MotorSettingActivity.this,itemsOnClick);
                motorPulseSelectWindow.showAtLocation(MotorSettingActivity.this.findViewById(R.id.mainMotor), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        final TableRow channel_pattern = (TableRow) this.findViewById(R.id.patternChannel);
        channel_pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                laserChannelSelectWindow = new LaserChannelSelectWindow(MotorSettingActivity.this,itemsOnClick);
                laserChannelSelectWindow.showAtLocation(MotorSettingActivity.this.findViewById(R.id.mainMotor), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        final TableRow valve_pattern = (TableRow) this.findViewById(R.id.patternValve);
        valve_pattern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                valvePositionSelectWindow = new ValvePositionSelectWindow(MotorSettingActivity.this,itemsOnClick);
                valvePositionSelectWindow.showAtLocation(MotorSettingActivity.this.findViewById(R.id.mainMotor), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        initToolbar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                String ip1 = ip_1.getText().toString();
//                String ip2 = ip_2.getText().toString();
//                String ip3 = ip_3.getText().toString();
//                String ip4 = ip_4.getText().toString();
//                String ip = ip1+"."+ip2+"."+ip3+"."+ip4;
//                String ports = port.getText().toString();
//
      //          Intent intent = new Intent();
//                intent.putExtra("ip_adress",ip);
//                intent.putExtra("port_num",ports);
//                intent.putExtra("pattern",patterns);
        //        setResult(RESULT_OK,intent);

        //        editor = pref.edit();
//                editor.putString("ip1",ip1);
//                editor.putString("ip2",ip2);
//                editor.putString("ip3",ip3);
//                editor.putString("ip4",ip4);
//                editor.putString("ports",ports);
//                editor.putString("patterns",patterns);
          //      editor.commit();
            //    finish();
                dataStore = true;

                onBackPressed();
            }
        });

        if(dataStore){
//            String ip11 = pref.getString("ip1","");
//            String ip22 = pref.getString("ip2","");
//            String ip33 = pref.getString("ip3","");
//            String ip44 = pref.getString("ip4","");
//            String portss = pref.getString("ports","");
//            ip_1.setText(ip11);
//            ip_2.setText(ip22);
//            ip_3.setText(ip33);
//            ip_4.setText(ip44);
//            port.setText(portss);
        }

    }
    @Override
    public void onBackPressed(){

//        ip_1 = (EditText)findViewById(R.id.ip_1);
//        ip_2 = (EditText)findViewById(R.id.ip_2);
//        ip_3 = (EditText)findViewById(R.id.ip_3);
//        ip_4 = (EditText)findViewById(R.id.ip_4);
//        port = (EditText)findViewById(R.id.port);
//        String ip1 = ip_1.getText().toString();
//        String ip2 = ip_2.getText().toString();
//        String ip3 = ip_3.getText().toString();
//        String ip4 = ip_4.getText().toString();
//        String ip = ip1+"."+ip2+"."+ip3+"."+ip4;
//        String ports = port.getText().toString();
//
        Intent intent = new Intent();
        intent.putExtra("motor_pattern",motor_pattern);
        intent.putExtra("motor_speed",motor_speed);
        intent.putExtra("motor_direction",motor_direction);
        intent.putExtra("motor_pulse",motor_pulse);
        intent.putExtra("laser_channel",laser_channel);
        intent.putExtra("valve_pos",valve_pos);
        setResult(RESULT_OK,intent);
//
//        editor = pref.edit();
//        editor.putString("ip1",ip1);
//        editor.putString("ip2",ip2);
//        editor.putString("ip3",ip3);
//        editor.putString("ip4",ip4);
//        editor.putString("ports",ports);
//        editor.putString("patterns",patterns);
//        editor.commit();
        finish();
        dataStore = true;

    }
    private  View.OnClickListener itemsOnClick = new View.OnClickListener(){
        public  void  onClick(View v){
            if(motorSelectWindow!=null)
                motorSelectWindow.dismiss();
            if(motorSpeedSelectWindow!=null)
                motorSpeedSelectWindow.dismiss();
            if(motorDirectionSelectWindow!=null)
                motorDirectionSelectWindow.dismiss();
            if(motorPulseSelectWindow!=null)
                motorPulseSelectWindow.dismiss();
            if(laserChannelSelectWindow!=null)
                laserChannelSelectWindow.dismiss();
            if(valvePositionSelectWindow!=null)
                valvePositionSelectWindow.dismiss();
            switch(v.getId()){
                case R.id.motorOK:
                    motor_pattern = getString(R.string.motor_OK);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_pattern, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorClose:
                    motor_pattern = getString(R.string.motor_Close);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_pattern, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorDirection1:
                    motor_direction = getString(R.string.motor_direction1);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_direction, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorDirection2:
                    motor_direction = getString(R.string.motor_direction2);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_direction, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorSpeed1:
                    motor_speed = getString(R.string.motor_speed1);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_speed, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorSpeed2:
                    motor_speed = getString(R.string.motor_speed2);
                    Toast.makeText(getApplicationContext(), "你点了  "+motor_speed, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorPulse1:
                    motor_pulse = getString(R.string.motor_pulse1);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_pulse, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.motorPulse2:
                    motor_pulse = getString(R.string.motor_pulse2);
                    Toast.makeText(getApplicationContext(), "你点了 "+motor_pulse, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.laserChannel1:
                    laser_channel = getString(R.string.laser_channel1);
                    Toast.makeText(getApplicationContext(), "你点了 "+laser_channel, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.laserChannel2:
                    laser_channel = getString(R.string.laser_channel2);
                    Toast.makeText(getApplicationContext(), "你点了 "+laser_channel, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.valvePosition1:
                    valve_pos = getString(R.string.valve_position1);
                    Toast.makeText(getApplicationContext(), "你点了 "+valve_pos, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.valvePosition2:
                    valve_pos = getString(R.string.valve_position2);
                    Toast.makeText(getApplicationContext(), "你点了 "+valve_pos, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.cancel:
                    if(motorSelectWindow!=null)
                        motorSelectWindow.dismiss();
                    if(motorSpeedSelectWindow!=null)
                        motorSpeedSelectWindow.dismiss();
                    if(motorDirectionSelectWindow!=null)
                        motorDirectionSelectWindow.dismiss();
                    if(motorPulseSelectWindow!=null)
                        motorPulseSelectWindow.dismiss();
                    if(laserChannelSelectWindow!=null)
                        laserChannelSelectWindow.dismiss();
                    if(valvePositionSelectWindow!=null)
                        valvePositionSelectWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(R.string.device_setting);
    }

}
