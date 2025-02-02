package io.castelo.main_server.auth;

import java.util.Date;

public record AuthTokenResponse(
        String accessToken,
        Date expiresIn
) {
}
