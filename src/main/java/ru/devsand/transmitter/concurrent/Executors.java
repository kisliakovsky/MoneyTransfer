package ru.devsand.transmitter.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

public class Executors {

    // Suppress default constructor for noninstantiability
    private Executors() {
        throw new AssertionError();
    }

    public static ExecutorService newFixedThreadPool(String threadRole) {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ThreadFactory threadFactory = new NamedThreadFactory(threadRole);
        return java.util.concurrent.Executors.newFixedThreadPool(numberOfThreads, threadFactory);
    }

}
