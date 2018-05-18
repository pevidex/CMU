package request;


import security.EncryptedObject;

public class NormalRequest extends Request{
    public EncryptedObject getEncryptedObject() {
        return encryptedObject;
    }

    private EncryptedObject encryptedObject;

    public NormalRequest(byte[] requesterKeyBytes, EncryptedObject encryptedObject) {
        super(requesterKeyBytes);
        this.encryptedObject = encryptedObject;
    }
}
