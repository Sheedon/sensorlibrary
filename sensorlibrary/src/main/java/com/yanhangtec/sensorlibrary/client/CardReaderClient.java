package com.yanhangtec.sensorlibrary.client;

import com.yanhangtec.sensorlibrary.SensorConstant;
import com.yanhangtec.sensorlibrary.SensorFactory;
import com.yanhangtec.sensorlibrary.client.center.CardReaderCenter;
import com.yanhangtec.sensorlibrary.client.listener.OnCardReaderListener;
import com.yanhangtec.sensorlibrary.client.listener.OnExceptionListener;
import com.yanhangtec.sensorlibrary.serial.rfid.RFIDSerialPort;
import com.yanhangtec.sensorlibrary.serial.rs485.RSSerialPort;

/**
 * 读卡器客户端
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 3:00 PM
 */
public class CardReaderClient implements CardReaderCenter {

    private static volatile CardReaderClient instance;

    private int type;

    public static CardReaderClient getInstance() {
        if (instance == null) {
            synchronized (CardReaderClient.class) {
                if (instance == null) {
                    instance = new CardReaderClient();
                }
            }
        }
        return instance;
    }

    private CardReaderClient() {

    }

    @Override
    public void initConfig() {
        initConfig(SensorFactory.getSensorType());
    }

    @Override
    public void initConfig(int type) {
        this.type = type;
        switch (type) {
            case SensorConstant.TYPE_RS485:
                RSClient.getInstance().initConfig();
                break;
            default:
                RFIDClient.getInstance().initConfig();
                break;
        }
    }

    @Override
    public synchronized void addListener(OnCardReaderListener listener) {
        switch (type) {
            case SensorConstant.TYPE_RS485:
                RSClient.getInstance().addListener(listener);
                break;
            default:
                RFIDClient.getInstance().addListener(listener);
                break;
        }
    }

    @Override
    public synchronized void removeListener(OnCardReaderListener listener) {
        switch (type) {
            case SensorConstant.TYPE_RS485:
                RSClient.getInstance().removeListener(listener);
                break;
            default:
                RFIDClient.getInstance().removeListener(listener);
                break;
        }
    }

    @Override
    public String getCurrentCard() {
        if (type == SensorConstant.TYPE_RS485) {
            return RSClient.getInstance().getCurrentCard();
        }
        return RFIDClient.getInstance().getCurrentCard();
    }

    public boolean isAlive() {
        if (type == SensorConstant.TYPE_RS485) {
            return RSSerialPort.isAlive();
        }
        return RFIDSerialPort.isAlive();
    }

    @Override
    public boolean isNormal() {
        if (type == SensorConstant.TYPE_RS485) {
            return RSClient.getInstance().isNormal();
        }
        return RFIDClient.getInstance().isNormal();
    }

    @Override
    public void bindException(OnExceptionListener listener) {
        if (type == SensorConstant.TYPE_RS485) {
            RSClient.getInstance().bindException(listener);
            return;
        }

        RFIDClient.getInstance().bindException(listener);
    }
}
