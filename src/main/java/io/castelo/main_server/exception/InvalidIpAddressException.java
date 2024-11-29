package io.castelo.main_server.exception;

public class InvalidIpAddressException extends RuntimeException {
    public InvalidIpAddressException(String message) {
        super(message);
    }
}
