package com.jaf.tcp.client.task;

public class HeadBeatDelayTask extends AbstactDelayTask{

    private Runnable task;


    public HeadBeatDelayTask(long dms) {
        super(random.nextInt(10000) + 25000);
        task = () -> {

        };

    }

    @Override
    public Runnable getTask() {
        return this.task;
    }
}
