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

        if (matcherColon.matches()) {
            return macAddress; // Correct format, no conversion needed
        } else if (matcherHyphen.matches()) {
            // Convert from hyphen format to colon format
            return macAddress.replace('-', ':');
        } else if (matcherNoDash.matches()) {
            // Convert from no delimiter format to colon format
            return macAddress.replaceAll("(.{2})(?!$)", "$1:");
        }
        throw new IllegalArgumentException("Invalid MAC Address format");
    }
}