package com.boss.enums;

import lombok.Getter;

@Getter
public enum RedisClusterEnum {
    CLUSTER("cluster"),
    REPLICATED("replicated"),
    SENTINEL("sentinel"),
    MASTER_SLAVE("master_slave"),
    SINGLE("single");

    private String type;

    RedisClusterEnum(String type) {
        this.type = type;
    }
}
