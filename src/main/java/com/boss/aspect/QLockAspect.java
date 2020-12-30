package com.boss.aspect;

import com.boss.annotation.QLock;
import com.boss.factory.LockFactory;
import com.boss.lock.QscLock;
import com.boss.model.QLockInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Classname QLockAspect
 * @Description 加锁逻辑
 * @author:yangmeng
 * @Date:2020/12/30 13:45
 */
@Component
@Aspect
@Order(0)
public class QLockAspect {
    private Logger logger = LoggerFactory.getLogger(QLockAspect.class);

    private ThreadLocal<QscLock> qscLockThreadLocal = new ThreadLocal<>();

    @Around("@annotation(com.boss.annotation.QLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method realMethod = this.getMethod(joinPoint);
        QLock lock = (QLock)realMethod.getAnnotation(QLock.class);
        QLockInfo lockInfo = QLockInfo.builder()
                .lockType(lock.lockType())
                .name(lock.name())
                .leaseTime(lock.leaseTime())
                .waitTime(lock.waitTime())
                .timeUnit(lock.timeUnit())
                .build();
        QscLock lockService = LockFactory.getLock(lockInfo);
        lockService.setLockInfo(lockInfo);
        qscLockThreadLocal.set(lockService);
        lockService.lock();
        return joinPoint.proceed();
    }

    @AfterReturning(value = "@annotation(com.boss.annotation.QLock)")
    public void afterReturning(JoinPoint joinPoint) {
        this.qscLockThreadLocal.get().release();
    }

    @AfterThrowing(value = "@annotation(com.boss.annotation.QLock)", throwing = "ex")
    public void afterThrowing (JoinPoint joinPoint, Throwable ex) throws Throwable {
        this.qscLockThreadLocal.get().release();
        throw ex;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getMethod(signature.getName(), method.getParameterTypes());
            } catch (SecurityException var5) {
                if (logger.isDebugEnabled()) {
                    logger.error("lockPoint getMethod", var5);
                }
            } catch (NoSuchMethodException var6) {
                if (logger.isDebugEnabled()) {
                    logger.error("lockPoint getMethod", var6);
                }
            }
        }

        return method;
    }
}
