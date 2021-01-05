package com.yanhangtec.sensorlibrary.model;

import org.sheedon.serial.internal.CharsUtils;
import org.sheedon.serial.retrofit.serialport.RULES;

/**
 * RS485读卡结果
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/14/20 1:53 PM
 */
public class RSModel {

    @RULES(end = 1)
    private byte[] commandType;// 命令类型
    @RULES(begin = 1, end = 2)
    private byte[] length;// 包长度
    @RULES(begin = 2, end = 3)
    private byte[] command;// 命令
    @RULES(begin = 3, end = 4)
    private byte[] address;// 地址
    @RULES(begin = 4, end = 5)
    private byte[] status;// 状态
    @RULES(begin = 5, end = 7)
    private byte[] cardType;// 卡片类型
    @RULES(begin = 7, end = 11)
    private byte[] data;// 数据信息

    /**
     * 获取标签编号
     */
    public String getDecodeLabelNumber() {
        if (data == null || data.length == 0)
            return "";
        return CharsUtils.byte2HexStrNotEmpty(data);
    }
}
