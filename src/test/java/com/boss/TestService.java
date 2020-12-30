package com.boss;

import com.boss.annotation.QLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by kl on 2017/12/29.
 */
@Service
@Slf4j
public class TestService {

    private int count = 0;

    @QLock(waitTime = 10,leaseTime = 60,name = "lock1")
    public String getValue(String param) throws Exception {
      //  if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
            //Thread.sleep(1000*3);
        //}
        count = count + 1;
        log.info(count+"======================"+Thread.currentThread().getName());
        return "success";
    }

    public int getCount(){
        return count;
    }

    @QLock(name = "lock1")
    public String getValue(String userId, Integer id)throws Exception{
        Thread.sleep(60*1000);
        return "success";
    }

    @QLock(name = "lock1")
    public String getValue(User user)throws Exception{
        Thread.sleep(60*1000);
        return "success";
    }

}
