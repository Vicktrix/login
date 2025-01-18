package com.myLogin.login.token;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSAKeyPairGenerator {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyPairGenerator() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSAKeyPairGenerator.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error inside RSAKeyPairGenerator -- "+ex);
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    public String getPublicKeyBase64() {
        return Base64.getMimeEncoder().encodeToString(publicKey.getEncoded());
    }
    public String getPrivateKeyBase64() {
        return Base64.getMimeEncoder().encodeToString(privateKey.getEncoded());
    }
}