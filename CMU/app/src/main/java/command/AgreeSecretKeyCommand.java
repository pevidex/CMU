package command;


import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import response.Response;
import response.ResponseData;
import security.CryptoManager;

public class AgreeSecretKeyCommand implements Commands{

    public byte[] getEncryptedSK() {
        return encryptedSK;
    }

    private byte[] encryptedSK;
    public AgreeSecretKeyCommand(byte[] serverPbkBytes, byte[] secretKeyBytes) throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        encryptedSK = CryptoManager.encryptSK(secretKeyBytes, serverPbkBytes);

    }

}
