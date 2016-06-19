package com.kebra;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class CitationLatine {
    static String IV = "AAAAAAAAAAAAAAAA";
    static String plaintext = "test text 123\0\0\0"; /*Note null padding*/
    static String encryptionKey = "0123456789abcdef";

    public static void main(String[] args) {
        System.out.println("Citation Latine");
        String enc = doZeJob(plaintext, encryptionKey);
        doZeJob(enc, encryptionKey);
    }

    public static String doZeJob(String text, String clef) {
        String base64encodedString = new String();
        try {

            System.out.println("plain:   " + text);

            String seize = padRight(text, text.length() - (text.length() % 16) + 16);
            System.out.print("seize[" + seize + "] " + seize.length());
            byte[] cipher = encrypt(seize, clef);

            System.out.print("cipher:  ");
            for (int i = 0; i < cipher.length; i++)
                System.out.print(new Integer(cipher[i]) + " ");
            System.out.println("");

            // Encode using basic encoder
            base64encodedString = Base64.getEncoder().encodeToString(cipher);
            System.out.println(base64encodedString);

            // Decode
            byte[] decipher = Base64.getDecoder().decode(base64encodedString);

            String decrypted = decrypt(decipher, encryptionKey);

            System.out.println("decrypt: " + decrypted);
            if(text.indexOf(" ")<0) {
                System.out.println("decrypt2: " + decrypt(Base64.getDecoder().decode(text), encryptionKey));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64encodedString;

    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }


    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}