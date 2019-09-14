package com.titaniumproductionco.db.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.titaniumproductionco.db.services.func.Proc;

public class LoginService {
    private static final SecureRandom RAND = new SecureRandom();
    // From Connectivity Lab
    private static final Base64.Encoder enc = Base64.getEncoder();
    private static final Base64.Decoder dec = Base64.getDecoder();

    /**
     * Login to the system with given username and password
     * 
     * @param username
     * @param password
     * @return true if the login is successful
     */
    public static boolean login(String username, char[] password) {
        if (username == null || username.length() <= 0 || password == null || password.length <= 0)
            return false;
        // Call stored proc
        Map<String, Object> result = Proc.Login_Check.call(null, username);
        if (result == null)
            return false;

        String salt = (String) result.get("PasswordSalt");
        String hash = (String) result.get("PasswordHash");

        String entered = hashPassword(stringToBytes(salt), password);
        return constantLengthTimeEquals(hash.toCharArray(), entered.toCharArray());
    }

    private static boolean constantLengthTimeEquals(char[] str1, char[] str2) {
        // See https://crackstation.net/hashing-security.htm
        int diff = str1.length ^ str2.length;
        for (int i = 0; i < str1.length && i < str2.length; i++) {
            diff |= str1[i] ^ str2[i];
        }
        return diff == 0;
    }

    /**
     * Add a new login user to the system
     * 
     * @param username
     * @param password
     * @param role
     * @param arg      role-specific argument
     * @return
     */
    public static boolean register(String username, char[] password, Role role, int arg) {
        if (username == null || username.length() <= 0 || password == null || password.length <= 0 || role == null)
            return false;
        byte[] salt = newSalt();
        String saltString = bytesToString(salt);
        String hashPass = hashPassword(salt, password);
        // Call stored proc
        Map<String, Object> result = Proc.Login_New.call(null, username, hashPass, saltString, role.NAME, arg);
        return result != null;

    }

    public static byte[] newSalt() {
        byte[] salt = new byte[16];
        RAND.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(byte[] salt, char[] password) {
        /* From Connectivity Lab. Slightly modified */
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKeyFactory f;
        byte[] hash = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (hash == null)
            throw new RuntimeException("Password did not hash!");
        return bytesToString(hash);
    }

    public static String bytesToString(byte[] data) {
        return enc.encodeToString(data);
    }

    public static byte[] stringToBytes(String str) {
        return dec.decode(str);
    }
}
