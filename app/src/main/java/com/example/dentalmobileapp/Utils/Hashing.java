package com.example.dentalmobileapp.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String enteredMPIN, String storedHashedMPIN) {
        String enteredHashedMPIN = hashPassword(enteredMPIN);
        return enteredHashedMPIN != null && enteredHashedMPIN.equals(storedHashedMPIN);
    }
}
