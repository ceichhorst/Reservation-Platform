package com.ceichhorst.reservation.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.StaleObjectStateException;

import jakarta.persistence.OptimisticLockException;

import java.util.concurrent.Callable;

/**
 * Utility for retrying operations that may fail due to optimistic lock conflicts
 *
 * @author ceichhorst
 */
public class OptimisticLockRetryExecutor {

    private static final Logger logger = LogManager.getLogger(OptimisticLockRetryExecutor.class);

    private final int maxRetries;

    public OptimisticLockRetryExecutor(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * Executes the given operation, retrying on optimistic lock failure
     * @param operation
     * @param <T>
     * @return the result of the operation
     * @throws Exception
     */
    public <T> T execute(Callable<T> operation) throws Exception {
        int attempts = 0;

        while (true) {
            try {
                attempts++;
                return operation.call();
            } catch (OptimisticLockException | StaleObjectStateException e) {
                if (attempts >= maxRetries) {
                    logger.warn("Optimistic lock conflict unresolved on {} attempts", attempts);
                    throw e;
                }

                logger.info("Optimistic lock conflict on attempt {}, retrying...", attempts);

                // Brief pause before retry to reduce collision
                try {
                    Thread.sleep(50L * attempts);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
    }
}
