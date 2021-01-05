package com.yanhangtec.sensorlibrary.utils;


import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TimerUtils {

    private Disposable mDisposable;
    private OnTimeListener listener;

    private Observer<Object> observer;
    private Observable<Long> observable;

    private String name;
    private boolean isRunning;

    public TimerUtils() {
        if (observer == null)
            observer = new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(Object o) {
                    if (listener != null) {
                        listener.onTimeBack((Long) o);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    if (listener != null) {
                        listener.onComplete(TimerUtils.this);
                    }
                    closeTimer();
                }
            };
    }

    public TimerUtils(OnTimeListener listener) {
        this();
        this.listener = listener;
    }

    public TimerUtils(String name) {
        this();
        this.name = name;
    }

    public TimerUtils(String name, OnTimeListener listener) {
        this(name);
        this.listener = listener;
    }

    public String getName() {
        return name;
    }

    public void setListener(OnTimeListener listener) {
        this.listener = listener;
    }

    /**
     * 启动定时器
     *
     * @param initialDelay
     * @param period
     * @param totalTime    总共次数
     */
    public void startTime(long initialDelay, long period, int totalTime) {
        if (totalTime < 0)
            return;

        isRunning = true;

        if (observable == null) {
            observable = Observable.interval(initialDelay, period, TimeUnit.SECONDS);
        }
        observable.interval(initialDelay, period, TimeUnit.SECONDS)
                .take(totalTime)
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 启动定时器
     *
     * @param initialDelay 延迟
     * @param period       暂停
     * @param totalTime    总共次数
     * @param unit         单位
     */
    public void startTime(long initialDelay, long period, int totalTime, TimeUnit unit) {
        if (totalTime < 0)
            return;

        isRunning = true;

        observable = Observable.interval(initialDelay, period, unit);

        observable.interval(initialDelay, period, unit)
                .take(totalTime)
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 启动定时器
     *
     * @param period
     * @param totalTime 总共次数
     */
    public void startMilliTime(long period, int totalTime) {
        if (totalTime < 0)
            return;

        isRunning = true;

        if (observable == null) {//毫秒
            observable = Observable.interval(0, period, TimeUnit.MILLISECONDS);
        }
        observable.interval(0, period, TimeUnit.MILLISECONDS)
                .take(totalTime)
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 启动定时器
     *
     * @param totalTime        总共次数
     * @param superimposedTime 叠加时间
     */
    public void startTime(int totalTime, final int superimposedTime) {
        if (totalTime < 0)
            return;
        isRunning = true;

        if (observable == null) {
            observable = Observable.interval(10, 10, TimeUnit.SECONDS);
        }
        observable.take(totalTime)
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) throws Exception {
                        return aLong + superimposedTime;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * 关闭定时器
     */
    public void closeTimer() {
        isRunning = false;
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public interface OnTimeListener {
        void onTimeBack(Long aLong);

        void onComplete(TimerUtils timerUtils);
    }

}
