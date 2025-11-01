package com.diego.mediateca.services;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CodigoInternoGenerator {
    private CodigoInternoGenerator() {}

    private static final Map<String, AtomicInteger> PREFIX_COUNTER = new ConcurrentHashMap<>();

    public static String next(String prefix) {
        var counter = PREFIX_COUNTER.computeIfAbsent(prefix, p -> new AtomicInteger(0));
        int n = counter.incrementAndGet();
        return "%s%05d".formatted(prefix, n);
    }
}
