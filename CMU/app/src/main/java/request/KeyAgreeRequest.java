package request;

import security.EncryptedObject;
import security.SignedObject;

public class KeyAgreeRequest extends Request{
    public SignedObject getSignedObject() {
        return signedObject;
    }

    SignedObject signedObject;
    public KeyAgreeRequest(byte[] requesterKeyBytes, SignedObject signedObject) {
        super(requesterKeyBytes);
        this.signedObject = signedObject;
    }
}
