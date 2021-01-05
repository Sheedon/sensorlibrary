package com.yanhangtec.sensorlibrary.client;

import com.yanhangtec.sensorlibrary.client.center.CardReaderCenter;
import com.yanhangtec.sensorlibrary.client.listener.OnCardReaderListener;
import com.yanhangtec.sensorlibrary.client.listener.OnDebugListener;
import com.yanhangtec.sensorlibrary.helper.RFIDHelper;
import com.yanhangtec.sensorlibrary.model.DataSource;
import com.yanhangtec.sensorlibrary.model.RFIDModel;
import com.yanhangtec.sensorlibrary.utils.TimerUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RFID客户端
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/15 14:25
 */
class RFIDClient implements DataSource.Callback<RFIDModel>, TimerUtils.OnTimeListener,
        CardReaderCenter {

    private TimerUtils rfidTimer;

    private long lastTime = System.currentTimeMillis();

    protected Set<OnCardReaderListener> listeners = new LinkedHashSet<>();

    private OnDebugListener debugListener;

    // 上一次RFID编号
    private String lastRfidNum = "";
    private AtomicBoolean isNormal = new AtomicBoolean(false);

    public static RFIDClient getInstance() {
        return RFIDHolder.INSTANCE;
    }

    private static class RFIDHolder {
        private static final RFIDClient INSTANCE = new RFIDClient();
    }

    private RFIDClient() {

    }

    @Override
    public void initConfig() {
        RFIDHelper.bindCommandResult(voidCallback);
        RFIDHelper.bindRFIDResult(this);
        RFIDHelper.setSignalStrength();
        startTimer();
        lastRfidNum = "";
    }

    /**
     * 新增监听器
     */
    public synchronized void addListener(OnCardReaderListener listener) {
        listeners.add(listener);
    }

    /**
     * 移除监听器
     */
    public synchronized void removeListener(OnCardReaderListener listener) {
        listeners.remove(listener);
    }

    @Override
    public String getCurrentCard() {
        return lastRfidNum;
    }

    @Override
    public boolean isNormal() {
        return isNormal.get();
    }

    @Override
    public void bindDebug(OnDebugListener listener) {
        debugListener = listener;
    }

    private void startTimer() {
        createTimer();

        if (rfidTimer.isRunning())
            return;

        rfidTimer.startTime(2, 2, Integer.MAX_VALUE);
    }

    private void createTimer() {
        if (rfidTimer == null) {
            rfidTimer = new TimerUtils("rfid_timer", this);
        }
    }

    @Override
    public void onDataLoaded(RFIDModel rfidModel) {
        syncTime();
        if (rfidModel == null)
            return;

        noticeRFIDLbl(rfidModel.getDecodeLabelNumber());
        noticeRFIDIsNormal(true);

    }

    /**
     * 通知分发RFID编号
     */
    private void noticeRFIDLbl(String rfidNum) {
        for (OnCardReaderListener listener : listeners) {
            if (listener != null) {
                lastRfidNum = rfidNum;
                listener.onCardInfo(rfidNum, true);
            }
        }
    }

    /**
     * 通知是否正常
     *
     * @param isNormal 是否正常
     */
    private void noticeRFIDIsNormal(boolean isNormal) {
        for (OnCardReaderListener listener : listeners) {
            if (listener != null && this.isNormal.get() != isNormal) {
                this.isNormal.set(isNormal);
                listener.onCardIsNormal(isNormal);
            }
        }


        sendDebug(!isNormal);
    }

    @Override
    public void onDataNotAvailable(String message) {
    }

    private DataSource.Callback<Void> voidCallback = new DataSource.Callback<Void>() {

        private long lastTime;

        @Override
        public void onDataNotAvailable(String message) {
        }

        @Override
        public void onDataLoaded(Void aVoid) {
            syncTime();
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime < 1000)
                return;
            lastTime = nowTime;

            noticeRFIDIsNormal(true);
        }
    };


    @Override
    public void onTimeBack(Long aLong) {
        long nowTimer = System.currentTimeMillis();
        if (nowTimer - lastTime >= 1500) {
            RFIDHelper.sendContinuousRead();
            noticeRFIDIsNormal(false);
        }

        syncTime();
    }

    private void sendDebug(boolean isStartDebug) {
        if (debugListener == null)
            return;

        debugListener.onDebug(isStartDebug);
    }

    private void syncTime() {
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onComplete(TimerUtils timerUtils) {
        startTimer();
    }
}
