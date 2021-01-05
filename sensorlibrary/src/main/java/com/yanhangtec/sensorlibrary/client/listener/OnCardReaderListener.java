package com.yanhangtec.sensorlibrary.client.listener;

/**
 * 读卡器监听器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 2:19 PM
 */
public interface OnCardReaderListener {
    void onCardInfo(String lblNum, boolean isContinuousSend);

    void onCardIsNormal(boolean isNormal);
}
