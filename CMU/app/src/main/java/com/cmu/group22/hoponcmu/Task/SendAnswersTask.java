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

    public SendAnswersTask(CurrentQuizActivity currentQuizActivity, ArrayList<Integer> a, String l) {
        this.currentQuizActivity = currentQuizActivity;
        this.answers = a;
        this.location = l;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        //ResponseHandlerImpl handler = new ResponseHandlerImpl();
        Log.d("Answers Task", "Size: " + answers.size());
        AnswersCommand ac = new AnswersCommand(answers, location);

        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(ac);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            AnswersResponse ar = (AnswersResponse) ois.readObject();

            if(ar==null)
                Log.d("OKKKKKKKK","lr null");

            this.answersResults = ar.getAnswersResult();

            if(this.answersResults==null)
                Log.d("OKKKKKKKK","questions null");

            currentQuizActivity.updateAnswers(this.answersResults);
            oos.close();
            ois.close();
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
        currentQuizActivity.updateAnswersViews();
    }
}
