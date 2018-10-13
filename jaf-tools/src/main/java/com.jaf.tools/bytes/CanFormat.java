package com.jaf.tools.bytes;

public class CanFormat {
    int sccan = 0;
    int id =0x001;
    int msb=0x001;
    int lsb=0x001;

    public CanFormat() {
    }
    public CanFormat(int id, int msb, int lsb) {
        this.id = id;
        this.msb = msb;
        this.lsb = lsb;
    }

    int getVal(){
        int val = 0;
        val = val| sccan<<29;
        val = val| id<<18;
        val = val| msb<<9;
        val = val| lsb;
        return val;
    }

    public void parseVal(int i) {
        sccan = (i &0x20000000)>>>29;
        id =  (i &0x1ffc0000)>>>18;
        msb =  (i &0x0003fe00)>>>9;
        lsb =  i &0x000001ff;
    }

    public int getSccan() {
        return sccan;
    }

    public void setSccan(int sccan) {
        this.sccan = sccan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsb() {
        return msb;
    }

    public void setMsb(int msb) {
        this.msb = msb;
    }

    public int getLsb() {
        return lsb;
    }

    public void setLsb(int lsb) {
        this.lsb = lsb;
    }

    public static void main(String[] args) {
        CanFormat format = new CanFormat(0x754, 7, 0);
        System.out.println("getVal=" + format.getVal());
        System.out.println("binary= " + Integer.toString(format.getVal(), 2));
        System.out.println("binarySize= " + Integer.toString(format.getVal(), 2).length());


        int num = 1;
        int s = num | 2;
        System.out.println("s=" + s);

//        format.parseVal(1);
//        int id = 0xfffffff;
//        int hex2 = 01010101010;
//        int hex10 = 1111111111;
//        System.out.println("id="  + Integer.toBinaryString(id));
//        System.out.println("id="  + id);
//        System.out.println("id=" + Integer.toString(id, 10));
//        System.out.println("hex2=" + Integer.toString(hex2, 10));
//        System.out.println("hex2=" + Integer.toString(hex2, 2));

        int i = 7;
        System.out.println("8hex=" + Integer.toString(i, 8));
        System.out.println("binary=" + Integer.toBinaryString(i));
//

        System.out.println("format=" + format.getId());

        int j = 0x0011;
        System.out.println("j=" + j);
    }
}
