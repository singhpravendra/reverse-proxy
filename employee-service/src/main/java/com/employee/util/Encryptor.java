package com.employee.util;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.Base64;

@Component
public class Encryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    // Simple encryption logic (AES)
    public String encrypt(String data) {
        try {
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[12]; // 12 bytes IV for AES GCM
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));

            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[12]; // Same IV used during encryption
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));

            byte[] decryptedData = cipher.doFinal(encryptedBytes);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
