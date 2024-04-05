package ru.otus.cache;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(l->l.notify(key, value, "PUT"));
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        listeners.forEach(l->l.notify(key, null, "REMOVE"));
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        listeners.forEach(l->l.notify(key, value, "GET"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
