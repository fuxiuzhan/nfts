package com.fxz.fts.Test;

import java.util.concurrent.CountDownLatch;
/*

 */
public class TestClass extends Thread {
    private CountDownLatch counter = new CountDownLatch(1);

    @Override
    public void run() {
        counter.countDown();
        System.out.println("Thread Starting....");
    }
}
