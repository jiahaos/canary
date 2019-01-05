package com.jaf.tools.bytes;

import org.apache.commons.codec.binary.Base64;
import sun.dc.pr.PRError;

public class ByteUtils {

    public static void main(String[] args) {
        String base64Str = "AAEABQAKDVAOAA1QHggNUC4QDVA+GA1QTiA=";
        String binaryStr = "0000000000000001000000000000010100000000000010100000110101010000000011100000000000001101010100000001111000001000000011010101000000101110000100000000110101010000001111100001100000001101010100000100111000100000";
        int tag = 1;
        int version = 1;
        //base64转bytes
        byte[] bytes = Base64.decodeBase64(base64Str);

        byte version1 = 127;
        byte[] bs = new byte[]{version1, version1};
        System.out.println(HexConver.conver2HexStr(bs));
        String hex16 = "1f";
        System.out.println(HexConver.conver2HexStr(HexConver.conver16HexToByte(hex16)));

        System.out.println("版本转换：" +HexConver.conver2HexStr(converStrategyVersion(tag, version)));


        System.out.println(bytes.length);
        System.out.println(Base64.encodeBase64String(bytes));
        System.out.println(Base64.isBase64(bytes));
        System.out.println(HexConver.conver2HexStr(bytes));
//        System.out.println(HexConver.conver2HexToByte(binaryStr));
        byte b = 0x35; // 0011 0101
        System.out.println(byteToBit(b));

        int byteSize = binaryStr.length()/8;
        System.out.println("byteSize=" + byteSize + "binaryStrSize=" + binaryStr.length());
        byte[] bytes1 = new byte[byteSize];
        for(int i = 0; i < byteSize;i++){
            int start = i*8;
            int end = i*8 + 8;
            String str = binaryStr.substring(start, end);
            byte one =  decodeBinaryString(str);
            bytes1[i] = one;
        }
        System.out.println(Base64.encodeBase64String(bytes1));
        String condition = "TA 181213154424433 89860918700302292633 2001 5.1 1062 7BE0 \n";

        System.out.printf(condition.getBytes().length + "");
        //
//
    }

    public static byte[] converStrategyVersion(int tag, int version) {
        //转换10进制版本
        int hex = 1 == tag ? 32768 + version : version;
        return int2bytes(hex);
    }

    public static byte[] int2bytes(int n) {
        int temp1 = 0,temp2=0;
        byte[] hex = new byte[2];
        if(n < 256) {
            hex[1] = (byte) n; }else{temp1 = n & 0xff;hex[1] = (byte)temp1; temp2 = n >> 8;hex[0] = (byte)temp2;}return hex;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 二进制字符串转byte
     */
    public static byte decodeBinaryString(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }
    /**	 * byte数组转换为二进制字符串,每个字节以","隔开	 * **/	public static String conver2HexStr(byte [] b)	{		StringBuffer result = new StringBuffer();		for(int i = 0;i<b.length;i++)		{			result.append(Long.toString(b[i] & 0xff, 2)+",");		}		return result.toString().substring(0, result.length()-1);	}

}
