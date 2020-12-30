package com.boss.model;

import com.boss.enums.QLockTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @Classname QLockInfo
 * @Description 锁信息
 * @author:yangmeng
 * @Date:2020/12/29 17:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QLockInfo {
    private QLockTypeEnum lockType;
    private String name;
    private long leaseTime;
    private long waitTime;
    private TimeUnit timeUnit;
}
