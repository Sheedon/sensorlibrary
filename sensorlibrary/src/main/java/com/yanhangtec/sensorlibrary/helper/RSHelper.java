package com.yanhangtec.sensorlibrary.helper;

import com.yanhangtec.sensorlibrary.model.DataSource;
import com.yanhangtec.sensorlibrary.model.OperatingModel;
import com.yanhangtec.sensorlibrary.model.RSModel;
import com.yanhangtec.sensorlibrary.serial.RemoteService;
import com.yanhangtec.sensorlibrary.serial.rs485.RSSerialPort;

import org.sheedon.serial.retrofit.Call;
import org.sheedon.serial.retrofit.Callback;
import org.sheedon.serial.retrofit.Observable;
import org.sheedon.serial.retrofit.Response;

/**
 * java类作用描述
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 2:26 PM
 */
public class RSHelper {

    /**
     * 绑定RFID标签结果
     *
     * @param callback 反馈
     */
    public static void bindRSResult(final DataSource.Callback<RSModel> callback) {
        RemoteService service = RSSerialPort.remote();
        Observable<RSModel> observable = service.bindRS();
        observable.subscribe(new Callback.Observable<RSModel>() {
            @Override
            public void onResponse(Observable<RSModel> call, Response<RSModel> response) {
                if (callback == null)
                    return;

                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.onDataLoaded(null);
                    return;
                }

                callback.onDataLoaded(response.body());
            }

            @Override
            public void onFailure(Observable<RSModel> call, Throwable t) {
                if (callback == null)
                    return;
                callback.onDataNotAvailable(t.getMessage());
            }
        });
    }


    /**
     * 绑定RFID标签结果
     *
     * @param callback 反馈
     */
    public static void setAutoReadCard(final DataSource.Callback<OperatingModel> callback) {
        RemoteService service = RSSerialPort.remote();
        Call<OperatingModel> observable = service.setAutoReadCard();
        observable.enqueue(new Callback.Call<OperatingModel>() {

            @Override
            public void onResponse(Call<OperatingModel> call, Response<OperatingModel> response) {
                if (callback == null)
                    return;

                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.onDataLoaded(null);
                    return;
                }

                callback.onDataLoaded(response.body());
            }

            @Override
            public void onFailure(Call<OperatingModel> call, Throwable t) {
                if (callback == null)
                    return;
                callback.onDataNotAvailable(t.getMessage());
            }
        });
    }
}
