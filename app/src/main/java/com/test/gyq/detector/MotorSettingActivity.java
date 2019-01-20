package com.test.gyq.detector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

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
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        Button doPusleBtn = (Button)findViewById(R.id.doPulse);
        doPusleBtn.setWidth(width/2);
        doPusleBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                new MainActivity.MyThread("@G").start();
                sendMsg("@G");
            }
        });
        Button closePusleBtn = (Button)findViewById(R.id.closePulse);
        closePusleBtn.setWidth(width/2);
        closePusleBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
   //             new MainActivity.MyThread("@T").start();
                sendMsg("@T");
            }
        });
        Button openLaserBtn = (Button)findViewById(R.id.openLaser);
        openLaserBtn.setWidth(width/2);
        openLaserBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                new MainActivity.MyThread("@Z").start();
                sendMsg("@Z");
            }
        });
        Button closeLaserBtn = (Button)findViewById(R.id.closeLaser);
        closeLaserBtn.setWidth(width/2);
        closeLaserBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                new MainActivity.MyThread("@K").start();
                sendMsg("@K");
            }
        });
        Button readADBtn = (Button)findViewById(R.id.readAD);
        readADBtn.setWidth(width/2);
        readADBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                new MainActivity.MyThread("@F").start();
                sendMsg("@F");
            }
        });
        Button readIDBtn = (Button)findViewById(R.id.readID);
        readIDBtn.setWidth(width/2);
        readIDBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                new MainActivity.MyThread("@H").start();
                sendMsg("@H");
            }
        });
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
                dataStore = true;
                onBackPressed();
            }
        });

        if(dataStore){
        }

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("motor_pattern",motor_pattern);
        intent.putExtra("motor_speed",motor_speed);
        intent.putExtra("motor_direction",motor_direction);
        intent.putExtra("motor_pulse",motor_pulse);
        intent.putExtra("laser_channel",laser_channel);
        intent.putExtra("valve_pos",valve_pos);
        setResult(RESULT_OK,intent);
        finish();
        dataStore = true;
    }
    private  View.OnClickListener itemsOnClick = new View.OnClickListener(){
        public  void  onClick(View v){
            switch(v.getId()){
                case R.id.motorOK:
                    motor_pattern = pref.getString("motor_OK","");
                    editor = pref.edit();
                    editor.putString("motor_pattern",motor_pattern);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_pattern, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@R"+motor_pattern).start();
                    sendMsg("@R"+motor_pattern);
                    dismissWindow();
                    break;
                case R.id.motorClose:
                    motor_pattern = pref.getString("motor_Close","");
                    editor = pref.edit();
                    editor.putString("motor_pattern",motor_pattern);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_pattern, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@R"+motor_pattern).start();
                    sendMsg("@R"+motor_pattern);
                    dismissWindow();
                    break;
                case R.id.motorDirection1:
                    motor_direction = pref.getString("motor_direction1","");
                    editor = pref.edit();
                    editor.putString("motor_direction",motor_direction);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_direction, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@D"+motor_direction).start();
                    sendMsg("@D"+motor_direction);
                    dismissWindow();
                    break;
                case R.id.motorDirection2:
                    motor_direction = pref.getString("motor_direction2","");
                    editor = pref.edit();
                    editor.putString("motor_direction",motor_direction);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_direction, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@D"+motor_direction).start();
                    sendMsg("@D"+motor_direction);
                    dismissWindow();
                    break;
                case R.id.motorSpeed1:
                    motor_speed = pref.getString("motor_speed1","");
                    editor = pref.edit();
                    editor.putString("motor_speed",motor_speed);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_speed, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@S"+motor_speed).start();
                    sendMsg("@S"+motor_speed);
                    dismissWindow();
                    break;
                case R.id.motorSpeed2:
                    motor_speed = pref.getString("motor_speed2","");
                    editor = pref.edit();
                    editor.putString("motor_speed",motor_speed);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了  "+motor_speed, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@S"+motor_speed).start();
                    sendMsg("@S"+motor_speed);
                    dismissWindow();
                    break;
                case R.id.motorPulse1:
                    motor_pulse = pref.getString("motor_pulse1","");
                    editor = pref.edit();
                    editor.putString("motor_pulse",motor_pulse);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_pulse, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@P"+motor_pulse).start();
                    sendMsg("@P"+motor_pulse);
                    dismissWindow();
                    break;
                case R.id.motorPulse2:
                    motor_pulse = pref.getString("motor_pulse2","");
                    editor = pref.edit();
                    editor.putString("motor_pulse",motor_pulse);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+motor_pulse, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@P"+motor_pulse).start();
                    sendMsg("@P"+motor_pulse);
                    dismissWindow();
                    break;
                case R.id.laserChannel1:
                    laser_channel = pref.getString("laser_channel1","");
                    editor = pref.edit();
                    editor.putString("laser_channel",laser_channel);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+laser_channel, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@E"+laser_channel).start();
                    sendMsg("@E"+laser_channel);
                    dismissWindow();
                    break;
                case R.id.laserChannel2:
                    laser_channel = pref.getString("laser_channel2","");
                    editor = pref.edit();
                    editor.putString("laser_channel",laser_channel);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+laser_channel, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@E"+laser_channel).start();
                    sendMsg("@E"+laser_channel);
                    dismissWindow();
                    break;
                case R.id.valvePosition1:
                    valve_pos = pref.getString("valve_position1","");
                    editor = pref.edit();
                    editor.putString("valve_pos",valve_pos);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+valve_pos, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@B"+valve_pos).start();
                    sendMsg("@B"+valve_pos);
                    dismissWindow();
                    break;
                case R.id.valvePosition2:
                    valve_pos = pref.getString("valve_position2","");
                    editor = pref.edit();
                    editor.putString("valve_pos",valve_pos);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "你选择了 "+valve_pos, Toast.LENGTH_SHORT).show();
//                    new MainActivity.MyThread("@B"+valve_pos).start();
                    sendMsg("@B"+valve_pos);
                    dismissWindow();
                    break;
                case R.id.motorOKModify:
                    setPara("motor_OK");
                    break;
                case R.id.motorCloseModify:
                    setPara("motor_Close");
                    break;
                case R.id.motorSpeed1Modify:
                    setPara("motor_speed1");
                    break;
                case R.id.motorSpeed2Modify:
                    setPara("motor_speed2");
                    break;
                case R.id.motorDirection1Modify:
                    setPara("motor_direction1");
                    break;
                case R.id.motorDirection2Modify:
                    setPara("motor_direction2");
                    break;
                case R.id.motorPulse1Modify:
                    setPara("motor_pulse1");
                    break;
                case R.id.motorPulse2Modify:
                    setPara("motor_pulse2");
                    break;
                case R.id.laserChannel1Modify:
                    setPara("laser_channel1");
                    break;
                case R.id.laserChannel2Modify:
                    setPara("laser_channel2");
                    break;
                case R.id.valvePosition1Modify:
                    setPara("valve_position1");
                    break;
                case R.id.valvePosition2Modify:
                    setPara("valve_position2");
                    break;
                case R.id.cancel:
                    dismissWindow();
                    break;
                default:
                    break;
            }
        }
    };

    private void sendMsg(String s) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.clear();
        bundle.putString("send", s);
        msg.setData(bundle);
        msg.what = 3;
        MainActivity.handler.sendMessage(msg);
    }

    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(R.string.device_setting);
    }
    private void setPara(final String para_name){
        AlertDialog.Builder builder = new AlertDialog.Builder(MotorSettingActivity.this);
        builder.setTitle("请输入 "+getString(getResources().getIdentifier(para_name,"string",getPackageName()))+" 参数值：");    //设置对话框标题
        builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
        final EditText edit = new EditText(MotorSettingActivity.this);
        edit.setText(pref.getString(para_name,""));
        builder.setView(edit);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data1 = edit.getText().toString();
                editor = pref.edit();
                editor.putString(para_name,data1);
                editor.commit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MotorSettingActivity.this, "你选择了取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        AlertDialog dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }
    private void dismissWindow(){
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
    }
}
