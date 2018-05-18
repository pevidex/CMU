package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.QuizResultActivity;

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
import command.LoginCommand;
import command.UserHistoryCommand;
import command.UserLocationHistoryCommand;
import request.Request;
import response.GetLocationsResponse;
import response.Response;
import response.UserHistoryResponse;
import classes.Location;
import response.UserLocationHistoryResponse;

public class UserLocationHistoryTask extends Tasks {

    private QuizResultActivity quizResultActivity;

    public UserLocationHistoryTask(QuizResultActivity quizResultActivity, GlobalContext c) {
        super(c);
        this.quizResultActivity = quizResultActivity;
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
            Commands glc = new UserLocationHistoryCommand(params[0],params[1]);
            request = wrapRequest(glc, signPbk, signPrk);
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
            if(o != null && o instanceof UserLocationHistoryResponse){
                UserLocationHistoryResponse response = (UserLocationHistoryResponse) o;
                if(quizResultActivity==null)
                    Log.d("UserLocationHistoryTask","activity-NULL");
                quizResultActivity.updateInterface(response.getQuestions(),response.getAnswersResult(),response.getUserAnswers());
                oos.close();
                ois.close();
                Log.d("UserLocationHistoryTask", "Hi there!!");
            }




        }
        catch (Exception e) {
            Log.d("UserHistoryHistoryTask", "DummyTask failed..." + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null) {
                try { server.close(); }
                catch (Exception e) { }
            }
        }
        return reply;
    }

   /* @Override
    protected void onPostExecute(String o) {
        if (o!=null) {
            quizResultActivity.updateInterface(o);
        }
    }*/
}
