package com.yanhangtec.sensorlibrary;

import androidx.annotation.IntRange;

import com.yanhangtec.sensorlibrary.client.CardReaderClient;
import com.yanhangtec.sensorlibrary.client.center.CardReaderCenter;
import com.yanhangtec.sensorlibrary.client.listener.OnCardReaderListener;
import com.yanhangtec.sensorlibrary.listener.InitializeListener;

/**
 * RFID 感应工厂
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 1/5/21 10:49 AM
 */
public class SensorFactory {

    // 单例模式
    private static final SensorFactory instance;

    // 感应类型
    private int sensorType = SensorConstant.TYPE_RFID;
    private String port;
    private int baudRate;

    private InitializeListener listener;

    static {
        instance = new SensorFactory();
    }

    private SensorFactory() {

    }

    public static void init(@IntRange(from = SensorConstant.TYPE_RFID, to = SensorConstant.TYPE_RS485)
                                    int sensorType, InitializeListener listener) {
        init(sensorType, SensorConstant.PORT, SensorConstant.BAUD_RATE, listener);
    }

    public static void init(@IntRange(from = SensorConstant.TYPE_RFID, to = SensorConstant.TYPE_RS485)
                                    int sensorType, String port, int baudRate, InitializeListener listener) {
        instance.sensorType = sensorType;
        instance.listener = listener;
        instance.port = port;
        instance.baudRate = baudRate;

        CardReaderClient.getInstance().initConfig(sensorType);

        if (listener != null) {
            listener.OnInitializeComplete();
        }
    }


    /**
     * library中获取感应类型
     */
    public static int getSensorType() {
        if (instance.sensorType == 0) {
            if (instance.listener != null) {
                instance.sensorType = instance.listener.onSensorType();
            }

            if (instance.sensorType == 0) {
                instance.sensorType = SensorConstant.TYPE_RFID;
            }
        }
        return instance.sensorType;
    }

    // 端口
    public static String getPort() {
        if (instance.port == null || instance.port.equals("")) {
            if (instance.listener != null) {
                instance.port = instance.listener.onPort();
            }

            if (instance.port == null || instance.port.equals("")) {
                instance.port = SensorConstant.PORT;
            }
        }
        return instance.port;
    }

    // 波特率
    public static int getBaudRate() {
        if (instance.baudRate == 0) {
            if (instance.listener != null) {
                instance.baudRate = instance.listener.onBaudRate();
            }

            if (instance.baudRate == 0) {
                instance.baudRate = SensorConstant.BAUD_RATE;
            }
        }
        return instance.baudRate;
    }

    /**
     * 获取一个RFID处理中心的实现类
     *
     * @return RFID中心的规范接口
     */
    public static CardReaderCenter<OnCardReaderListener> getCardReaderCenter() {
        return CardReaderClient.getInstance();
    }

}
