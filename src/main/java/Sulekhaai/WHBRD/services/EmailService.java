package Sulekhaai.WHBRD.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendOtpEmail(String to, String otp) {
        // For testing purposes, just print the OTP to console
        System.out.println("==========================================");
        System.out.println("TEST MODE: Email would be sent to: " + to);
        System.out.println("TEST MODE: OTP is: " + otp);
        System.out.println("==========================================");
        
        // No actual email sending for testing
        // This prevents the authentication error
    }
} 