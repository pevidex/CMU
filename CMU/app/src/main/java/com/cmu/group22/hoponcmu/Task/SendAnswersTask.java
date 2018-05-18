package com.cmu.group22.hoponcmu.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.CurrentQuizActivity;
import com.cmu.group22.hoponcmu.GlobalContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
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

import classes.Answers;
import classes.Question;
import command.AnswersCommand;
import command.Commands;
import command.GetQuestionsCommand;
import command.LoginCommand;
import request.Request;
import response.AnswersResponse;
import response.GetQuestionsResponse;
import response.Response;

public class SendAnswersTask extends Tasks {

    private CurrentQuizActivity currentQuizActivity;
    ArrayList<Boolean> answersResults;
    ArrayList<Integer> answers;
    String location;
    String userName;

    public SendAnswersTask(CurrentQuizActivity currentQuizActivity, ArrayList<Integer> a, String l, String userName, GlobalContext c) {
        super(c);
        this.currentQuizActivity = currentQuizActivity;
        this.answers = a;
        this.location = l;
        this.userName= userName;
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

        Log.d("SendAnswersTask", "Answer's Size: " + answers.size());

        try {
            Commands lc = new AnswersCommand(location, answers,userName);
            request = wrapRequest(lc, signPbk, signPrk);
        }catch (IOException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                e) {
            Log.d("requesterror",e.getMessage());
        }


        try {
            server = new Socket("10.0.2.2", 9090);
            Log.d("SendAnswersTask", "step1");
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(request);
            Log.d("SendAnswersTask", "step2");
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            Log.d("SendAnswersTask", "step3");

            Response ar = (Response) ois.readObject();

            Object o = unpackEncryptedResponse(ar, serverSignPbkBytes);
            if(o != null && o instanceof AnswersResponse){
                AnswersResponse response = (AnswersResponse) o;
                if(ar==null)
                    Log.d("SendAnswersTask","lr null");
                this.answersResults = response.getAnswersResult();
                if(this.answersResults==null)
                    Log.d("SendAnswersTask","questions null");

                currentQuizActivity.updateAnswers(this.answersResults);
                oos.close();
                ois.close();
            }


        }
        catch (Exception e) {
            Log.d("SendAnswersTask", "failed...");
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
        currentQuizActivity.updateAnswersViews();
    }
}
