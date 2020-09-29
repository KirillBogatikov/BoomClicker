package org.boomgames.boomclicker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private static MessageDigest md5; 
    
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String of(String value) {
        byte[] bytes = md5.digest(value.getBytes());
        bytes = md5.digest(bytes);
        
        for(int i = 0; i < bytes.length; i++) {
            if(bytes[i] == 0) {
                bytes[i] = '_';
            }
        }
        
        return new String(bytes);
    }
}
