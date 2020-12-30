package com.boss.config;

import com.boss.aspect.QLockAspect;
import com.boss.enums.RedisClusterEnum;
import com.boss.factory.LockFactory;
import com.boss.lock.FairLock;
import com.boss.lock.ReadLock;
import com.boss.lock.ReentrantLock;
import com.boss.lock.WriteLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

/**
 * @Classname QLockAutoConfiguration
 * @Description 自动装配
 * @author:yangmeng
 * @Date:2020/12/30 10:22
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(QLockConfig.class)
@Import({QLockAspect.class})
public class QLockAutoConfiguration {

    @Autowired
    private QLockConfig lockConfig;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(){
        Config config = new Config();
        if(RedisClusterEnum.CLUSTER.getType().equals(lockConfig.getClusterPattern())){
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            this.initClusterConfig(clusterServersConfig);
        }

        if(RedisClusterEnum.REPLICATED.getType().equals(lockConfig.getClusterPattern())){
            ReplicatedServersConfig replicatedServersConfig = config.useReplicatedServers();
            this.initReplicatedServerConfig(replicatedServersConfig);
        }

        if(RedisClusterEnum.MASTER_SLAVE.getType().equals(lockConfig.getClusterPattern())){
            MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
            this.initMasterSlaveServersConfig(masterSlaveServersConfig);
        }

        if(RedisClusterEnum.SENTINEL.getType().equals(lockConfig.getClusterPattern())){
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            this.initSentinelServersConfig(sentinelServersConfig);
        }

        if(RedisClusterEnum.SINGLE.getType().equals(lockConfig.getClusterPattern())){
            SingleServerConfig singleServerConfig = config.useSingleServer();
            this.initSingleServerConfig(singleServerConfig);
        }

        return Redisson.create(config);
    }

    private void initSingleServerConfig(SingleServerConfig singleServerConfig) {
        singleServerConfig.setAddress(String.format("%s%s", "redis://", lockConfig.getAddress())).setPassword(lockConfig.getPassword())
                .setDatabase(lockConfig.getDatabase());
    }

    private void initSentinelServersConfig(SentinelServersConfig sentinelServersConfig) {
        String[] addressArr = lockConfig.getAddress().split(",");
        Arrays.asList(addressArr).forEach((address) -> sentinelServersConfig.addSentinelAddress(new String[]{String.format("%s%s", "redis://", address)}));
        sentinelServersConfig.setPassword(lockConfig.getPassword());
    }

    private void initMasterSlaveServersConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
        throw new RuntimeException("暂不支持主从模式");
    }

    private void initReplicatedServerConfig(ReplicatedServersConfig replicatedServersConfig) {
        throw new RuntimeException("暂不支持云托管模式");
    }

    private void initClusterConfig(ClusterServersConfig clusterServersConfig) {
        String[] addressArr = lockConfig.getAddress().split(",");
        Arrays.asList(addressArr).forEach((address) -> clusterServersConfig.addNodeAddress(new String[]{String.format("%s%s", "redis://", address)}));
        clusterServersConfig.setPassword(lockConfig.getPassword());
    }

    @Bean
    public LockFactory lockFactory(){
        return new LockFactory();
    }

    @Bean
    public FairLock fairLock(){
        return new FairLock();
    }

    @Bean
    public ReadLock readLock(){
        return new ReadLock();
    }

    @Bean
    public WriteLock writeLock(){
        return new WriteLock();
    }

    @Bean
    public ReentrantLock reentrantLock(){
        return new ReentrantLock();
    }
}
