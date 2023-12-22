package com.example.dentalmobileapp.Utils;

import java.util.Random;

public class OTPGenerator {

    public static String generateOTP() {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(digits.length());
            otpBuilder.append(digits.charAt(index));
        }

        return otpBuilder.toString();
    }
}
