package command;


import android.util.Log;

import com.cmu.group22.hoponcmu.Task.DeliverCommandTask;

import response.AnswersResponse;
import response.Response;

public class CommandHandlerImpl implements CommandHandler {
    AnswersResponse lastAnswers;
    @Override
    public Response handle(AnswersCommand lc) {
        DeliverCommandTask d = (DeliverCommandTask) new DeliverCommandTask(this,lc).execute();
        try{
            String temp = d.get();}
        catch(Exception e){
            Log.d("DummyClient","ERROR on get questions task");}
        return lastAnswers;
    }
    public void updateServerAnswer(AnswersResponse a){
        this.lastAnswers=a;
    }
}
