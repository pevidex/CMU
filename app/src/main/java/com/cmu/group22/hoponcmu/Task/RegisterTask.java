package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.RegisterActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import command.RegisterCommand;
import response.RegisterResponse;

public class RegisterTask extends AsyncTask<String, Void, String> {

    private RegisterActivity registerActivity;

    public RegisterTask(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        RegisterCommand lc = new RegisterCommand(params[1],params[0]);
        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(lc);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());

            RegisterResponse lr = (RegisterResponse) ois.readObject();
            reply = lr.getMessage();
            if(lr.getSuccess())//change this to use a handler
                registerActivity.nextActivity();
            Log.d("DummyClient",lr.getMessage());
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
        if (o != null) {
            registerActivity.updateInterface(o);
        }
    }
}
