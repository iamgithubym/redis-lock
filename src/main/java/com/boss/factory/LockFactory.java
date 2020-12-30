package com.boss.factory;

import com.boss.lock.QscLock;
import com.boss.model.QLockInfo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname LockFactory
 * @Description TODO
 * @author:yangmeng
 * @Date:2020/12/29 20:14
 */
public class LockFactory implements ApplicationContextAware {

    private static Map<String, QscLock> lockMap = new HashMap<>(4);

    public static QscLock getLock(QLockInfo lockInfo){
        return lockMap.get(lockInfo.getLockType().getType());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, QscLock> map = applicationContext.getBeansOfType(QscLock.class);
        map.forEach((key, value) -> lockMap.put(value.lockType(), value));
    }
}
