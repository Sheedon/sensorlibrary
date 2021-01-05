package com.yanhangtec.sensorlibrary;

/**
 * 基本配置信息
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/6/10 9:57
 */
public interface SensorConstant {

    int TYPE_RFID = 1;// 标准RFID读卡模式
    int TYPE_RS485 = 2;// RS485


    String PORT = "/dev/ttyS1";
    int BAUD_RATE = 115200;
}
