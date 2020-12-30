package com.boss;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @Classname LockServiceApplicationTests
 * @Description TODO
 * @author:yangmeng
 * @Date:2020/12/30 15:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KlockTestApplication.class)
@Slf4j
public class LockServiceApplicationTests {
    @Autowired
    TestService testService;

    @Autowired
    TimeoutService timeoutService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void multithreadingTest()throws Exception{
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0,10).forEach(i-> executorService.submit(() -> {
            try {
                log.error("线程:[" + Thread.currentThread().getName() + "]开始等待=》 "+ new Date().toLocaleString());
                cyclicBarrier.await();
                testService.getValue("sleep");
                int count = testService.getCount();
                log.error("线程:[" + Thread.currentThread().getName() + "]拿到结果=》" + count +" "+ new Date().toLocaleString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        //countDownLatch.await();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
}
