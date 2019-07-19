package com.test.gyq.detector;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.rxpermisson.PermissionAppCompatActivity;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.content.BroadcastReceiver;

import gyq.wifixiaofang.ui.adapter.MyListViewAdapter;
import gyq.wifixiaofang.ui.api.OnNetworkChangeListener;
import gyq.wifixiaofang.ui.component.MyListView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import gyq.wifixiaofang.utils.*;
import gyq.wifixiaofang.ui.*;
import rx.Subscriber;

import static android.content.Context.WIFI_SERVICE;

public class MainActivity extends PermissionAppCompatActivity {

    private LineChartView lineChart;
    String[] date = {"0"};//X轴的标注
    int[] score = {0};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private Timer timer = null;
    private static boolean isDoing = false;
    static Socket socket = null;
    static Socket socket3 = null;
    static Context context;

    static DatagramSocket socket2 = null;

    private static TextView Receiver;

    private static EditText sendText;

    private static String text = "",rtext = "";

    private static DatagramPacket dp = null;

    private Button send, clean_1, clean_2;

    private static SharedPreferences pref;
    static String patterns="";
    static String wifi_name="",wifi_pwd="",wifi_port="",wifi_ip="",
            local_ip="",local_port="",
            motor_pattern="",motor_speed="",motor_pulse="",
            motor_direction="",laser_channel="",valve_pos="";
    static int wifi_port_num=0,local_port_num=0;
    private InternetDynamicBroadCastReceiver mReceiver;
    //final WifiAdminUtils mWifiAdmin = null;
    //WifiC wifiC = null;
    public static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    text = sendText.getText().toString();
                    final byte[] buffer = text.getBytes();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(!updataPara())
                                return;
                            DatagramSocket ds = null;
                            try {

                                if(dp == null)
                                    dp = new DatagramPacket(buffer,buffer.length,
                                        new InetSocketAddress(wifi_ip,wifi_port_num));
                                ds = new DatagramSocket();
                                ds.send(dp);
                                ds.close();
                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;

                case 3:
                    String mt = msg.getData().getString("send");
                    final byte[] buffer2 = mt.getBytes();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(!updataPara())
                                return;
                            DatagramSocket ds = null;
                            try {
                                dp = new DatagramPacket(buffer2,
                                        buffer2.length,
                                        new InetSocketAddress(wifi_ip,wifi_port_num));
                                ds = new DatagramSocket();
                                ds.send(dp);
                                ds.close();
                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 4:
                    Toast.makeText(context, msg.getData().getString("tip"), Toast.LENGTH_LONG).show();
                    break;

                default:
                    if(!TextUtils.isEmpty(rtext)) {
            //            Receiver = (TextView) findViewById(R.id.receiver);
                        Receiver.append(rtext);
                        rtext = "";
                    }
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(R.string.base_permission, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE)
                .subscribe(new Subscriber() {
                    @Override
                    public void onNext(Object o) {
                        if (o.equals(true)){
                            //             Toast.makeText(MainActivity.this,"请求权限成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"请求权限失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });
        context = MainActivity.this;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sendText = (EditText) findViewById(R.id.sendText);
        Receiver = (TextView) findViewById(R.id.receiver);
        Receiver.setMovementMethod(ScrollingMovementMethod.getInstance());
        send = (Button) findViewById(R.id.send);
        clean_1 = (Button) findViewById(R.id.clean_1);
        clean_2 = (Button) findViewById(R.id.clean_2);
        clean_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendText.setText("");
            }
        });
        clean_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Receiver.setText("");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
      //          String sendtext = sendText.getText().toString();
                if(!updataPara())
                    return;
                handler.sendEmptyMessage(1);
          //      Toast.makeText(getApplicationContext(), Receiver.getText().toString(), Toast.LENGTH_LONG).show();
//                new MyThread(sendtext).start();
            }

        });

        lineChart = (LineChartView) findViewById(R.id.line_chart);
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
        motor_pattern = pref.getString("motor_pattern","");
        motor_speed = pref.getString("motor_speed","");
        motor_pulse = pref.getString("motor_pulse","");
        motor_direction = pref.getString("motor_direction","");
        laser_channel = pref.getString("laser_channel","");
        valve_pos = pref.getString("valve_pos","");
        wifi_name = pref.getString("wifi_name","");
        wifi_pwd = pref.getString("wifi_pwd","");
        wifi_ip = pref.getString("wifi_ip","");
        wifi_port = pref.getString("wifi_port","");
        local_ip = pref.getString("local_ip","");
        local_port = pref.getString("local_port","");
        patterns = pref.getString("patterns","");

