package Sulekhaai.WHBRD.services;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.time.Instant;

@Service
public class OtpService {
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(email, new OtpData(otp, Instant.now().plusSeconds(300))); // 5 minutes expiry
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData data = otpStore.get(email);
        if (data == null || data.isExpired() || !data.otp.equals(otp)) {
            return false;
        }
        otpStore.remove(email); // OTP used, remove it
        return true;
    }

    private static class OtpData {
        final String otp;
        final Instant expiry;

        OtpData(String otp, Instant expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiry);
        }
    }
} 