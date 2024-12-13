package org.tinkoff.token;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class TokenUtil {
    private final TokenProvider tokenProvider = new TokenProvider();
    private Token token;
    private final ReentrantLock lock = new ReentrantLock();

    public Token getToken() {
        if (lock.tryLock()) {
            try {
                if (token == null || new Date().after(token.getExpirationTime())) {
                    try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
                        Future<Token> future = executor.submit(tokenProvider::getToken);
                        token = future.get(15, TimeUnit.SECONDS);
                    } catch (TimeoutException e) {
                        System.out.println("Timeout: " + e.getMessage());
                        return null;
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        return null;
                    }
                } else if (new Date().after(new Date(token.getExpirationTime().getTime() - TimeUnit.MINUTES.toMillis(5)))) {
                    try {
                        token.refresh();
                    } catch (Exception e) {
                        System.out.println("Error refreshing token: " + e.getMessage());
                        return null;
                    }
                }
                return token;
            } finally {
                lock.unlock();
            }
        } else {
            return null;
        }
    }
}