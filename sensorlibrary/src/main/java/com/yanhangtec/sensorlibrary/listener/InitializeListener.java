package com.yanhangtec.sensorlibrary.listener;

/**
 * 初始化监听器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 1/5/21 11:04 AM
 */
public interface InitializeListener {

    // 初始化完成
    void OnInitializeComplete();

    // 感应类型
    int onSensorType();

    // 端口
    String onPort();

    // 波特率
    int onBaudRate();
}
