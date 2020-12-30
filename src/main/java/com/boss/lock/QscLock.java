package com.boss.lock;

import com.boss.model.QLockInfo;

/**
 * @Description TODO
 * @author:yangmeng
 * @Date:2020/12/29 19:33
 */
public interface QscLock {

    void setLockInfo(QLockInfo lockInfo);

    String lockType();

    /**
     * ��ȡ��
     * @return
     */
    boolean lock();

    /**
     * �ͷ���
     * @return
     */
    boolean release();
}
