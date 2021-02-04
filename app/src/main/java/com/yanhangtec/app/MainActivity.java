package com.yanhangtec.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.yanhangtec.sensorlibrary.SensorConstant;
import com.yanhangtec.sensorlibrary.SensorFactory;
import com.yanhangtec.sensorlibrary.client.center.CardReaderCenter;
import com.yanhangtec.sensorlibrary.client.listener.OnCardReaderListener;
import com.yanhangtec.sensorlibrary.client.listener.OnExceptionListener;
import com.yanhangtec.sensorlibrary.listener.InitializeListener;

public class MainActivity extends AppCompatActivity
        implements InitializeListener, OnExceptionListener, OnCardReaderListener {


    private CardReaderCenter readerCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorFactory.init(SensorConstant.TYPE_RS485, SensorConstant.PORT, 9600, this);

        readerCenter = SensorFactory.getCardReaderCenter();
        readerCenter.bindException(this);
        readerCenter.addListener(this);
    }

    @Override
    public void OnInitializeComplete() {
        Log.v("SXD", "OnInitializeComplete");
    }

    @Override
    public int onSensorType() {
        return SensorConstant.TYPE_RS485;
    }

    @Override
    public String onPort() {
        return "/dev/ttyS1";
    }

    @Override
    public int onBaudRate() {
        return 9600;
    }

    @Override
    public void onException(boolean isException) {
    }

    @Override
    public void onCardInfo(String lblNum, boolean isContinuousSend) {
        Log.v("SXD", "lblNum:" + lblNum);
        Log.v("SXD", "isContinuousSend:" + isContinuousSend);
    }

    @Override
    public void onCardIsNormal(boolean isNormal) {
        Log.v("SXD", "isNormal:" + isNormal);
    }
}