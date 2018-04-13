package classes;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Answers implements Serializable{
    private ArrayList<Integer> ans = new ArrayList<>();
    public void init(ArrayList<Question> quizItems){
        //initialize the ans(make sure only the first time to initialize)
        if(ans.isEmpty()){
            Log.d("ATLAS","is empty");
            list();
            for(Question q: quizItems){
                ans.add(0);
            };
        }
    }
    public void set(int index,Integer value){
        ans.set(index,value);
    }

    public Integer get(int index){
        return ans.get(index);
    }

    public void clear(){
        ans.clear();
    }

    //for debug
    public void list(){
        Log.d("ATLAS",String.valueOf(ans.size()));
        Log.d("ATLAS",ans.toString());

    }
}
