package org.tinkoff.token;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class TokenProvider {
    public Token getToken() {
        return new Token(
                Date.from(Instant.now().plusSeconds(1800)),
                UUID.randomUUID().toString()
        );
    }
}
