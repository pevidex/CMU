package security;




import android.util.Log;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class SignKeypair implements Serializable{
    final String alias = "SignKeyPair";
    public PublicKey sign_pbk;
    public PrivateKey sign_prk;
    final static private String SIGNATURE_ALGORITHM = "DSA";

    public SignKeypair() throws NoSuchProviderException {
        //generate the sign keypair

        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance(SIGNATURE_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG","AndroidOpenSSL");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            sign_prk = pair.getPrivate();
            sign_pbk = pair.getPublic();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //Log.d("nosuchalg",e.getMessage());
        }

        //}

    }

    public byte[] getSignPbkBytes(){
        return sign_pbk.getEncoded();
    }

    public byte[] getSignPrkBytes(){
        return sign_prk.getEncoded();
    }

    public PublicKey getSignPbk(byte[] publicKeyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance(SIGNATURE_ALGORITHM);
        return kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }
    public PublicKey getSign_pbk(){
        return sign_pbk;
    }

    public PrivateKey getSign_prk(){
        return sign_prk;
    }

    public static Signature getSigningEngine() {
        Signature sig = null;
        try {
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("No implementation for "+SIGNATURE_ALGORITHM+" Signature in this JVM", e);
        }
        return sig;
    }
}