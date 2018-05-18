package command;


import android.provider.Settings;
import android.util.Log;

import com.cmu.group22.hoponcmu.GlobalContext;
import com.cmu.group22.hoponcmu.Task.DeliverCommandTask;

import java.util.Map;

import response.AnswersResponse;
import response.Response;

public class CommandHandlerImpl implements CommandHandler {
    AnswersResponse lastAnswers;
    GlobalContext globalContext;

    public CommandHandlerImpl(){}
    public CommandHandlerImpl(GlobalContext g){
        this.globalContext = g;
    }

    @Override
    public Response handle(LoginCommand c) {
        return null;
    }

    @Override
    public Response handle(RegisterCommand c) {
        return null;
    }

    @Override
    public Response handle(GetLocationsCommand c) {
        return null;
    }

    @Override
    public Response handle(GetQuestionsCommand c) {
        return null;
    }

    @Override
    public Response handle(AnswersCommand lc) {
        DeliverCommandTask d = (DeliverCommandTask) new DeliverCommandTask(this,lc).execute();
        try{
            String temp = d.get();}
        catch(Exception e){
            Log.d("DummyClient","ERROR on get questions task");}
        return lastAnswers;
    }

    @Override
    public Response handle(UserHistoryCommand c) {
        return null;
    }

    @Override
    public Response handle(UserLocationHistoryCommand c) {
        return null;
    }

    public void updateServerAnswer(AnswersResponse a){
        this.lastAnswers=a;
    }

    @Override
    public Response handle(ShareCommand c){
        globalContext.addToRanking(c.getUserName(), c.getLocation(), c.getCorrect(), c.getTotal());
        globalContext.addToTimeRanking(c.getUserName(), c.getLocation(), c.getQuizzTime());
        return null;
    }
}
