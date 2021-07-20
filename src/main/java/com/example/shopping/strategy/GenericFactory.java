package com.example.shopping.strategy;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GenericFactory<K, V extends GenericStrategy<K>>{
    private final List<V> strategies;
    private Map<K, V> strategiesMap;

    @PostConstruct
    void init(){
        strategiesMap = strategies.stream().collect(Collectors.toMap(GenericStrategy::getType, Function.identity()));
    }

    public V findStrategyByKey(K key){
        return strategiesMap.get(key);
    }
}