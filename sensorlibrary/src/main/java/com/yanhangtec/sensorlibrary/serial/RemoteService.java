package com.yanhangtec.sensorlibrary.serial;


import com.yanhangtec.sensorlibrary.model.OperatingModel;
import com.yanhangtec.sensorlibrary.model.RFIDModel;
import com.yanhangtec.sensorlibrary.model.RSModel;

import org.sheedon.serial.retrofit.Call;
import org.sheedon.serial.retrofit.Observable;
import org.sheedon.serial.retrofit.serialport.BACKNAME;
import org.sheedon.serial.retrofit.serialport.MESSAGEBIT;
import org.sheedon.serial.retrofit.serialport.PARITYBIT;

/**
 * 串口接口
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/7 12:28
 */
public interface RemoteService {

    /**
     * 发送设置信号强度指令指令
     */
    @MESSAGEBIT("00B6000207D0")
    @PARITYBIT("8F")
    Call<Void> setSignalStrength();

    /**
     * 发送连续读取指令指令
     */
    @MESSAGEBIT("0027000322FFFF")
    @PARITYBIT("4A")
    Call<Void> sendContinuousRead();

    /**
     * 绑定rfid指令反馈
     */
    @BACKNAME("01FF")
    Observable<Void> bindCommandBack();


    /**
     * 绑定rfid反馈
     */
    @BACKNAME("0222")
    Observable<RFIDModel> bindRFID();

    @MESSAGEBIT("0308C120020000")
    @PARITYBIT("17")
    @BACKNAME("C1")
    Call<OperatingModel> setAutoReadCard();

    @BACKNAME("02")
    Observable<RSModel> bindRS();

}
