package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import command.AnswersCommand;
import command.CommandCliHandlerImpl;
import request.Request;
import response.AnswersResponse;
import response.Response;
import security.EncryptedObject;
import security.SignedObject;

public class DeliverCommandTask extends AsyncTask<String, Void, String> {
    CommandCliHandlerImpl commandHandlerImpl;
    Request answersCommand;
    public DeliverCommandTask(CommandCliHandlerImpl commandHandlerImpl, Request answersCommand) {
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
            Response ar = (Response) ois.readObject();

            commandHandlerImpl.updateServerAnswer(ar);
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
}
