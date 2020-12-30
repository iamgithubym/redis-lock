package com.boss.annotation;

import com.boss.enums.QLockTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 锁注解
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface QLock {
    /**
     * 锁名称
     * @return
     */
    String name() default "";

    /**
     * 锁类型
     * @return
     */
    QLockTypeEnum lockType() default QLockTypeEnum.Fair;

    /**
     * 获取锁的等待时间
     * @return
     */
    long waitTime() default 5L;

    /**
     * 锁释放时间
     * @return
     */
    long leaseTime() default 5L;

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