        ImageButton infoBtn = (ImageButton) findViewById(R.id.info);
        ImageButton wifiBtn = (ImageButton) findViewById(R.id.wifi);
        ImageButton getBtn = (ImageButton) findViewById(R.id.get);
        final ImageButton doBtn = (ImageButton) findViewById(R.id.go);
//        wifiBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//        getBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//        doBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!updataPara())
                    return;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("配置具体情况");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                final TextView tv = new TextView(MainActivity.this);
                //tv.setBackgroundResource(R.drawable.fengmian);
                tv.setTextSize(25);
                tv.setTextColor(Color.RED);
                String connet_status = "未连接";
                WifiC wifiC = new WifiC();
                String wifi_name1 = wifiC.getWifiSSID(context);
                if (wifi_name1.indexOf(wifi_name) > 0)
                    connet_status = wifi_name1 + "已连接";
                String info="Wifi名称："+wifi_name+
                        "\nWifi密码："+wifi_pwd+
                        "\nWifi IP："+wifi_ip+
                        "\nWifi端口："+wifi_port_num+
                        "\n本地 IP："+local_ip+
                        "\n本地端口："+local_port_num+
                        "\n通信方式："+patterns+
                        "\n连接WIFI状态："+connet_status+
                        "\n\n步进电机工作状态："+motor_pattern+
                        "\n步进速度："+motor_speed+
                        "\n步进脉冲："+motor_pulse+
                        "\n步进方向："+motor_direction+
                        "\n光开关通道："+laser_channel+
                        "\n十通阀位置："+valve_pos;
                tv.setText(info);
                tv.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL);
                tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                builder.setView(tv);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
                //Toast.makeText(WifiActivity.this, info, Toast.LENGTH_LONG).show();
            }
        });
        wifiBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!updataPara())
                    return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //toastutils.showLong("网络侦听正在启用");
                        showMsg("网络侦听正在启用");
                        byte[] buffer = new byte[1024];
                        //虽然开辟的缓冲内存大小为1024字节，但也可以设置一个小于该值的缓存空间接收数据包
                        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                        DatagramSocket ds = null;
                        try {
                            //监听在UDP 端口
                            ds = new DatagramSocket(local_port_num);
                        } catch (SocketException e) {
                            e.printStackTrace();
                            showMsg("无法创建侦听端口");
                            return;
                        }
                        while (true) {
                            try {
                                //receive() 方法是一个阻塞性方法！
                                ds.receive(dp);
                                handler.sendEmptyMessage(2);
                                 } catch (IOException e) {
                                e.printStackTrace();
                                showMsg("无法接收消息");
                                return;
                            }
                            rtext = new String(dp.getData(),
                                    dp.getOffset(), dp.getLength());
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).start();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new InternetDynamicBroadCastReceiver();
        this.registerReceiver(new InternetDynamicBroadCastReceiver(), filter);
        getBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //sendText.setText("");
                Random ran = new Random();
                String date1 = "" + mAxisXValues.size();
                mAxisXValues.add(new AxisValue(mAxisXValues.size()).setLabel(date1));
                int score1 = ran.nextInt(100);//b数组新增元素
                mPointValues.add(new PointValue(mPointValues.size(), score1));
                initLineChart();//初始化
            }
        });
        doBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //sendText.setText("");
                if (!isDoing) {
                    isDoing = true;
                    doBtn.setImageResource(android.R.drawable.ic_media_pause);
                    if(timer==null)
                        timer = new Timer();
                    timer.schedule(new Mytask(),new Date(),2000);
                    return;
                }
                if (isDoing) {
                    doBtn.setImageResource(android.R.drawable.ic_media_play);
                    if(timer!=null) {
                        timer.cancel();
                        timer=null;
                    }
                    isDoing = false;
                }
            }
        });

    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.wifi_setting) {
            //           Intent intent = new Intent(this,SettingActivity.class);
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, 1);
        }
        if (id == R.id.device_setting) {
            //           Intent intent = new Intent(this,SettingActivity.class);
            Intent intent = new Intent(this, MotorSettingActivity.class);
            startActivityForResult(intent, 2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    wifi_name = data.getStringExtra("wifi_name");
                    wifi_pwd = data.getStringExtra("wifi_pwd");
                    if(wifi_ip != data.getStringExtra("wifi_ip") ||
                            wifi_port != data.getStringExtra("wifi_port") ||
                            local_ip != data.getStringExtra("local_ip") ||
                            local_port != data.getStringExtra("local_port") ||
                            patterns != data.getStringExtra("patterns"))
                    {
                        if(socket != null)
                            try {
                                socket.close();
                            } catch (IOException e) {
                            }
                        if(socket3 != null)
                            try {
                                socket3.close();
                            } catch (IOException e) {
                            }
                        if(socket2 != null)
                            socket2.close();
                        socket =  null;
                        socket3 =  null;
                        socket2 = null;
                    }
                //    port_num = Integer.parseInt(port);
                    //获取wifi服务
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    motor_pattern = data.getStringExtra("motor_pattern");
                    motor_speed = data.getStringExtra("motor_speed");
                    motor_pulse = data.getStringExtra("motor_pulse");
                    motor_direction = data.getStringExtra("motor_direction");
                    laser_channel = data.getStringExtra("laser_channel");
                    valve_pos = data.getStringExtra("valve_pos");
                }
                break;
            default:
        }
    }
    public class InternetDynamicBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this,"网络发生了变化",Toast.LENGTH_SHORT).show();
        }
    }
    public static void showMsg(String mtext){
        Message msg = new Message();
        msg.what = 4;
        Bundle bundle = new Bundle();
        bundle.clear();
        bundle.putString("tip", mtext);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public class Mytask extends TimerTask {
        @Override
        public void run() {
            if (isDoing) {
                Random rd = new Random();
                String date1 = ""+mAxisXValues.size();
                mAxisXValues.add(new AxisValue(mAxisXValues.size()).setLabel(date1));
                int score1 = rd.nextInt(100);//b数组新增元素
                mPointValues.add(new PointValue(mPointValues.size(), score1));
                initLineChart();//初始化
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socket != null)
            try {
                socket.close();
            } catch (IOException e) {
            }
        if(socket3 != null)
            try {
                socket3.close();
            } catch (IOException e) {
            }
        if(socket2 != null)
            socket2.close();
        socket =  null;
        socket3 =  null;
        socket2 = null;        //关闭线程
      //  udpUtils.setKeepRunning(false);
    }

    public static boolean updataPara(){
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        motor_pattern = pref.getString("motor_pattern","");
        motor_speed = pref.getString("motor_speed","");
        motor_pulse = pref.getString("motor_pulse","");
        motor_direction = pref.getString("motor_direction","");
        laser_channel = pref.getString("laser_channel","");
        valve_pos = pref.getString("valve_pos","");
        wifi_name = pref.getString("wifi_name","");
        wifi_pwd = pref.getString("wifi_pwd","");
        wifi_ip = pref.getString("wifi_ip","");
        wifi_port = pref.getString("wifi_port","");
        local_ip = pref.getString("local_ip","");
        local_port = pref.getString("local_port","");
        patterns = pref.getString("patterns","");
        if(wifi_ip=="" || wifi_port=="")
            return false;
        if((local_ip=="" || local_port=="") && patterns == "UDP")
            return false;
        try{
            wifi_port_num = Integer.parseInt(wifi_port);
        }catch(NumberFormatException e)
        {
            showMsg("WIFI通信端口出错");
            return false;
        }
        try {
            local_port_num = Integer.parseInt(local_port);
        } catch (NumberFormatException e) {
            showMsg("本地通信端口出错");
            return false;
        }
        return true;
    }

}


 class WifiC{

    protected static final String TAG = "MainActivity";

    private static final int REFRESH_CONN = 100;
    // Wifi管理类
    private WifiAdminUtils mWifiAdmin = null;
    // 扫描结果列表
    private List<ScanResult> list = new ArrayList<ScanResult>();
    // 显示列表
    private MyListView listView;

    private MyListViewAdapter mAdapter;
    //下标
    private int mPosition;

    private Context context;

    public String ip = "";

     SharedPreferences pref;
     String patterns="";
     String wifi_name="",wifi_pwd="",wifi_port="",wifi_ip="",
             local_ip="",local_port="";



    private OnNetworkChangeListener mOnNetworkChangeListener = new OnNetworkChangeListener() {

        @Override
        public void onNetWorkDisConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNetWorkConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    };
    public void msg(String text){
        AlertDialog.Builder  builder=new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.abc_btn_radio_to_on_mtrl_000);
        builder.setMessage(text);

        //为builder对象添加确定按钮，不过这里嵌套了一个函数
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0,int arg1)
                    {
                        //android.os.Process.killProcess(android.os.Process.myPid());
                        //        finish();
                    }
                }
        );
        //builder创建对话框对象AlertDialog
        AlertDialog simpledialog=builder.create();
        simpledialog.show();


    }

    public void execCommand(String command) throws IOException {
        // start the ls command running
        //String[] args =  new String[]{"sh", "-c", command};
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        // read the ls output
        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append('\n');
        }
        //tv.setText(sb.toString());
        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public void setMobileDataStatus(Context context,boolean enabled) throws InvocationTargetException
    {
        Method dataConnSwitchmethod = null;
        Class telephonyManagerClass = null;
        Object ITelephonyStub = null;
        Class ITelephonyClass = null;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

//        if(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED){
//            isEnabled = true;
//        }else{
//            isEnabled = false;
//        }

        try {
            telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getITelephonyMethod.setAccessible(true);
        try {
            ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (!enabled) {
            try {
                dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            try {
                dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        dataConnSwitchmethod.setAccessible(true);
        try {
            dataConnSwitchmethod.invoke(ITelephonyStub);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setMobileDataEnabled(Context context, boolean enabled){
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = null;
        try {
            cmClass = Class.forName(cm.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field iConnectivityManagerField = null;
        try {
            iConnectivityManagerField = cmClass.getDeclaredField("mService");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        iConnectivityManagerField.setAccessible(true);
        Object iConnectivityManager = null;
        try {
            iConnectivityManager = iConnectivityManagerField.get(cm);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Class iConnectivityManagerClass = null;
        try {
            iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method setMobileDataEnabledMethod = null;
        try {
            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        setMobileDataEnabledMethod.setAccessible(true);
        try {
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public boolean dowifi(Context context1) {
        context = context1;
        initData();
        while(!mWifiAdmin.openWifi()){
            //      while (mWifiAdmin.checkState() != WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
                //           mWifiAdmin.openWifi();
            } catch (InterruptedException ie) {
                Toast.makeText(context, "打开WIFI出错", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(mWifiAdmin.getBSSID().indexOf(wifi_name) <= 0)
            mWifiAdmin.disConnectionWifi(mWifiAdmin.getConnNetId());
        int i= 20;
        boolean connect_ok = false;
        while(i > 0){
            //      while (mWifiAdmin.checkState() != WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
                if(mWifiAdmin.connect(wifi_name,wifi_pwd,
                        WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA)){
                    connect_ok = true;
                    break;
                }
                else
                    i = i - 1;;
                //           mWifiAdmin.openWifi();
            } catch (InterruptedException ie) {
                Toast.makeText(context, "连接ESP WIFI出错", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(connect_ok) {
            Toast.makeText(context, "连接成功\n", Toast.LENGTH_LONG).show();
            return true;
//            Settings.System.putString(cr, Settings.System.WIFI_STATIC_IP, local_ip);
        }
        else {
            Toast.makeText(context, "连接失败", Toast.LENGTH_LONG).show();
            return false;
        }
//        isGetIp = true;
    }

      /**
     * 初始化数据
     */
    private void initData() {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        wifi_name = pref.getString("wifi_name","");
        wifi_pwd = pref.getString("wifi_pwd","");
        wifi_ip = pref.getString("wifi_ip","");
        wifi_port = pref.getString("wifi_port","");
        local_ip = pref.getString("local_ip","");
        local_port = pref.getString("local_port","");
        patterns = pref.getString("patterns","");
        if(mWifiAdmin == null)
            mWifiAdmin = new WifiAdminUtils(context);
        // 获得Wifi列表信息
        getWifiListInfo();
    }


    /**
     * 得到wifi的列表信息
     */
    private void getWifiListInfo() {
        Log.d(TAG, "getWifiListInfo");
        mWifiAdmin.startScan();
        List<ScanResult> tmpList = mWifiAdmin.getWifiList();
        if (tmpList == null) {
            list.clear();
        } else {
            list = tmpList;
        }
    }
    public String getWifiSSID(Context context1){
        context = context1;
        initData();
        return mWifiAdmin.getBSSID();
    }
}
