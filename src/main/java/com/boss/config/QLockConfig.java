package com.boss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Classname QLockConfig
 * @Description TODO
 * @author:yangmeng
 * @Date:2020/12/29 18:04
 */
@ConfigurationProperties(prefix = QLockConfig.PREFIX)
@Data
public class QLockConfig {
    public static final String PREFIX = "qlock.config";

    private String address;

    private String password;

    private int database = 0;

    private long waitTime = 60;

    private long leaseTime = 60;

    private String clusterPattern;
}
