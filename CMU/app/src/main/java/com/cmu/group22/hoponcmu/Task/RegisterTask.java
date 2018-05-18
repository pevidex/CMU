package com.cmu.group22.hoponcmu.Task;

import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.RegisterActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import command.Commands;
import command.RegisterCommand;
import request.Request;
import response.Response;

import response.RegisterResponse;

public class RegisterTask extends Tasks {

    private RegisterActivity registerActivity;



    public RegisterTask(RegisterActivity registerActivity, GlobalContext c) {
        super(c);
        this.registerActivity = registerActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        Request request = null;

        byte[] serverSignPbkBytes = getServerPbk("serverkey/signpbk");//used for sign
        PublicKey signPbk = null;
        PrivateKey signPrk = null;
        //get key
        signPrk = context.getSignPri();
        signPbk = context.getSignPub();


        //create the request of register
        try {
            Commands registerC = new RegisterCommand(params[1],params[0]);
            request = wrapRequest(registerC, signPbk, signPrk);
        } catch (IOException|InvalidAlgorithmParameterException|NoSuchPaddingException|NoSuchAlgorithmException|InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {
            Log.d("requesterror",e.getMessage());
        }

        //send the request and deal with
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(request);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            Response lr = (Response) ois.readObject();


            Object o = unpackEncryptedResponse(lr,serverSignPbkBytes);
            if(o != null && o instanceof RegisterResponse){
                    RegisterResponse response = (RegisterResponse) o;
                    reply = response.getMessage();
                    if(response.getSuccess())
                        registerActivity.nextActivity();
                    Log.d("DummyClient",response.getMessage());

                    oos.close();
                    ois.close();

            }else{
                Log.d("responsemsg","something wrong");
                oos.close();
                ois.close();
                return null;
            }



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



}
