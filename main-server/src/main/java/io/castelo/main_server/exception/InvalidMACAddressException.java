package io.castelo.main_server.exception;

public class InvalidMACAddressException extends RuntimeException {
    public InvalidMACAddressException(String message) {
        super(message);
    }
}
