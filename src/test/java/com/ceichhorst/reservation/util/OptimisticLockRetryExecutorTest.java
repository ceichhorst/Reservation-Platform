package com.ceichhorst.reservation.util;

import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticLockRetryExecutorTest {

    @Test
    void testExecute_succeedsOnFirstAttempt() throws Exception {
        OptimisticLockRetryExecutor executor = new OptimisticLockRetryExecutor(3);
        AtomicInteger callCount = new AtomicInteger(0);

        String result = executor.execute(() -> {
            callCount.incrementAndGet();
            return "success";
        });

        assertEquals("success", result);
        assertEquals(1, callCount.get());
    }

    @Test
    void testExecute_retriesOnOptimisticLockException() throws Exception {
        OptimisticLockRetryExecutor executor = new OptimisticLockRetryExecutor(3);
        AtomicInteger callCount = new AtomicInteger(0);

        String result = executor.execute(() -> {
            int count = callCount.incrementAndGet();
            if (count < 3) {
                throw new OptimisticLockException("Simulated conflict");
            }
            return "success after retires";
        });

        assertEquals("success after retries", result);
        assertEquals(3, callCount.get());
    }

    @Test
    void testExecute_throwsAfterMaxRetries() {
        OptimisticLockRetryExecutor executor = new OptimisticLockRetryExecutor(2);
        AtomicInteger callCount = new AtomicInteger(0);

        assertThrows(Exception.class, () -> executor.execute(() -> {
            callCount.incrementAndGet();
            throw new OptimisticLockException("Persistent conflict");
        }));
        assertEquals(2, callCount.get());
    }

    @Test
    void testExecute_nonLockExceptionNotRetried() {
        OptimisticLockRetryExecutor executor = new OptimisticLockRetryExecutor(3);
        AtomicInteger callCount = new AtomicInteger(0);

        assertThrows(RuntimeException.class, () -> executor.execute(() -> {
            callCount.incrementAndGet();
            throw new RuntimeException("Non-lock error");
        }));
        assertEquals(1, callCount.get());
    }

    @Test
    void testExecute_returnsNullSuccessFully() throws Exception {
        OptimisticLockRetryExecutor executor = new OptimisticLockRetryExecutor(3);

        Object result = executor.execute(() -> null);

        assertNull(result);
    }
}
