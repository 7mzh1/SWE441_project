package org.jhotdraw8.styleable;

import javafx.css.StyleOrigin;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;

import java.util.AbstractMap;
import java.util.Set;

class SimpleStyleableMapProxy<K, V> extends AbstractMap<K, V> {
    @NonNull
    private final SimpleStyleableMap<K, V> target;
    @Nullable

    private final StyleOrigin origin;
    private final int originOrdinal;

    public SimpleStyleableMapProxy(@NonNull SimpleStyleableMap<K, V> target, @Nullable StyleOrigin origin) {
        this.target = target;
        this.originOrdinal = origin == null ? SimpleStyleableMap.AUTO_ORIGIN : origin.ordinal();
        this.origin = origin;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean b = target.containsKey(origin, (K) key);
        boolean sb = super.containsKey(key);
        if (sb != b) {
            System.err.println(entrySet());
            for (Entry<K, V> kvEntry : entrySet()) {
                System.err.println(kvEntry.getKey() + "=" + kvEntry.getValue() + " -> b:" + b + " sb:" + sb);
            }

            throw new AssertionError("buhu");
        }
        return b;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return target.getOrDefault(originOrdinal, (K) key, defaultValue);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return target.entrySet(origin);
    }

    @Override
    public V get(Object key) {
        return target.getOrDefault(originOrdinal, (K) key, null);
    }

    @Override
    public V put(K key, V value) {
        return target.put(originOrdinal, key, value);
    }

    @Override
    public V remove(Object key) {
        if (origin == null) {
            return null;
        } else {
            return target.removeKey(origin, (K) key);
        }
    }

    @Override
    public int size() {
        return target.size(origin);
    }
}