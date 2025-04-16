package com.insanet.insanet_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public OtpServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean sendOtpToEmail(String email, String otp) {
        try {
            logger.info("Sending OTP to email: {} -> OTP: {}", email, otp);
            redisTemplate.opsForValue().set("otp:" + email, otp, 5, TimeUnit.MINUTES);

            return true;
        } catch (Exception e) {
            logger.error("Error sending OTP to email: {}", email, e);
            return false;
        }
    }

    @Override
    public boolean sendOtpToPhone(String phone, String otp) {
        try {
            logger.info("Sending OTP to phone: {} -> OTP: {}", phone, otp);
            redisTemplate.opsForValue().set("otp:" + phone, otp, 5, TimeUnit.MINUTES);
            String otpValue = redisTemplate.opsForValue().get("otp:" + phone);
            logger.debug("OTP stored in Redis for phone {}: {}", phone, otpValue);
            return true;
        } catch (Exception e) {
            logger.error("Error sending OTP to phone: {}", phone, e);
            return false;
        }
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
