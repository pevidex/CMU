package command;


import android.provider.Settings;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.Task.DeliverCommandTask;

import java.util.Map;

import request.Request;
import response.AnswersResponse;
import response.CliResponse;
import response.Response;
import response.ResponseData;
import security.EncryptedObject;
import security.SignedObject;

public class CommandCliHandlerImpl implements CommandClientHandler {
    Response lastAnswers;
    GlobalContext globalContext;

    public CommandCliHandlerImpl(GlobalContext g){
        this.globalContext = g;
    }


    @Override
    public CliResponse handle(CliAnswersCommand lc) {
        Request request = lc.getReq();
        DeliverCommandTask d = (DeliverCommandTask) new DeliverCommandTask(this, request).execute();
        try{
            String temp = d.get();}
        catch(Exception e){
            Log.d("DummyClient","ERROR on get questions task");}
        return new CliResponse(lastAnswers, "ok");
    }

    public void updateServerAnswer(Response r){
        this.lastAnswers=r;
    }

    @Override
    public CliResponse handle(ShareCommand c){
        globalContext.addToRanking(c.getUserName(), c.getLocation(), c.getCorrect(), c.getTotal());
        globalContext.addToTimeRanking(c.getUserName(), c.getLocation(), c.getQuizzTime());
        return new CliResponse(null, "ok");
    }

}
