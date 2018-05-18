package security;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

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
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
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
    
    public PublicKey getSign_pbk() {
    	return sign_pbk;
    }
    
    public PrivateKey getSign_prk() {
    	return sign_prk;
    }

    public static Signature getSigningEngine() {
        Signature sig = null;
        try {
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException  e) {
            throw new Error("No implementation for "+SIGNATURE_ALGORITHM+" Signature in this JVM", e);
        }
        return sig;
    }
    
	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}
	public PrivateKey getPrivate(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("DSA");
		return kf.generatePrivate(spec);
	}
	
	public PublicKey getPublic(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("DSA");
		return kf.generatePublic(spec);
	}
	
    
}
