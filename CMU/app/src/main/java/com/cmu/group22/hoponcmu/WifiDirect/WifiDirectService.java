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


import com.cmu.group22.hoponcmu.CurrentQuizActivity;
import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.QuizSubmitActivity;
import com.cmu.group22.hoponcmu.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import classes.Question;
import command.AnswersCommand;
import command.CliAnswersCommand;
import command.CliCommand;
import command.Command;
import command.CommandCliHandlerImpl;
import command.ShareCommand;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import request.NormalRequest;
import response.AnswersResponse;
import response.CliResponse;
import response.Response;
import response.ResponseData;
import security.EncryptedObject;
import security.SignedObject;

public class WifiDirectService extends Service implements SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener{


    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pSocket mCliSocket = null;
    private CommandCliHandlerImpl handler;

    private SimWifiP2pBroadcastReceiver receiver;
    private Map<String, String> peersByName = new HashMap<String, String>();
    private static WifiDirectService instance;

    private String myName;

    boolean nearMonument = false;
    String monumentId = "";
    GlobalContext globalContext;

    public WifiDirectService(){
        instance = this;
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
        globalContext = (GlobalContext) getApplicationContext();
        handler = new CommandCliHandlerImpl(globalContext);

        Log.d("WIFI-SERVICE", "INITIALIZED");
        // spawn the chat server background task
        new IncommingCommTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(mBound){
            Log.d("WIFI-SERVICE","INSIDE BOUND");
            unbindService(mConnection);
            mBound=false;
            unregisterReceiver(receiver);
        }
        instance=null;
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

        for (SimWifiP2pDevice device : deviceList.getDeviceList()) {
            if(device.deviceName.startsWith("M")) {
                nearMonument = true;
                monumentId = device.deviceName.substring(1);
                globalContext.setQuizz(new ArrayList<Question>()); //CLEAR OLD MONUMENT QUESTIONS
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
            mBound = false;
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
                    try {
                        ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                        CliCommand cmd = null;
                        try {
                            cmd = (CliCommand) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        CliResponse cliResponse= cmd.handle(handler);
                        if(!cliResponse.getSTATE_CODE().equals("ok"))
                            return null;
                        Response rsp = (Response) cliResponse.getObj();
                        Log.d("WIFI-SERVICE", "Received Communication");

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


    public String sendAnswersToNative(CurrentQuizActivity c, ArrayList<Integer> answers, String location, String userName, long quizzTime){
        for (Map.Entry<String, String> entry : peersByName.entrySet()) {
            if(entry.getKey().startsWith("M")) continue;
            if((Integer.parseInt(entry.getKey().substring(4)))%2 ==0) {
                Log.d("WIFI-SERVICE", "sending answers to neighbor");
                SendAnswersToNativeTask s = (SendAnswersToNativeTask) new SendAnswersToNativeTask(c, answers, location, userName, quizzTime).execute(entry.getValue());
                try {
                    String temp = s.get();
                    Log.d("WIFI-DIRECT", "Task Result = " + temp);
                    if (temp.equals("ACK"))
                        return "SUCCESS";
                } catch (Exception e) {
                    Log.d("CurrentActivity", "task error");
                }
            }

        }
        return "ERROR";
    }

    public class SendAnswersToNativeTask extends AsyncTask<String, String, String> {
        private String location;
        private String userName;
        private ArrayList<Integer> answers;
        private CurrentQuizActivity currentQuizActivity;
        long quizzTime;
        public SendAnswersToNativeTask(CurrentQuizActivity c, ArrayList<Integer> answers, String location, String userName, long qt){
            this.answers=answers;
            this.location=location;
            this.userName=userName;
            this.currentQuizActivity=c;
            this.quizzTime = qt;
        }
        @Override
        protected String doInBackground(String[] params) {
            try {
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));
                AnswersCommand ac =new AnswersCommand(location, answers,userName, quizzTime);

                SignedObject cmdSigned = new SignedObject(ac, globalContext.getSignPri());
                SecretKeySpec secretKey = globalContext.getSecretKey();
                EncryptedObject cmdEncrypted = new EncryptedObject(cmdSigned, secretKey);
                NormalRequest req = new NormalRequest(globalContext.getSignPub().getEncoded(), cmdEncrypted);

                CliAnswersCommand cac = new CliAnswersCommand(req);

                ObjectOutputStream oos = new ObjectOutputStream(mCliSocket.getOutputStream());
                oos.writeObject(cac);
                Log.d("WIFI-SERVICE", "Sent Answers");
                ObjectInputStream ois = new ObjectInputStream(mCliSocket.getInputStream());

                Response r = null;
                AnswersResponse ar = null;
                try {
                    r = (Response) ois.readObject();
                    if(r == null)
                        return null;
                    EncryptedObject encryptedR = r.getEncryptedObj();
                    SignedObject decryptedR = encryptedR.decrypt(globalContext.getSecretKey());
                    if(decryptedR.verify(globalContext.getServerSigKey()))
                        ar = (AnswersResponse) decryptedR.getObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                currentQuizActivity.updateAnswers(ar.getAnswersResult());
                mCliSocket.close();
                mCliSocket = null;
                return "ACK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCliSocket = null;
            return null;
        }

    }

    public void shareResults(QuizSubmitActivity c, int numCorrectAnswers, String location, String userName, long quizzTime){
        for (Map.Entry<String, String> entry : peersByName.entrySet()) {
            Log.d("WIFI-SERVICE", "Sharing answer with " + entry.getKey());
            new ShareResultsTask(c, numCorrectAnswers, location, userName, quizzTime).execute(entry.getValue());
        }
    }

    public class ShareResultsTask extends AsyncTask<String, String, String> {
        private String location;
        private String userName;
        int correctAnswers;
        private QuizSubmitActivity quizSubmitActivity;
        private long quizzTime;
        public ShareResultsTask(QuizSubmitActivity c, int r , String location, String userName, long qt){
            this.correctAnswers = r;
            this.location=location;
            this.userName=userName;
            this.quizSubmitActivity=c;
            this.quizzTime = qt;
        }

        @Override
        protected String doInBackground(String[] params) {
            try {
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));
                ShareCommand sc = new ShareCommand(userName, location, correctAnswers, 4, quizzTime);
                ObjectOutputStream oos = new ObjectOutputStream(mCliSocket.getOutputStream());
                oos.writeObject(sc);
                Log.d("WIFI-SERVICE", "Sent share!");
                ObjectInputStream ois = new ObjectInputStream(mCliSocket.getInputStream());

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
