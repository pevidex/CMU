package security;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public class SignedObject implements Serializable{
    private static final long serialVersionUID = -8807331723807741905L;
    byte[] serializedObject;
    byte[] signatureBytes;


    final static private String SIGNATURE_ALGORITHM = "DSA";


    public SignedObject(Serializable obj, PrivateKey privateKey) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            serializedObject = bos.toByteArray();

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        // Sign serialized object
        Signature signature = SignKeypair.getSigningEngine();
        try {
            signature.initSign(privateKey);
            signature.update(serializedObject);
            this.signatureBytes = signature.sign();
        } catch (NullPointerException | InvalidKeyException | SignatureException e) {
            throw new Error("Problem while creating signed object.", e);
        }
    }

    public byte[] getSerializedObject(){
        return serializedObject;
    }

    public byte[] getSignatureBytes(){
        return signatureBytes;
    }

    public boolean verify(byte[] pubkeyBytes) throws InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance(SIGNATURE_ALGORITHM);
        PublicKey pubkey = kf.generatePublic(new X509EncodedKeySpec(pubkeyBytes));
        Signature signature = SignKeypair.getSigningEngine();

        signature.initVerify(pubkey);
        signature.update(serializedObject);
        return signature.verify(signatureBytes);
    }

    public Object getObject() throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(serializedObject);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;

    }


}