package com.jaf.tcp.client.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public interface DelayTask extends Delayed {

    Runnable getTask();

    @Override
    default int compareTo(Delayed o) {
        long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : (d < 0) ? -1 : 1;
    }
}
