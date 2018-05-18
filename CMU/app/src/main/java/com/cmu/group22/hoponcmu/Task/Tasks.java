package com.cmu.group22.hoponcmu.Task;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import command.AgreeSecretKeyCommand;
import command.Command;
import command.Commands;
import request.KeyAgreeRequest;
import request.NormalRequest;
import request.Request;
import response.LoginResponse;
import response.Response;
import response.ResponseData;
import security.EncryptedObject;
import security.SignedObject;

public abstract class Tasks extends AsyncTask<String, Void, String> {
    protected final GlobalContext context;

    public Tasks(GlobalContext context) {
        this.context = context;
    }

    public byte[] getServerPbk(String Path) {
        AssetManager assetManager = context.getAssets();
        InputStream input = null;
        byte[] pubkeyBytes = null;
        try {
            input = assetManager.open(Path);
            int size = 0;
            size = input.available();
            pubkeyBytes = new byte[size];
            input.read(pubkeyBytes);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pubkeyBytes;

    }

    public Request wrapRequest(Commands cmd, PublicKey signPbk, PrivateKey signPrk) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        if(cmd instanceof AgreeSecretKeyCommand){
            SignedObject cmdSigned = new SignedObject(cmd, signPrk);
            return new KeyAgreeRequest(signPbk.getEncoded(),cmdSigned);
        }else{//normal request
            SignedObject cmdSigned = new SignedObject(cmd, signPrk);
            SecretKeySpec secretKey = context.getSecretKey();
            Log.d("debugtask","normal request - 2");
            if(secretKey == null){
                Log.d("debugtask","secret null");
            }
            EncryptedObject cmdEncrypted = new EncryptedObject(cmdSigned, secretKey);
            Log.d("debugtask","normal request - 3");

            return new NormalRequest(signPbk.getEncoded(), cmdEncrypted);

        }

    }

    public Object unpackEncryptedResponse(Response response, byte[] serverSignKey) throws NoSuchPaddingException, ClassNotFoundException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, InvalidKeySpecException, SignatureException {
        if(response == null)
            return null;
        EncryptedObject encryptedResponse = response.getEncryptedObj();
        Log.d("debugtask","before context");

        SignedObject decrypted = encryptedResponse.decrypt(context.getSecretKey());
        Log.d("debugtask","after context");

        if(decrypted.verify(serverSignKey))
            return decrypted.getObject();
        return null;
    }
}
