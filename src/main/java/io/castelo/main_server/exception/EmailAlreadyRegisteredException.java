package io.castelo.main_server.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("User with email: " + email + " already exists");
    }
}
