package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.QuizResultActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import command.UserHistoryCommand;
import command.UserLocationHistoryCommand;
import response.UserHistoryResponse;
import classes.Location;
import response.UserLocationHistoryResponse;

public class UserLocationHistoryTask extends AsyncTask<String, Void, String> {

    private QuizResultActivity quizResultActivity;

    public UserLocationHistoryTask(QuizResultActivity quizResultActivity) {
        this.quizResultActivity = quizResultActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        UserLocationHistoryCommand glc = new UserLocationHistoryCommand(params[0],params[1]);
        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(glc);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            UserLocationHistoryResponse lr = (UserLocationHistoryResponse) ois.readObject();
            if(quizResultActivity==null)
                Log.d("UserLocationHistoryTask","activity-NULL");

            quizResultActivity.updateInterface(lr.getQuestions(),lr.getAnswersResult(),lr.getUserAnswers());
            oos.close();
            ois.close();
            Log.d("UserLocationHistoryTask", "Hi there!!");
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
