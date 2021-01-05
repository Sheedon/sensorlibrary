package com.yanhangtec.sensorlibrary.serial.rs485.converters;

import android.annotation.SuppressLint;

import com.yanhangtec.sensorlibrary.utils.CharsUtils;

import org.sheedon.serial.retrofit.Converter;
import org.sheedon.serial.retrofit.SerialMessage;

/**
 * 请求校验码转化器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/20 17:12
 */
public final class RequestParityBitConverter implements Converter<SerialMessage, String> {


    @SuppressLint("DefaultLocale")
    @Override
    public String convert(SerialMessage value) {
        if (value == null)
            return "";

        if (value.getParityBit() != null && !value.getParityBit().equals("")) {
            return value.getParityBit().toUpperCase();
        }

        if (value.getMessageBit() == null || value.getMessageBit().isEmpty()) {
            return "";
        }

        String contentStr = value.getMessageBit();
        if (contentStr.length() % 2 == 1) {
            contentStr += "0";
        }

        byte[] bytes = CharsUtils.hexStringToBytes(contentStr);

        return CharsUtils.byteToHex(sumCheck(bytes));
    }

    /**
     * 求校验和的算法
     *
     * @param msg 需要求校验和的字节数组
     * @return 校验和
     */
    private byte sumCheck(byte[] msg) {
        char checksum;
        checksum = 0;
        for (byte b : msg) {
            checksum ^= b; //异或
        }
        return (byte) ~checksum; //按位取反
    }

}
