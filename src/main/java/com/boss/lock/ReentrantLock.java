package com.boss.lock;

import com.boss.enums.QLockTypeEnum;
import com.boss.model.QLockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname ReentrantLock
 * @Description TODO
 * @author:yangmeng
 * @Date:2020/12/29 19:57
 */
public class ReentrantLock implements QscLock {

    @Autowired
    private RedissonClient redissonClient;

    private RLock rLock;

    private QLockInfo lockInfo;

    @Override
    public void setLockInfo(QLockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    @Override
    public String lockType() {
        return QLockTypeEnum.Reentrant.getType();
    }

    /**
     * 加锁
     *
     * @return
     */
    @Override
    public boolean lock() {
        try {
            rLock = redissonClient.getLock(lockInfo.getName());
            this.rLock.tryLock(this.lockInfo.getWaitTime(), this.lockInfo.getLeaseTime(), this.lockInfo.getTimeUnit());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 释放锁
     *
     * @return
     */
    @Override
    public boolean release() {
        if(rLock.isHeldByCurrentThread()){
            try {
                rLock.unlock();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
