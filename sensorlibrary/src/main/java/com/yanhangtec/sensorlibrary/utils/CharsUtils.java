package com.yanhangtec.sensorlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheedon on 2017/9/28.
 */

public class CharsUtils {
    /**
     * byte转hexString
     *
     * @param buffer 数据
     * @param size   字符数
     * @return
     */
    public static String bytesToHexString(final byte[] buffer, final int size) {
        StringBuilder stringBuilder = new StringBuilder();
        if (buffer == null || size <= 0) return null;
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(buffer[i] & 0xff);
            if (hex.length() < 2) stringBuilder.append(0);
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    /**
     * hexString转byte
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字符串转换为ASCII码
     */
    public static String getAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (byte b : bs) sb.append(toHex(b));
        return sb.toString();
    }

    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return toHexUtil(n);
        } else {
            String t = toHex(n / 16);
            int nn = n % 16;
            sb.append(t).append(toHexUtil(nn));
        }
        return sb.toString();
    }

    private static String toHexUtil(int n) {
        String rt = "";
        switch (n) {
            case 10:
                rt += "A";
                break;
            case 11:
                rt += "B";
                break;
            case 12:
                rt += "C";
                break;
            case 13:
                rt += "D";
                break;
            case 14:
                rt += "E";
                break;
            case 15:
                rt += "F";
                break;
            default:
                rt += n;
        }
        return rt;
    }

    /**
     * 十六进制转换字符串
     *
     * @param hexStr
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp;
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            stmp = Integer.toHexString(value & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    public static String int2HexStr(int num) {
        String stmp = toHex(num);
        stmp = (stmp.length() % 2 == 1) ? "0" + stmp : stmp;
        return stmp;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @return String
     */
    public static String byte2HexStr2(byte[] b) {
        String stmp;
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            stmp = Integer.toHexString(value);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isNumber(char str) {
        return CharsUtils.isNumeric(String.valueOf(str));
    }

    /**
     * bytes转换成字符串
     */
    public static String byte2Str(byte[] b) {
        return hexStr2Str(byte2HexStr(b).replace(" ", ""));
    }

    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    /**
     * 求校验和的算法
     *
     * @param msg 需要求校验和的字节数组
     * @return 校验和
     */
    public static byte sumCheck(byte[] msg) {
        long sum = 0;

        /** 逐Byte添加位数和 */
        for (byte byteMsg : msg) {
            if (byteMsg < 0) {
                sum += byteMsg & 0xff;
            } else {
                sum += byteMsg;
            }

            if (sum > 0xff) {
                sum &= 0xff;
            }
        }

        return (byte) (sum & 0xff);
    }

    public static String sumCheckToHexStr(byte[] msg) {
        byte b = sumCheck(msg);
        return byteToHex(b);
    }

    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex.toUpperCase().trim();
    }

    public static int byteToHexInteger(byte b) {
        if (b < 0)
            return b & 0xff;
        return b;
    }

    public static byte[] getSendBuf(byte[] bb) {
        int ri = alex_crc16(bb, bb.length);
        return new byte[]{(byte) (0xff & ri), (byte) ((0xff00 & ri) >> 8)};
    }

    private static int alex_crc16(byte[] buf, int len) {
        int i, j;
        int c, crc = 0xFFFF;
        for (i = 0; i < len; i++) {
            c = buf[i] & 0x00FF;
            crc ^= c;
            for (j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else
                    crc >>= 1;
            }
        }
        return (crc);
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
