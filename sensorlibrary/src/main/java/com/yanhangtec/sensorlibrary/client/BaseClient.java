package com.yanhangtec.sensorlibrary.client;

import com.yanhangtec.sensorlibrary.client.center.BridgeCenter;
import com.yanhangtec.sensorlibrary.client.listener.OnHandleListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 基础客户端类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/15 15:29
 */
public abstract class BaseClient<Listener extends OnHandleListener> implements BridgeCenter<Listener> {
    protected Set<Listener> listeners = new LinkedHashSet<>();

    public synchronized void addListener(Listener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(Listener listener) {
        listeners.remove(listener);
    }
}
