package com.yanhangtec.sensorlibrary.helper;

import com.yanhangtec.sensorlibrary.model.DataSource;
import com.yanhangtec.sensorlibrary.model.RFIDModel;
import com.yanhangtec.sensorlibrary.serial.RemoteService;
import com.yanhangtec.sensorlibrary.serial.rfid.RFIDSerialPort;

import org.sheedon.serial.retrofit.Call;
import org.sheedon.serial.retrofit.Callback;
import org.sheedon.serial.retrofit.Observable;
import org.sheedon.serial.retrofit.Response;

/**
 * RFID辅助类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/15 15:59
 */
public class RFIDHelper {

    /**
     * 绑定RFID标签结果
     *
     * @param callback 反馈
     */
    public static void bindRFIDResult(final DataSource.Callback<RFIDModel> callback) {
        RemoteService service = RFIDSerialPort.remote();
        Observable<RFIDModel> observable = service.bindRFID();
        observable.subscribe(new Callback.Observable<RFIDModel>() {
            @Override
            public void onResponse(Observable<RFIDModel> call, Response<RFIDModel> response) {
                if (callback == null)
                    return;

                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.onDataLoaded(null);
                    return;
                }

                callback.onDataLoaded(response.body());
            }

            @Override
            public void onFailure(Observable<RFIDModel> call, Throwable t) {
                if (callback == null)
                    return;
                callback.onDataNotAvailable(t.getMessage());
            }
        });
    }


    /**
     * 绑定指令反馈
     *
     * @param callback 反馈
     */
    public static void bindCommandResult(final DataSource.Callback<Void> callback) {
        RemoteService service = RFIDSerialPort.remote();
        Observable<Void> observable = service.bindCommandBack();
        observable.subscribe(new Callback.Observable<Void>() {
            @Override
            public void onResponse(Observable<Void> call, Response<Void> response) {
                if (callback == null)
                    return;

                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.onDataLoaded(null);
                    return;
                }

                callback.onDataLoaded(response.body());
            }

            @Override
            public void onFailure(Observable<Void> call, Throwable t) {
                if (callback == null)
                    return;
                callback.onDataNotAvailable(t.getMessage());
            }
        });
    }


    /**
     * 发送设置信号强度指令指令
     */
    public static void setSignalStrength() {
        RemoteService service = RFIDSerialPort.remote();
        Call<Void> observable = service.setSignalStrength();
        observable.publishNotCallback();
    }

    /**
     * 发送连续读取指令指令
     */
    public static void sendContinuousRead() {
        RemoteService service = RFIDSerialPort.remote();
        Call<Void> observable = service.sendContinuousRead();
        observable.publishNotCallback();
    }
}
