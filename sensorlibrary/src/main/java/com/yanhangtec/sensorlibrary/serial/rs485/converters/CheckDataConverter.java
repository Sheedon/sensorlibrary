package com.yanhangtec.sensorlibrary.serial.rs485.converters;

import org.sheedon.serial.DataCheckBean;
import org.sheedon.serial.DataConverter;
import org.sheedon.serial.ResponseBody;
import org.sheedon.serial.SafetyByteBuffer;

import java.util.Arrays;

/**
 * 数据校验转化器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/3/11 0:45
 */
public class CheckDataConverter implements DataConverter<SafetyByteBuffer, DataCheckBean> {

    private static final byte[] STARTBIT = new byte[]{};
    private static final byte[] ENDBIT = new byte[]{};
    private static final byte[] COMMAND_03 = new byte[]{0x03};
    private static final byte[] COMMAND_04 = new byte[]{0x04};

    CheckDataConverter() {

    }


    // 数据格式
    // 命令类型     包长度     命令     地址       状态      参数与数据       校验和
    // 0x04        0x0C     0x02     0x20      0x00                      X
    @Override
    public DataCheckBean convert(SafetyByteBuffer value) {
        if (value == null || value.length() == 0) {
            return DataCheckBean.build(null, 0);
        }

        int cmd03 = value.indexOf(COMMAND_03[0]);
        int cmd04 = value.indexOf(COMMAND_04[0]);
        cmd03 = cmd03 == -1 ? Integer.MAX_VALUE : cmd03;
        cmd04 = cmd04 == -1 ? Integer.MAX_VALUE : cmd04;

        int index = Math.min(cmd03, cmd04);

        if (index + 2 >= value.length()) {
            return DataCheckBean.build(null, index);
        }

        // 一个内容到总长度
        byte[] lengthStr = value.substring(index + 1, index + 2);
        int length = calcLength(lengthStr);
        if (length < 0 || index + length > value.length()) {
            return DataCheckBean.build(null, index);
        }

        byte[] content = value.substring(index, index + length);
        boolean check = checkContent(content);
        if (check) {

            byte[] messageBit = Arrays.copyOf(content, content.length - 1);
            byte[] parityBit = Arrays.copyOfRange(content, content.length - 1, content.length);

            ResponseBody body = ResponseBody.build(STARTBIT, messageBit, parityBit, ENDBIT,
                    value.substring(index, index + length));

            return DataCheckBean.build(body, index + length);
        } else {
            return DataCheckBean.build(null, index + length);
        }
    }

    /**
     * 获取总长度
     *
     * @param str 字符
     */
    private int calcLength(byte[] str) {
        byte position = str[0];

        return position & 0xFF;

    }


    /**
     * 核实内容校验码
     * 拿到校验码 后两位
     * 拿到内容 除了后两位外的数据
     *
     * @param content 内容
     * @return 校验是否一致
     */
    private boolean checkContent(byte[] content) {
        if (content.length <= 1)
            return false;

        byte checkByte = content[content.length - 1];
        byte[] contentBytes = Arrays.copyOf(content, content.length - 1);
        byte checkResult = sumCheck(contentBytes);

        return checkByte == checkResult;
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