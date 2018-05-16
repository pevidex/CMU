package com.cmu.group22.hoponcmu.WifiDirect;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;


import com.cmu.group22.hoponcmu.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import command.AnswersCommand;
import command.Command;
import command.CommandHandlerImpl;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import response.AnswersResponse;
import response.Response;

public class WifiDirectService extends Service implements SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener{


    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pSocket mCliSocket = null;
    private CommandHandlerImpl handler;

    private SimWifiP2pBroadcastReceiver receiver;
    private Map<String, String> peersByName = new HashMap<String, String>();
    private static WifiDirectService instance;

    private String myName;

    boolean nearMonument = false;
    String monumentId = "";

    public WifiDirectService(){
        instance = this;
        handler = new CommandHandlerImpl();
    }

    public static WifiDirectService getInstance(){
        return instance;
    }



    @Override
    public int onStartCommand(Intent t, int f, int sid){
        Log.d("WIFI-SERVICE", "INITIALIZING");
        initializeWifiDirect();
        Intent intent = new Intent(getBaseContext(), SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        Log.d("WIFI-SERVICE", "INITIALIZED");
        // spawn the chat server background task
        new IncommingCommTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return super.onStartCommand(t, f, sid);
    }

    @Override
    public void onDestroy() {
        Log.d("WIFI-SERVICE", "Service Destroyed");
    }

    public void updatePeers(){
        Log.d("WIFI-SERVICE", "Request Peer Update");
        if(mBound)
            mManager.requestPeers(mChannel, WifiDirectService.this);
    }

    public void updateGroup(){
        Log.d("WIFI-SERVICE", "Request Group Update");
        if (mBound)
            mManager.requestGroupInfo(mChannel, WifiDirectService.this);
    }

    private void initializeWifiDirect() {
        SimWifiP2pSocketManager.Init(getBaseContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        receiver = new SimWifiP2pBroadcastReceiver(WifiDirectService.this);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList deviceList, SimWifiP2pInfo simWifiP2pInfo) {

        peersByName.clear();

        myName = simWifiP2pInfo.getDeviceName();

        for (SimWifiP2pDevice device : deviceList.getDeviceList()) {
            if (device.deviceName.equals(myName)) {
                continue;
            }
            peersByName.put(device.deviceName, device.getVirtIp());
            Log.d("WIFI-SERVICE", "onGroupInfoAvailable: added " + device.deviceName);
        }
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList deviceList) {

        nearMonument = false;
        monumentId = "";

        for (SimWifiP2pDevice device : deviceList.getDeviceList()) {
            if(device.deviceName.startsWith("M")) {
                nearMonument = true;
                monumentId = device.deviceName.substring(1);
            }
        }

        if(!nearMonument)
            Log.d("WIFI-SERVICE", "Device not in range of monuments");
    }

    public static boolean isRunning(){
        return instance != null;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getApplication(), getMainLooper(),
                    null);
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
        }
    };

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("WifiService", "IncommingCommTask started (" + this.hashCode() + ").");

            try {
                mSrvSocket = new SimWifiP2pSocketServer(
                        Integer.parseInt(getString(R.string.port)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SimWifiP2pSocket sock = mSrvSocket.accept();
                    Log.d("WIFI-SERVICE", "Accept done");
                    try {
                        ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                        Command cmd = null;
                        try {
                            cmd = (Command) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Response rsp = cmd.handle(handler);

                        ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
                        oos.writeObject(rsp);
                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } finally {
                        sock.close();
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }
            Log.d("WIFI-SERVICE", "Ended INCOMM TASK");
            return null;
        }
    }

    public class OutgoingCommTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));
            } catch (UnknownHostException e) {
                return "Unknown Host:" + e.getMessage();
            } catch (IOException e) {
                return "IO error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
    public String sendAnswersToNative(ArrayList<Integer> answers, String location, String userName){
        for (Map.Entry<String, String> entry : peersByName.entrySet()) {
            if(entry.getKey().equals(monumentId)) continue;
            if((Integer.parseInt(entry.getKey().substring(3)))%2!=0){
                if ((new SendAnswersToNativeTask(answers, location, userName).execute(entry.getValue()).equals("ACK")))
                    return "SUCCESS";
                }
        }
        return "ERROR";
    }

    public class SendAnswersToNativeTask extends AsyncTask<String, String, String> {
        private String location;
        private String userName;
        private ArrayList<Integer> answers;
        public SendAnswersToNativeTask(ArrayList<Integer> answers, String location, String userName){
            this.answers=answers;
            this.location=location;
            this.userName=userName;
        }
        @Override
        protected String doInBackground(String[] params) {
            try {
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));
                AnswersCommand ac =new AnswersCommand(location, answers,userName);
                ObjectOutputStream oos = new ObjectOutputStream(mCliSocket.getOutputStream());
                oos.writeObject(ac);
                ObjectInputStream ois = new ObjectInputStream(mCliSocket.getInputStream());

                AnswersResponse ar = null;
                try {
                    ar = (AnswersResponse) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(ar==null)
                    Log.d("SendAnswersTask","lr null");
                mCliSocket.close();
                mCliSocket = null;
                return "ACK";
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCliSocket = null;
            return null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public boolean isNearMonument() {
        return nearMonument;
    }

    public void setNearMonument(boolean nearMonument) {
        this.nearMonument = nearMonument;
    }

    public String getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(String monumentId) {
        this.monumentId = monumentId;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

}
