package io.castelo.main_server.utils;

import io.castelo.main_server.exception.InvalidIpAddressException;

import java.net.InetAddress;

public class IpAddressValidator {

    public static void validateIpAddress(String ipAddress){
        try {
            InetAddress.ofLiteral(ipAddress);
        } catch (Exception e) {
            throw new InvalidIpAddressException("The IP Address " + ipAddress + " is not valid");
        }
    }
}
