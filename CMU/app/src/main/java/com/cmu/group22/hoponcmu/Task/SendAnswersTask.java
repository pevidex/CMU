package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.CurrentQuizActivity;
import com.cmu.group22.hoponcmu.GlobalContext;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public SendAnswersTask(CurrentQuizActivity currentQuizActivity) {
        this.currentQuizActivity = currentQuizActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        //ResponseHandlerImpl handler = new ResponseHandlerImpl();
        GlobalContext globalContext = (GlobalContext)  currentQuizActivity.getApplicationContext();
        Answers answers = globalContext.getAnswers();

        AnswersCommand ac = new AnswersCommand(answers.getAnswers());

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
