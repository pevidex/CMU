package com.cmu.group22.hoponcmu.Task;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.RegisterActivity;

import command.AgreeSecretKeyCommand;
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
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.acl.LastOwnerException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import response.RegisterResponse;

public class RegisterTask extends AsyncTask<String, Void, String> {

    private RegisterActivity registerActivity;
    private GlobalContext context;


    public RegisterTask(RegisterActivity registerActivity, GlobalContext c) {
        this.registerActivity = registerActivity;
        this.context = c;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;

        byte[] serverPbkBytes = getServerPbk("serverkey/pubkey");//used for encryption
        byte[] serverSignPbkBytes = getServerPbk("serverkey/signpbk");//used for sign
        byte[] secretKeyBytes = null;
        SignKeypair signKeypair = null;
        SecretKey secretKey = null;
        Request requestKeyAgree = null;
        Request requestRegister = null;

        //set some stuff about key
        try {
            signKeypair = new SignKeypair();
            secretKey = CryptoManager.generateSK();
            secretKeyBytes = secretKey.getEncoded();
            context.setSignPri(signKeypair.getSign_prk());
            context.setSignPub(signKeypair.getSign_pbk());
        } catch (NoSuchProviderException|NoSuchAlgorithmException|NoSuchPaddingException e) {
            Log.d("keyerror",e.getMessage());
        }


        //create the request of keyagreement
        try {
            AgreeSecretKeyCommand akeyc = new AgreeSecretKeyCommand(serverPbkBytes,secretKeyBytes);
            SignedObject akeycSigned = new SignedObject(akeyc, signKeypair.getSign_prk());
            KeyAgreeRequest akeycRequest = new KeyAgreeRequest(signKeypair.getSignPbkBytes(),akeycSigned);
            requestKeyAgree = akeycRequest;
        } catch (NoSuchProviderException|BadPaddingException|IllegalBlockSizeException|InvalidKeyException|InvalidKeySpecException|IOException e) {
            Log.d("requesterror",e.getMessage());
        }

        //create the request of register


        try {
            RegisterCommand registerC = new RegisterCommand(params[1],params[0]);
            SignedObject registerCSigned = new SignedObject(registerC, context.getSignPri());
            EncryptedObject registerCEncrypted = new EncryptedObject(registerCSigned, secretKey);
            NormalRequest registerRequest = new NormalRequest(context.getSignPub().getEncoded(), registerCEncrypted);
            requestRegister = registerRequest;
        } catch (IOException|NoSuchPaddingException|NoSuchAlgorithmException|InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {
            Log.d("requesterror",e.getMessage());

        }

        //send the request and deal with
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(requestKeyAgree);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            Response lr = (Response) ois.readObject();

            if(lr == null){
                Log.d("responsemsg","null response");
                oos.close();
                ois.close();
                return null;
            }else {
                EncryptedObject responseEncrypted = lr.getEncryptedObj();
                SignedObject decrypted = responseEncrypted.decrypt(secretKey);

                if(decrypted.verify(serverSignPbkBytes)){
                    if(decrypted.getObject() instanceof SecretKeyResponse){
                        if((((SecretKeyResponse) decrypted.getObject()).getSTATE_CODE()).equals("ok")){
                            Log.d("responsemsg","agree succeed");
                            context.setSecretKey(secretKey);
                            oos.close();
                            ois.close();

                            //send register request
                            ObjectOutputStream oosRegister = new ObjectOutputStream(server.getOutputStream());
                            oosRegister.writeObject(requestRegister);
                            Log.d("responsemsg","waiting...");

                            ObjectInputStream oisRegister = new ObjectInputStream(server.getInputStream());
                            Response rr = (Response) oisRegister.readObject();
                            Log.d("responsemsg","wait end");

                            if(rr == null){
                                Log.d("responsemsg","null response of register");
                                oos.close();
                                ois.close();
                                return null;
                            }
                        }
                        else{
                            Log.d("responsemsg","something wrong with key agreement");
                            oos.close();
                            ois.close();
                            return null;
                        }
                    }else
                        Log.d("responsemsg","not agree response type");
                        oos.close();
                        ois.close();
                        return null;
                }else{
                    oos.close();
                    ois.close();
                    Log.d("responsemsg","signature fails");
                    return null;
                }
            }



            //RegisterResponse lr = (RegisterResponse) ois.readObject();
          //  reply = lr.getMessage();
          //  if(lr.getSuccess())//change this to use a handler
         //       registerActivity.nextActivity();
         //   Log.d("DummyClient",lr.getMessage());

           // Log.d("DummyClient", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("DummyClient", "DummyTask failed..." + e.getMessage());
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

}
