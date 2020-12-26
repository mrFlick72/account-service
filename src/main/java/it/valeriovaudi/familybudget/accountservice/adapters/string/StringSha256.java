package it.valeriovaudi.familybudget.accountservice.adapters.string;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringSha256 {
    public static String toSha256(String input) {
        MessageDigest messageDigest;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            digest = messageDigest.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
        }

        return new String(digest, Charset.defaultCharset());
    }
}
