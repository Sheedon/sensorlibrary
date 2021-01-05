package com.yanhangtec.sensorlibrary.model;

import org.sheedon.serial.retrofit.serialport.RULES;

/**
 * 工作模式
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 1:30 PM
 */
public class OperatingModel {

    @RULES(end = 1)
    private byte[] packageType;// 包类型
    @RULES(begin = 1, end = 2)
    private byte[] length;// 包长度
    @RULES(begin = 2, end = 3)
    private byte[] command;// 命令
    @RULES(begin = 3, end = 4)
    private byte[] address;// 地址
    @RULES(begin = 4, end = 5)
    private byte[] status;// 状态
    @RULES(begin = 5, end = 7)
    private byte[] other;// 保留


    public boolean getDecodeStatus() {
        if (status == null || status.length == 0) {
            return false;
        }
        return status[0] == 0;
    }
}
