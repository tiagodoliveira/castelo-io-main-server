package io.castelo.main_server.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MACAddressValidator {

    private static final String MAC_ADDRESS_PATTERN_COLON = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$";
    private static final String MAC_ADDRESS_PATTERN_HYPHEN = "^([0-9A-Fa-f]{2}-){5}[0-9A-Fa-f]{2}$";
    private static final String MAC_ADDRESS_PATTERN_NODASH = "^[0-9A-Fa-f]{12}$";

    public static String normalizeMACAddress(String macAddress) {
        Pattern patternColon = Pattern.compile(MAC_ADDRESS_PATTERN_COLON);
        Pattern patternHyphen = Pattern.compile(MAC_ADDRESS_PATTERN_HYPHEN);
        Pattern patternNoDash = Pattern.compile(MAC_ADDRESS_PATTERN_NODASH);

        Matcher matcherColon = patternColon.matcher(macAddress);
        Matcher matcherHyphen = patternHyphen.matcher(macAddress);
        Matcher matcherNoDash = patternNoDash.matcher(macAddress);

        if (matcherColon.matches() || matcherHyphen.matches()) {
            return macAddress.replaceAll("[-:]", "");
        } else if (matcherNoDash.matches()) {
            return macAddress;
        }
        throw new IllegalArgumentException("Invalid MAC Address format");
    }

    public static String formatMACAddressWithColon(String normalizedMACAddress) {
        if (!normalizedMACAddress.matches(MAC_ADDRESS_PATTERN_NODASH)) {
            throw new IllegalArgumentException("MAC Address must be in normalized format containing only alphanumeric characters");
        }
        return normalizedMACAddress.replaceAll("(.{2})(?!$)", "$1:");
    }
}