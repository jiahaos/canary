package com.jaf.tcp.client.task;

import java.util.Random;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class AbstactDelayTask implements DelayTask{

    protected static final Random random = new Random();

    private final long exeTime;


    public AbstactDelayTask(long dms) {
        this.exeTime = System.nanoTime()
                + TimeUnit.NANOSECONDS.convert(dms, TimeUnit.MILLISECONDS);;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(exeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if(o == this)
            return 0;
        if(o instanceof AbstactDelayTask){
            AbstactDelayTask task = (AbstactDelayTask)o;
            long e = task.exeTime;
            return e == 0 ? 0 : (e < 0) ? -1 : 1;
        }
        long span = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return span > 0 ? 1 : (span == 0 ? 0 : -1);
    }
}
