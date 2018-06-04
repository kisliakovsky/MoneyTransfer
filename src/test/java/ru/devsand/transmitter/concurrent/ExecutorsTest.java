package ru.devsand.transmitter.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ExecutorsTest {

    @Test
    public void checkNewFixedThreadPool() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool("greeter");
        Future<Boolean> resultFuture = executorService.submit(
                () -> Thread.currentThread().getName().startsWith("greeter"));
        assertThat(resultFuture.get(), is(true));
        executorService.shutdownNow();
    }

}