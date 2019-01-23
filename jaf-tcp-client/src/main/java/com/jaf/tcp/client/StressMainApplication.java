package com.jaf.tcp.client;

public class StressMainApplication {

    public static void main(String[] args) {
        new TcpClient("127.0.0.1", 8990).start();
    }
}
