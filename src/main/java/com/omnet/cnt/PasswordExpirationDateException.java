package com.omnet.cnt;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PasswordExpirationDateException extends UsernameNotFoundException {

    public PasswordExpirationDateException(String message) {
        super(message);
    }

    public PasswordExpirationDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
