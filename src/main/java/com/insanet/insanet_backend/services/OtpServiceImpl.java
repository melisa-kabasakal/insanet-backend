package com.insanet.insanet_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private final StringRedisTemplate redisTemplate;

    private static final int OTP_LENGTH = 6;
    private static final String OTP_CHARSET = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Autowired
    public OtpServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARSET.charAt(random.nextInt(OTP_CHARSET.length())));
        }
        String finalOtp = otp.toString();
        logger.info("Generated OTP: {}", finalOtp);
        return finalOtp;
    }

    private boolean sendOtpToEmail(String email, String otp) {
        try {
            logger.info("Sending OTP to email: {} -> OTP: {}", email, otp);
            redisTemplate.opsForValue().set("otp:" + email, otp, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            logger.error("Error sending OTP to email: {}", email, e);
            return false;
        }
    }

    private boolean sendOtpToPhone(String phone, String otp) {
        try {
            logger.info("Sending OTP to phone: {} -> OTP: {}", phone, otp);
            redisTemplate.opsForValue().set("otp:" + phone, otp, 5, TimeUnit.MINUTES);
            logger.debug("OTP stored in Redis for phone {}: {}", phone, otp);
            return true;
        } catch (Exception e) {
            logger.error("Error sending OTP to phone: {}", phone, e);
            return false;
        }
    }

    @Override
    public boolean sendOtp(String emailOrPhone) {
        String otp = generateOtp();
        if (emailOrPhone.contains("@")) {
            return sendOtpToEmail(emailOrPhone, otp);
        } else {
            return sendOtpToPhone(emailOrPhone, otp);
        }
    }

    @Override
    public String getOtpForEmailOrPhone(String emailOrPhone) {
        return redisTemplate.opsForValue().get("otp:" + emailOrPhone);
    }

    @Override
    public boolean verifyOtp(String emailOrPhone, String otpCode) {
        try {
            String storedOtp = redisTemplate.opsForValue().get("otp:" + emailOrPhone);
            logger.debug("Stored OTP in Redis for {}: {}", emailOrPhone, storedOtp);
            return otpCode != null && otpCode.equals(storedOtp);
        } catch (Exception e) {
            logger.error("Error verifying OTP for {}", emailOrPhone, e);
            return false;
        }
    }
}
