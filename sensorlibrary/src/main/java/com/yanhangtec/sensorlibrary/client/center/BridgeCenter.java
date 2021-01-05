package com.yanhangtec.sensorlibrary.client.center;

import com.yanhangtec.sensorlibrary.client.listener.OnHandleListener;

/**
 * 通知中心
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/15 15:35
 */
public interface BridgeCenter<Listener extends OnHandleListener> {

    // 初始化配置
    void initConfig();

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
