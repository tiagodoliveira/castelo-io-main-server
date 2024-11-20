package io.castelo.main_server.exception;

import java.util.Date;

public record ErrorDetails(
        Date timeStamp,
        String message,
        String details
) {
}
