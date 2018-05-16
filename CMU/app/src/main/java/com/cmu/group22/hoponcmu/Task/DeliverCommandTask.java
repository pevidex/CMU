package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import command.AnswersCommand;
import command.CommandHandlerImpl;
import response.AnswersResponse;

public class DeliverCommandTask extends AsyncTask<String, Void, String> {
    CommandHandlerImpl commandHandlerImpl;
    AnswersCommand answersCommand;
    public DeliverCommandTask(CommandHandlerImpl commandHandlerImpl, AnswersCommand answersCommand) {
        this.answersCommand=answersCommand;
        this.commandHandlerImpl=commandHandlerImpl;}


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(answersCommand);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            AnswersResponse ar = (AnswersResponse) ois.readObject();
            commandHandlerImpl.updateServerAnswer(ar);
            Log.d("DummyClient","Locations received");
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
}
