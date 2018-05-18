package response;

import java.io.Serializable;

import security.EncryptedObject;
import security.SignedObject;

public class Response implements Serializable{
    EncryptedObject obj;

    public EncryptedObject getEncryptedObj() {
        return obj;
    }

    public Response(EncryptedObject obj) {
        this.obj = obj;
    }
}
