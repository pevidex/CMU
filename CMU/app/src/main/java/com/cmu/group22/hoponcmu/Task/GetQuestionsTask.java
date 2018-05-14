package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.CurrentQuizActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import classes.Question;
import command.GetQuestionsCommand;
import response.GetQuestionsResponse;

public class GetQuestionsTask extends AsyncTask<String, Void, String> {

    private CurrentQuizActivity currentQuizActivity;
    private ArrayList<Question> questions;

    public GetQuestionsTask(CurrentQuizActivity currentQuizActivity) {
        this.currentQuizActivity = currentQuizActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        //ResponseHandlerImpl handler = new ResponseHandlerImpl();
        GetQuestionsCommand glc = new GetQuestionsCommand(params[0]);
        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(glc);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetQuestionsResponse gqr = (GetQuestionsResponse) ois.readObject();
            if(!gqr.getSuccess())
                currentQuizActivity.updateInterface("Failed to get the locations!");
            this.questions = gqr.getQuestions();
            currentQuizActivity.updateQuestions(this.questions);
            Log.d("DummyClient","Questions received");
            oos.close();
            ois.close();
            Log.d("DummyClient", "Hi there!!");
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
