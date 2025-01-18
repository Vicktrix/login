package com.myLogin.login.token;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DecodeEncodeService {
    
    public String encrypt(String message, PublicKey publicKey) {
        final Cipher cipher = getCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException ex) {System.err.println("InvalidKeyException "+ex);}
        final byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(finalCipher(bytes, cipher)); 
    }
    
    public String decrypt(String encrypedMessage, PrivateKey privateKey) {
        final byte[] decode = Base64.getDecoder().decode(encrypedMessage);
        final Cipher cipher = getCipher();
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException ex) {System.err.println("InvalidKeyException "+ex);}
        return new String(finalCipher(decode, cipher), StandardCharsets.UTF_8);   
    }
    
    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
//            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException ex) {System.err.println("NoSuchAlgorithmException "+ex);
        } catch (NoSuchPaddingException ex) {System.err.println("NoSuchPaddingException "+ex);}
        return cipher;
    }
    
    private byte[] finalCipher(byte[] inPut, Cipher cipher) {
        byte[] outPut = null;
        try {
            outPut = cipher.doFinal(inPut);
        } catch (IllegalBlockSizeException ex) {System.err.println("IllegalBlockSizeException "+ex);
        } catch (BadPaddingException ex) {System.err.println("BadPaddingException "+ex);}
        return outPut;
    }
    // -------------------------------------------------------------------------
    
    public PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {e.printStackTrace();
        } catch (InvalidKeySpecException e) {e.printStackTrace();}
        return publicKey;
    }
    public PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {e.printStackTrace();
        } catch (InvalidKeySpecException e) {e.printStackTrace();}
        return privateKey;
    }
}