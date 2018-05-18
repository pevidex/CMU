package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.MainActivity;

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

import command.Commands;
import command.GetLocationsCommand;
import request.Request;
import response.GetLocationsResponse;
import classes.Location;
import response.Response;

public class GetLocationsTask extends Tasks {

    private MainActivity mainActivity;
    private ArrayList<Location> locations;

    public GetLocationsTask(MainActivity mainActivity, GlobalContext context) {
        super(context);
        this.mainActivity = mainActivity;
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

        try{
            Commands glc = new GetLocationsCommand();
            request = wrapRequest(glc, signPbk, signPrk);
        } catch (IOException|InvalidAlgorithmParameterException|NoSuchPaddingException|NoSuchAlgorithmException|InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {
            Log.d("requesterror",e.getMessage());
        }

        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(request);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            Response lr = (Response) ois.readObject();

            Object o = unpackEncryptedResponse(lr, serverSignPbkBytes);
            Log.d("debugtask","response received");

            if (o != null & o instanceof GetLocationsResponse){
                Log.d("debugtask","get loc response received");

                GetLocationsResponse response = (GetLocationsResponse) o;
                if(!response.getSuccess())
                    mainActivity.updateInterface("Failed to get the locations!");
                Log.d("debugtask","response get");

                this.locations = response.getLocations();
                mainActivity.updateLocations(this.locations);
                Log.d("debugtask","Locations received");
                oos.close();
                ois.close();
                Log.d("debugtask", "Hi there!!");
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
        if (o!=null) {
            mainActivity.updateInterface(o);
        }
    }
}
