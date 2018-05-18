package security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoManager {
	
	final private static String AS_ALOGROTHIM = "RSA/ECB/PKCS1Padding";

    public static SecretKey generateSK() throws NoSuchPaddingException, NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static byte[] encryptSK(byte[] toEncrypt, byte[] keyBytes){
        byte[] encrypted = null;
        //convert to pubkey
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        PublicKey pubkey = null;
        try {
            pubkey = kf.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        //encrypt
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(AS_ALOGROTHIM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
   
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
 

        } catch (InvalidKeyException e) {

            e.printStackTrace();
        }

        try {
            encrypted = cipher.doFinal(toEncrypt);
        } catch (IllegalBlockSizeException e) {
    
            e.printStackTrace();
        } catch (BadPaddingException e) {

            e.printStackTrace();
        }


        return encrypted;
    }

    public static SecretKey revertSK(byte[] toDecrypt, PrivateKey prikey) throws NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException {

        Cipher cipher = null;
        cipher = Cipher.getInstance(AS_ALOGROTHIM);

        cipher.init(Cipher.DECRYPT_MODE, prikey);
        byte[] decrypted = cipher.doFinal(toDecrypt);
        
        
        SecretKey secretKey = new SecretKeySpec(decrypted, 0,
                decrypted.length, "AES");
        return secretKey;
    }
    
    public static PublicKey getPbkFromBytes(byte[] publicKeyBytes,String ALGORITHM) throws InvalidKeySpecException, NoSuchAlgorithmException {
    	KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
    	return kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

}
