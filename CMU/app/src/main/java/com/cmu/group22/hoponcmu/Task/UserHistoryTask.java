package com.cmu.group22.hoponcmu.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.cmu.group22.hoponcmu.MainActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import command.UserHistoryCommand;
import response.UserHistoryResponse;
import classes.Location;

public class UserHistoryTask extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private ArrayList<Location> locations;

    public UserHistoryTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        //ResponseHandlerImpl handler = new ResponseHandlerImpl();
        UserHistoryCommand glc = new UserHistoryCommand(params[0]);
        try {
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(glc);
            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            UserHistoryResponse lr = (UserHistoryResponse) ois.readObject();
            this.locations = lr.getMessage();
            mainActivity.updateLocations(this.locations);
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

    @Override
    protected void onPostExecute(String o) {
        if (o!=null) {
            mainActivity.updateInterface(o);
        }
    }
}
