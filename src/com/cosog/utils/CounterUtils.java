package com.cosog.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

public class CounterUtils {
	public static LongAdder count = new LongAdder();
	public static CountDownLatch countDownLatch;
    public static void incr() {
        count.increment();
    }
    public static void reset() {
    	count.reset();
    }
    public static long sum(){
    	return count.sum();
    }
    
    public static void initCountDownLatch(int count){
    	countDownLatch=new CountDownLatch(count);
    }
    
    public static void countDown(){
    	countDownLatch.countDown();
    }
    
    public static void await() throws InterruptedException{
    	countDownLatch.await();
    }
}
