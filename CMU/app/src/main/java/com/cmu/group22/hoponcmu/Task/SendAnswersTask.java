package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.CurrentQuizActivity;
import com.cmu.group22.hoponcmu.GlobalContext;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import classes.Answers;
import classes.Question;
import command.AnswersCommand;
import command.GetQuestionsCommand;
import response.AnswersResponse;
import response.GetQuestionsResponse;

public class SendAnswersTask extends AsyncTask<String, Void, String> {

    private CurrentQuizActivity currentQuizActivity;
    ArrayList<Boolean> answersResults;
    ArrayList<Integer> answers;
    String location;
    String userName;
    long quizzTime;

    public SendAnswersTask(CurrentQuizActivity currentQuizActivity, ArrayList<Integer> a, String l, String userName, long qt) {
        this.currentQuizActivity = currentQuizActivity;
        this.answers = a;
        this.location = l;
        this.userName= userName;
        this.quizzTime = qt;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        //ResponseHandlerImpl handler = new ResponseHandlerImpl();
        Log.d("SendAnswersTask", "Answer's Size: " + answers.size());
        AnswersCommand ac = new AnswersCommand(location, answers,userName, quizzTime);

        try {
            server = new Socket("10.0.2.2", 9090);
            Log.d("SendAnswersTask", "step1");
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(ac);
            Log.d("SendAnswersTask", "step2");
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            Log.d("SendAnswersTask", "step3");
            AnswersResponse ar = (AnswersResponse) ois.readObject();
            if(ar==null)
                Log.d("SendAnswersTask","lr null");

            this.answersResults = ar.getAnswersResult();

            if(this.answersResults==null)
                Log.d("SendAnswersTask","questions null");

            currentQuizActivity.updateAnswers(this.answersResults);
            oos.close();
            ois.close();
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
