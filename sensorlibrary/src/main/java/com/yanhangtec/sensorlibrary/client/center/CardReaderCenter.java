package com.yanhangtec.sensorlibrary.client.center;

import com.yanhangtec.sensorlibrary.client.listener.OnDebugListener;
import com.yanhangtec.sensorlibrary.client.listener.OnHandleListener;

/**
 * 读卡中心
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 2:22 PM
 */
public interface CardReaderCenter<Listener extends OnHandleListener> extends BridgeCenter<Listener> {

    // 获取当前卡片编号
    String getCurrentCard();

    boolean isNormal();

    void bindDebug(OnDebugListener listener);

}
