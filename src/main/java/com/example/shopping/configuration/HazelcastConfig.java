package com.example.shopping.configuration;

import com.example.shopping.entity.Product;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {
    @Bean
    Config configHazelcast() {
        var config = new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig()
                        .setName("product")
                        .setEvictionConfig(new EvictionConfig()
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setSize(10)
                                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE))
                        .setTimeToLiveSeconds(60 * 60 * 24));

        config.getSerializationConfig().addDataSerializableFactory
                (1, (int id) -> (id == 1) ? (IdentifiedDataSerializable) new Product() : null);
        return config;
    }
}
