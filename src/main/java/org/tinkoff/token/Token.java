package org.tinkoff.token;

import java.time.Instant;
import java.util.Date;

public class Token {
    private Date expirationTime;
    private final String token;

    public Token(Date expirationTime, String token) {
        this.expirationTime = expirationTime;
        this.token = token;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void refresh() {
        expirationTime = Date.from(Instant.now().plusSeconds(1800));
    }
}
