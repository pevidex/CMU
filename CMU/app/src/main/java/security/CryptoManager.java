package security;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
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

    public static SecretKeySpec generateSK() throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String passkey = "qwe123098poifsdf";
        SecretKeySpec secretKey = new SecretKeySpec(passkey.getBytes("UTF-8"),"AES");
        return secretKey;
    }

    public static byte[] encryptSK(byte[] toEncrypt, byte[] keyBytes){
        byte[] encrypted = null;
        //convert to pubkey
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        Log.d("errorx","fuck");
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            Log.d("errorx","fuck");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d("errorx",e.getMessage());

        }
        PublicKey pubkey = null;
        try {
            pubkey = kf.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            Log.d("errorx",e.getMessage());
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
            Log.d("debug","before cipher init");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            Log.d("debug","after1 cipher init");

        } catch (InvalidKeyException e) {
            Log.d("errorx",e.getMessage());
            e.printStackTrace();
        }
        Log.d("debug","after2 cipher init");
        try {
            encrypted = cipher.doFinal(toEncrypt);
        } catch (IllegalBlockSizeException e) {
            Log.d("errorx",e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            Log.d("errorx",e.getMessage());
            e.printStackTrace();
        }
        Log.d("debug","fuck");

        return encrypted;
    }

    public static SecretKeySpec revertSK(byte[] toDecrypt, byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey prikey =  kf.generatePrivate(spec);

        Cipher cipher = null;
        cipher = Cipher.getInstance(AS_ALOGROTHIM);

        cipher.init(Cipher.DECRYPT_MODE, prikey);
        byte[] decrypted = cipher.doFinal(toDecrypt);
        SecretKeySpec secretKey = new SecretKeySpec(decrypted, 0,
                decrypted.length, "AES");
        return secretKey;
    }

}
