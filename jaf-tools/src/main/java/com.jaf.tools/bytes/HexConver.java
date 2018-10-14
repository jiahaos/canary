package com.jaf.tools.bytes;

public class HexConver
{
    public static void main(String[] args)
    {
        String content = "这是一个测试";
        System.out.println("原字符串："+content);
        String hex2Str = conver2HexStr(content.getBytes());
        System.out.println("\n转换为二进制的表示形式："+hex2Str);
        byte [] b = conver2HexToByte(hex2Str);
        System.out.println("二进制字符串还原："+new String(b));

        String hex16Str = conver16HexStr(content.getBytes());
        System.out.println("\n转换为十六进制的表示形式:"+ hex16Str);
        System.out.println("十六进制字符串还原:"+ new String(conver16HexToByte(hex16Str)));
    }

    /**
     * byte数组转换为二进制字符串,每个字节以","隔开
     * **/
    public static String conver2HexStr(byte [] b)
    {
        StringBuffer result = new StringBuffer();
        for(int i = 0;i<b.length;i++)
        {
            result.append(Long.toString(b[i] & 0xff, 2)+",");
        }
        return result.toString().substring(0, result.length()-1);
    }

    /**
     * 二进制字符串转换为byte数组,每个字节以","隔开
     * **/
    public static byte[] conver2HexToByte(String hex2Str)
    {
        String [] temp = hex2Str.split(",");
        byte [] b = new byte[temp.length];
        for(int i = 0;i<b.length;i++)
        {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }


    /**
     * byte数组转换为十六进制的字符串
     * **/
    public static String conver16HexStr(byte [] b)
    {
        StringBuffer result = new StringBuffer();
        for(int i = 0;i<b.length;i++)
        {
            if((b[i]&0xff)<0x10)
                result.append("0");
            result.append(Long.toString(b[i]&0xff, 16));
        }
        return result.toString().toUpperCase();
    }

    /**
     * 十六进制的字符串转换为byte数组
     * **/
    public static byte[] conver16HexToByte(String hex16Str)
    {
        char [] c = hex16Str.toCharArray();
        byte [] b = new byte[c.length/2];
        for(int i = 0;i<b.length;i++)
        {
            int pos = i * 2;
            b[i] = (byte)("0123456789ABCDEF".indexOf(c[pos]) << 4 | "0123456789ABCDEF".indexOf(c[pos+1]));
        }
        return b;
    }
}
