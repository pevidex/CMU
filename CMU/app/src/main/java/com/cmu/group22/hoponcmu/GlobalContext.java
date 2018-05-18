package com.cmu.group22.hoponcmu;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classes.Answers;
import classes.Question;

public class GlobalContext extends Application {

    private int sessionId = 0;

    private String userName;
    private Answers ans = new Answers();
    private Map<String, Long> startTime= new HashMap<String, Long>();


    ArrayList<Question> quizz = new ArrayList<Question>();

    private Map<String, Map<String, Integer>> ranking = new HashMap<String, Map<String, Integer>>();
    private Map<String, Map<String, Long>> rankingTime = new HashMap<String, Map<String, Long>>();


    public int getSessionId(){
        return this.sessionId;
    }

    public void setSessionId(int id){
        this.sessionId = id;
    }

    public Answers getAnswers(){ return this.ans; }

    public ArrayList<Question> getQuizz() {
        return quizz;
    }

    public void setQuizz(ArrayList<Question> quizz) {
        this.quizz = quizz;
    }

    public void setAnswers(Answers a){
        ans = a;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, Map<String, Integer>> getRanking() {
        return ranking;
    }

    public Map<String, Map<String, Long>> getRankingTime() {
        return rankingTime;
    }

    public void setStartTime(String location, long time){
        if(!startTime.containsKey(location))
            startTime.put(location, time);
    }

    public long getStartTime(String location){
        return startTime.get(location);
    }

    public void setRanking(Map<String, Map<String, Integer>> ranking) {
        this.ranking = ranking;
    }

    //Ranking is a MAP that to each user name has (Location, Correct Answers)
    public void addToRanking(String user, String location, int correct, int total){
        Map<String, Integer> userRanking;
        if(ranking.containsKey(user)) {
            userRanking = ranking.get(userName);
            userRanking.put(location, correct);
        }
        else{
            userRanking = new HashMap<String, Integer>();
            userRanking.put(location, correct);
            ranking.put(user, userRanking);
        }
        Log.d("GLOBAL CONTEXT", "Updated Ranking");
    }

    //Ranking is a MAP that to each user name has (Location, Quizz time)
    public void addToTimeRanking(String user, String location, long time){
        Map<String, Long> userRanking;
        if(rankingTime.containsKey(user)) {
            userRanking = rankingTime.get(userName);
            userRanking.put(location, time);
        }
        else{
            userRanking = new HashMap<String, Long>();
            userRanking.put(location, time);
            rankingTime.put(user, userRanking);
        }
        Log.d("GLOBAL CONTEXT", "Updated Time Ranking");
    }
}
