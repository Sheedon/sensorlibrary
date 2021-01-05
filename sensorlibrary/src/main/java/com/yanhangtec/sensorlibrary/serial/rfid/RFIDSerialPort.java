package com.yanhangtec.sensorlibrary.serial.rfid;

import com.yanhangtec.sensorlibrary.SensorFactory;
import com.yanhangtec.sensorlibrary.serial.RemoteService;
import com.yanhangtec.sensorlibrary.serial.rfid.converters.DataConverterFactory;
import com.yanhangtec.sensorlibrary.serial.rfid.converters.SerialConverterFactory;

import org.sheedon.serial.ResponseBody;
import org.sheedon.serial.SerialClient;
import org.sheedon.serial.retrofit.Retrofit;
import org.sheedon.serial.serialport.SerialRealCallback;

/**
 * RFID请求封装
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/7 21:24
 */
public class RFIDSerialPort {
    private static final String STARTBIT = "BB";
    private static final String ENDBIT = "7E";

    private static RFIDSerialPort instance;
    private Retrofit retrofit;
    private SerialClient client;

    static {
        instance = new RFIDSerialPort();
    }

    private RFIDSerialPort() {
    }

    public static SerialClient getClient() {
        if (instance.client != null)
            return instance.client;

        // 存储起来
        instance.client = new SerialClient.Builder()
                // 串口
                .path(SensorFactory.getPort())
                // 波特率
                .baudRate(SensorFactory.getBaudRate())
                // 名称 随意
                .name("rfid")
                // 线程取值间隔
                .threadSleepTime(300)
                // 信息超时时长
                .messageTimeout(3)
                .addConverterFactory(DataConverterFactory.create())
                .callback(instance.callback)
                .build();

        return instance.client;
    }

    // 构建一个Retrofit
    private static Retrofit getRetrofit() {
        if (instance.retrofit != null) {
            return instance.retrofit;
        }

        // 得到一个SerialPort Client
        SerialClient client = getClient();

        // 设置链接
        instance.retrofit = new Retrofit.Builder()
                // 设置client
                .client(client)
                // 设置默认起始位
                .baseStartBit(STARTBIT)
                // 设置默认停止位
                .baseEndBit(ENDBIT)
                // 设置串口解析器
                .addConverterFactory(SerialConverterFactory.create())
                .build();

        return instance.retrofit;
    }

    public static boolean isAlive() {
        SerialClient client = getClient();
        if (client == null)
            return false;

        return client.isAlive();
    }

    /**
     * 返回一个请求代理
     *
     * @return RemoteService
     */
    public static RemoteService remote() {
        return RFIDSerialPort.getRetrofit().create(RemoteService.class);
    }

    private SerialRealCallback callback = new SerialRealCallback() {
        @Override
        public void onCallback(ResponseBody data) {

        }

        @Override
        public void onRequest(String message) {

        }
    };
}
