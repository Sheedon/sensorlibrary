package com.yanhangtec.sensorlibrary.client;


import com.yanhangtec.sensorlibrary.client.center.CardReaderCenter;
import com.yanhangtec.sensorlibrary.client.listener.OnCardReaderListener;
import com.yanhangtec.sensorlibrary.client.listener.OnDebugListener;
import com.yanhangtec.sensorlibrary.helper.RSHelper;
import com.yanhangtec.sensorlibrary.model.DataSource;
import com.yanhangtec.sensorlibrary.model.OperatingModel;
import com.yanhangtec.sensorlibrary.model.RSModel;
import com.yanhangtec.sensorlibrary.utils.TimerUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RS485客户端
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 2:03 PM
 */
class RSClient extends BaseClient<OnCardReaderListener>
        implements DataSource.Callback<RSModel>,
        TimerUtils.OnTimeListener,
        CardReaderCenter<OnCardReaderListener> {

    private static volatile RSClient instance;

    private OnDebugListener debugListener;

    private TimerUtils rsTimer;

    private String lastRsNum = "";
    private AtomicBoolean isNormal = new AtomicBoolean(false);

    private long lastTime;

    public static RSClient getInstance() {
        if (instance == null) {
            synchronized (RSClient.class) {
                if (instance == null) {
                    instance = new RSClient();
                }
            }
        }
        return instance;
    }

    private RSClient() {

    }

    @Override
    public void initConfig() {
        RSHelper.bindRSResult(this);
        startTimer();
        lastRsNum = "";
    }

    private void startTimer() {
        createTimer();

        if (rsTimer.isRunning())
            return;

        rsTimer.startTime(10, 10, Integer.MAX_VALUE);
    }

    private void createTimer() {
        if (rsTimer == null) {
            rsTimer = new TimerUtils("rs_timer", this);
        }
    }

    @Override
    public String getCurrentCard() {
        return lastRsNum;
    }

    @Override
    public boolean isNormal() {
        return isNormal.get();
    }

    @Override
    public void bindDebug(OnDebugListener listener) {
        debugListener = listener;
    }

    @Override
    public void onDataLoaded(RSModel rsModel) {
        syncTime();
        if (rsModel == null)
            return;

        noticeRFIDLbl(rsModel.getDecodeLabelNumber());
        noticeRFIDIsNormal(true);
    }

    @Override
    public void onDataNotAvailable(String message) {

    }

    @Override
    public void onTimeBack(Long aLong) {
        long nowTimer = System.currentTimeMillis();
        if (nowTimer - lastTime >= 1500) {
            RSHelper.setAutoReadCard(voidCallback);
            noticeRFIDIsNormal(false);
        }

    }

    @Override
    public void onComplete(TimerUtils timerUtils) {

    }

    /**
     * 通知分发RFID编号
     */
    private void noticeRFIDLbl(String rfidNum) {
        for (OnCardReaderListener listener : listeners) {
            if (listener != null) {
                lastRsNum = rfidNum;
                listener.onCardInfo(rfidNum, false);
            }
        }
    }

    /**
     * 通知是否正常
     *
     * @param isNormal 是否正常
     */
    private void noticeRFIDIsNormal(boolean isNormal) {
        if (this.isNormal.get() != isNormal) {
            for (OnCardReaderListener listener : listeners) {
                if (listener != null) {
                    this.isNormal.set(isNormal);
                    listener.onCardIsNormal(isNormal);
                }
            }
        }


        sendDebug(!isNormal);
    }

    private void syncTime() {
        lastTime = System.currentTimeMillis();
    }

    private void sendDebug(boolean isStartDebug) {
        if (debugListener == null)
            return;

        debugListener.onDebug(isStartDebug);
    }


    private DataSource.Callback<OperatingModel> voidCallback = new DataSource.Callback<OperatingModel>() {

        @Override
        public void onDataNotAvailable(String message) {
        }

        @Override
        public void onDataLoaded(OperatingModel model) {
            if (model != null && model.getDecodeStatus()) {
                syncTime();
                noticeRFIDIsNormal(true);

                if (rsTimer != null && rsTimer.isRunning()) {
                    rsTimer.closeTimer();
                }
            }
        }
    };

}
