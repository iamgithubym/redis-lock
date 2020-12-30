package com.boss;

import com.boss.annotation.QLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wanglaomo
 * @since 2019/4/16
 **/
@Service
public class TimeoutService {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutService.class);

    @QLock(name="foo-service", leaseTime=-1)
    public void foo1() {
        try {
            logger.info("foo1 acquire lock");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @QLock(name="foo-service", waitTime=2)
    public void foo2() {
        try {
            logger.info("acquire lock");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @QLock(name="foo-service", waitTime=2)
    public void foo3() {
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info("acquire lock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @QLock(name="foo-service",
            waitTime=2)
    public String foo4(String foo, String bar) {
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info("acquire lock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "foo4";
    }

    private String customLockTimeout(String foo, String bar) {

        logger.info("customLockTimeout foo: " + foo + " bar: " + bar);
        return "custom foo: " + foo + " bar: " + bar;
    }


    @QLock(name="foo-service", waitTime=10)
    public void foo5(String foo, String bar) {
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info("acquire lock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @QLock(name="foo-service", leaseTime=10, waitTime = 10)
    public void foo6(String foo, String bar) {
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info("acquire lock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @QLock(name="foo-service", leaseTime=1, waitTime = 10)
    public void foo7(String foo, String bar) {
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info("acquire lock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @QLock(name="foo-service", leaseTime=1, waitTime = 10)
    public String foo8(String foo, String bar) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "foo8";
    }

    private String customReleaseTimeout(String foo, String bar) {

        throw new IllegalStateException("customReleaseTimeout");
    }
}
