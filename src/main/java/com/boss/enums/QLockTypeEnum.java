package com.boss.enums;

import lombok.Getter;

/**
 * @author meng.yang
 */
@Getter
public enum QLockTypeEnum {

    Reentrant("Reentrant","可重入锁"),

    Fair("Fair","公平锁"),

    Read("Read","读锁"),

    Write("Write","写锁");

    private String type;
    private String desc;

    QLockTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
