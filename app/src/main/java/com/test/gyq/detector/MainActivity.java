package com.test.gyq.detector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

public class MainActivity extends AppCompatActivity {

    private LineChartView lineChart;
    String[] date = {"0"};//X轴的标注
    int[] score = {0};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private Timer timer = null;
    private static boolean isDoing = false;
    Socket socket;

    DatagramSocket socket2;

    private TextView Receiver;

    private EditText sendText;

    private Button send, clean_1, clean_2;

    private SharedPreferences pref;
    static String ip="", patterns="";
    static String wifi_name="",wifi_pwd="",wifi_port="",motor_pattern="",motor_speed="",motor_pulse="",
            motor_direction="",laser_channel="",valve_pos="";
    static int port_num=0;

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                if (bundle.getString("tip") == null) {
                    Receiver.append(bundle.getString("receive") + "\n");
                } else
                    Toast.makeText(MainActivity.this, bundle.getString("tip"), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                String sendtext = sendText.getText().toString();
                Receiver.append("通信模式：" + patterns + "\n");
                Receiver.append("目标IP：" + ip + "\n");
                Receiver.append("目标端口：" + port_num + "\n");
                new MyThread(sendtext).start();
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
        wifi_port = pref.getString("wifi_port","");

        ImageButton infoBtn = (ImageButton) findViewById(R.id.info);
        ImageButton wifiBtn = (ImageButton) findViewById(R.id.wifi);
        ImageButton getBtn = (ImageButton) findViewById(R.id.get);
        final ImageButton doBtn = (ImageButton) findViewById(R.id.go);
//        wifiBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//        getBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//        doBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("配置具体情况");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                final TextView tv = new TextView(MainActivity.this);
                //tv.setBackgroundResource(R.drawable.fengmian);
                tv.setTextSize(25);
                tv.setTextColor(Color.RED);
                String info="Wifi名称："+wifi_name+
                        "\nWifi密码："+wifi_pwd+
                        "\nWifi端口："+wifi_port+
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
                //Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
            }
        });
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
                    doBtn.setImageResource(R.drawable.pause);
                    if(timer==null)
                        timer = new Timer();
                    timer.schedule(new Mytask(),new Date(),2000);
                    return;
                }
                if (isDoing) {
                    doBtn.setImageResource(R.drawable.start);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    wifi_name = data.getStringExtra("wifi_name");
                    wifi_pwd = data.getStringExtra("wifi_pwd");
                    wifi_port = data.getStringExtra("wifi_port");
                    String pattern = data.getStringExtra("pattern");
                //    port_num = Integer.parseInt(port);
                    patterns = pattern;
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

    class MyThread extends Thread {

        private String text;

        public MyThread(String str) {
            text = str;
        }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            if (patterns.equals("TCP")) {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port_num), 10000);
                    OutputStream writer = socket.getOutputStream();
                    writer.write(text.getBytes("UTF-8"));
                    writer.flush();
                    InputStream reader = socket.getInputStream();
                    byte[] buf = new byte[1024 * 4];
                    int receives = reader.read(buf);
                    String receive = new String(buf, 0, receives);
                    bundle.putString("receive", receive);
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (SocketTimeoutException aa) {
                    bundle.putString("tip", "服务器连接失败！请检查网络是否打开");
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (patterns.equals("UDP")) {
                try {
                    if (socket2 == null) {
                        socket2 = new DatagramSocket(null);
                        socket2.setReuseAddress(true);
                        socket2.bind(new InetSocketAddress(port_num));
                    }
                    InetAddress serverAddress = InetAddress.getByName(ip);
                    byte output_data[] = text.getBytes();
                    DatagramPacket outputPacket = new DatagramPacket(output_data, output_data.length, serverAddress, port_num);
                    socket2.send(outputPacket);
                    byte input_data[] = new byte[1024 * 4];
                    DatagramPacket inputPacket = new DatagramPacket(input_data, input_data.length);
                    socket2.receive(inputPacket);
                    String receive = new String(inputPacket.getData(), inputPacket.getOffset(), inputPacket.getLength());
                    bundle.putString("receive", receive);
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                    socket2.close();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
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
}

