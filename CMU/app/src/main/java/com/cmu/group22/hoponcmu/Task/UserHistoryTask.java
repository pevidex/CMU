package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.MainActivity;
import com.cmu.group22.hoponcmu.MyQuizActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.microedition.khronos.opengles.GL;

import command.Commands;
import command.LoginCommand;
import command.UserHistoryCommand;
import request.Request;
import response.Response;
import response.UserHistoryResponse;
import classes.Location;

public class UserHistoryTask extends Tasks {

    private MyQuizActivity myQuizActivity;
    private ArrayList<Location> locations;

    public UserHistoryTask(MyQuizActivity myQuizActivity, GlobalContext c) {
        super(c);
        this.myQuizActivity = myQuizActivity;
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

        //create the reques
        try {
            Commands lc = new UserHistoryCommand(params[0]);
            request = wrapRequest(lc, signPbk, signPrk);
        }catch (IOException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                e) {
            Log.d("requesterror",e.getMessage());
        }


        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(request);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());

            Response lr = (Response) ois.readObject();
            Object o = unpackEncryptedResponse(lr, serverSignPbkBytes);
            if(o != null && o instanceof UserHistoryResponse){
                UserHistoryResponse response = (UserHistoryResponse) o;
                this.locations = response.getMessage();
                if(myQuizActivity==null)
                    Log.d("UserHistoryTask","step1");
                if(this.locations!=null)
                    myQuizActivity.updateLocations(this.locations);
                Log.d("UserHistoryTask","Locations received");
                oos.close();
                ois.close();
                Log.d("UserHistoryTask", "Hi there!!");
            }


        }
        catch (Exception e) {
            Log.d("UserHistoryTask", "DummyTask failed..." + e.getMessage());
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
        if (o!=null) {
            myQuizActivity.updateInterface(o);
        }
    }
}
