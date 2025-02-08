package io.castelo.main_server.auth.jwt;

import java.util.Date;

public record AuthTokenResponse(
        String accessToken,
        Date expiresIn
) {
}
