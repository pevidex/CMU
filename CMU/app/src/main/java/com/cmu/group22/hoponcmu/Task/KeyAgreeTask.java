package com.cmu.group22.hoponcmu.Task;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.RegisterActivity;

import command.AgreeSecretKeyCommand;
import command.Commands;
import command.RegisterCommand;
import request.KeyAgreeRequest;
import request.NormalRequest;
import request.Request;
import response.Response;
import response.SecretKeyResponse;
import security.CryptoManager;
import security.EncryptedObject;
import security.SignKeypair;
import security.SignedObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.acl.LastOwnerException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import response.RegisterResponse;

public class KeyAgreeTask extends Tasks {

    private RegisterActivity registerActivity;


    public KeyAgreeTask(RegisterActivity registerActivity, GlobalContext c) {
        super(c);
        this.registerActivity = registerActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        Log.d("debug","error - key task in");

        byte[] serverPbkBytes = getServerPbk("serverkey/pubkey");//used for encryption
        byte[] serverSignPbkBytes = getServerPbk("serverkey/signpbk");//used for sign
        byte[] secretKeyBytes = null;
        SignKeypair signKeypair = null;
        SecretKeySpec secretKey = null;
        Request request = null;
        Log.d("debug","error - key task in - 2");

        //set some stuff about key
        try {
            signKeypair = new SignKeypair();
            secretKey = CryptoManager.generateSK();
            secretKeyBytes = secretKey.getEncoded();
            context.setSignPri(signKeypair.getSign_prk());
            context.setSignPub(signKeypair.getSign_pbk());
            context.setSecretKey(secretKey);

        } catch (UnsupportedEncodingException|NoSuchProviderException|NoSuchAlgorithmException|NoSuchPaddingException e) {
            Log.d("keyerror",e.getMessage());
        }
        Log.d("debugtask","error - key task in - 3");


        //create the request of keyagreement
        try {
            Commands akeyc = new AgreeSecretKeyCommand(serverPbkBytes,secretKeyBytes);
            request = wrapRequest(akeyc, signKeypair.getSign_pbk(), signKeypair.getSign_prk());
        } catch (InvalidAlgorithmParameterException|NoSuchPaddingException|NoSuchAlgorithmException|NoSuchProviderException|BadPaddingException|IllegalBlockSizeException|InvalidKeyException|InvalidKeySpecException|IOException e) {
            Log.d("requesterror",e.getMessage());
        }

        //send the request and deal with
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(request);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            Response lr = (Response) ois.readObject();



            Object o = unpackEncryptedResponse(lr, serverSignPbkBytes);

            if(o instanceof SecretKeyResponse){
                if((((SecretKeyResponse) o).getSTATE_CODE()).equals("ok")){
                    Log.d("responsemsg - keyagree","agree succeed");
                    oos.close();
                    ois.close();
                }
                else{
                    Log.d("responsemsg","something wrong with key agreement");
                    oos.close();
                    ois.close();
                    return null;
                }
            }else{
                Log.d("responsemsg","not agree response type");
                oos.close();
                ois.close();
                return null;

            }


        }
        catch (Exception e) {
            Log.d("DummyClient1", "DummyTask failed..." + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null) {
                try { server.close(); }
                catch (Exception e) { }
            }
        }
        return reply;
    }

    @Override
    protected void onPostExecute(String o) {
        if (o != null) {
            registerActivity.updateInterface(o);
        }
    }


}
