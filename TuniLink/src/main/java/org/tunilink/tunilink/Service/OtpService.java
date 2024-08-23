package org.tunilink.tunilink.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, LocalDateTime> otpTimestamps = new HashMap<>();

    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Générer un OTP de 6 chiffres
        return String.valueOf(otp);
    }


    public void saveOTP(String email, String otp) {
        otpStorage.put(email, otp);
        otpTimestamps.put(email, LocalDateTime.now());
    }

    public boolean verifyOTP(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        LocalDateTime timestamp = otpTimestamps.get(email);

        if (storedOtp == null || timestamp == null) {
            return false;
        }

        // Vérifier si l'OTP a expiré (validité de 5 minutes)
        if (LocalDateTime.now().isAfter(timestamp.plusMinutes(5))) {
            otpStorage.remove(email);
            otpTimestamps.remove(email);
            return false;
        }

        return storedOtp.equals(otp);
    }
}

