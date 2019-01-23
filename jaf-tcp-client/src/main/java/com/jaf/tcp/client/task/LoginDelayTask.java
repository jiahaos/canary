package com.jaf.tcp.client.task;

import java.nio.channels.Channel;
import java.util.concurrent.Delayed;

public class LoginDelayTask extends AbstactDelayTask{

    private Runnable task;

    public LoginDelayTask(Channel channel, String startKey) {
        super(random.nextInt(500));
        task = () -> {


        };


    }

    @Override
    public Runnable getTask() {
        return this.task;
    }

}
