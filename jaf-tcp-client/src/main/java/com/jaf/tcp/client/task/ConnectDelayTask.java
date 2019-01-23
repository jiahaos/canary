package com.jaf.tcp.client.task;

public class ConnectDelayTask extends AbstactDelayTask{

    private Runnable task;

    public ConnectDelayTask(long dms, int index) {
        super(index * Integer.parseInt(System.getProperty("connect.delay", "100")));
        task = () -> {

        };
    }

    @Override
    public Runnable getTask() {
        return this.task;
    }
}
