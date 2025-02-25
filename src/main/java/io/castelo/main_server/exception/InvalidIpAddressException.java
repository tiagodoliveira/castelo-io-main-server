package io.castelo.main_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidIpAddressException extends RuntimeException {
    public InvalidIpAddressException(String message) {
        super(message);
    }
}
