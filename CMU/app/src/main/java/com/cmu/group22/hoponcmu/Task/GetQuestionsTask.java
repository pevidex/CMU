package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.CurrentQuizActivity;
import com.cmu.group22.hoponcmu.GlobalContext;

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

import classes.Question;
import command.Commands;
import command.GetQuestionsCommand;
import command.LoginCommand;
import request.Request;
import response.GetQuestionsResponse;
import response.LoginResponse;
import response.Response;


public class GetQuestionsTask extends Tasks {

    private CurrentQuizActivity currentQuizActivity;
    private ArrayList<Question> questions;

    public GetQuestionsTask(CurrentQuizActivity currentQuizActivity, GlobalContext context) {
        super(context);
        this.currentQuizActivity = currentQuizActivity;
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
            Commands lc = new GetQuestionsCommand(params[0]);
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
            Response gqr = (Response) ois.readObject();
            Object o = unpackEncryptedResponse(gqr, serverSignPbkBytes);
            if(o != null && o instanceof GetQuestionsResponse){
                GetQuestionsResponse response = (GetQuestionsResponse) o;
                if(!response.getSuccess())
                    currentQuizActivity.updateInterface("Failed to get the locations!");
                this.questions = response.getQuestions();
                currentQuizActivity.updateQuestions(this.questions);
                Log.d("DummyClient","Questions received");
                oos.close();
                ois.close();
                Log.d("DummyClient", "Hi there!!");
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
            currentQuizActivity.updateInterface(o);
        }
    }
}
