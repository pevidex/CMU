package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.LoginActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import command.LoginCommand;
import response.LoginResponse;

public class LoginTask extends AsyncTask<String, Void, String> {

    private LoginActivity loginActivity;

    public LoginTask(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        //ResponseHandlerImpl handler = new ResponseHandlerImpl();
        LoginCommand lc = new LoginCommand(params[1],params[0]);
        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(lc);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());

            LoginResponse lr = (LoginResponse) ois.readObject();


            reply = lr.getMessage();
            if(lr.getSuccess()) {//change this to use a handler
                GlobalContext globalContext = (GlobalContext) loginActivity.getApplicationContext();
                globalContext.setSessionId(lr.getSessionId());
                Log.d("Set Session ID", Integer.toString(globalContext.getSessionId()));
                loginActivity.nextActivity();
            }
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
            loginActivity.updateInterface(o);
        }
    }
}
