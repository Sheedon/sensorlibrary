package com.yanhangtec.sensorlibrary.client.center;

import com.yanhangtec.sensorlibrary.client.listener.OnCardReaderListener;
import com.yanhangtec.sensorlibrary.client.listener.OnDebugListener;

/**
 * 读卡中心
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 2:22 PM
 */
public interface CardReaderCenter {

    // 初始化配置
    void initConfig();

    void initConfig(int type);

    void addListener(OnCardReaderListener listener);

    void removeListener(OnCardReaderListener listener);

    // 获取当前卡片编号
    String getCurrentCard();

    boolean isNormal();

    void bindDebug(OnDebugListener listener);

}
