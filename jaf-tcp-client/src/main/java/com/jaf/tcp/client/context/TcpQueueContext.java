package com.jaf.tcp.client.context;

import com.jaf.tcp.client.task.DelayTask;

import java.util.concurrent.DelayQueue;

public class TcpQueueContext {

    private DelayQueue<DelayTask> delayQueue;

    public TcpQueueContext(){
        delayQueue = new DelayQueue<DelayTask>();
    }


    public DelayTask poll(){
        return delayQueue.poll();
    }

    public void put(DelayTask e){
        delayQueue.put(e);
    }


}
