package com.yanhangtec.sensorlibrary.serial.rs485.converters;

import com.yanhangtec.sensorlibrary.utils.CharsUtils;

import org.sheedon.serial.DataConverter;

/**
 * 反馈规则转换器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/3/11 0:45
 */
public class CallbackRuleConverter implements DataConverter<byte[], Long> {

    CallbackRuleConverter() {

    }

    // 数据格式
    // 命令类型     包长度     命令     地址       状态      参数与数据       校验和
    // 0x04        0x0C     0x02     0x20      0x00     0x00 0x00        X
    @Override
    public Long convert(byte[] value) {
        if (value == null || value.length < 3)
            return -1L;

        return (long) CharsUtils.byteToHexInteger(value[2]);
    }
}
