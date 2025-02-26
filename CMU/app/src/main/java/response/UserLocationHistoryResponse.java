package response;

import java.util.ArrayList;

import classes.Question;

public class UserLocationHistoryResponse implements Response {

    private static final long serialVersionUID = 734423234276534155L;
    private ArrayList<Question> questions;
    private ArrayList<Integer> userAnswers;
    private ArrayList<Boolean> answersResult;

    public ArrayList<Integer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(ArrayList<Integer> userAnswers) {
        this.userAnswers = userAnswers;
    }
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Boolean> getAnswersResult() {
        return answersResult;
    }

    public void setAnswersResult(ArrayList<Boolean> answersResult) {
        this.answersResult = answersResult;
    }

    public UserLocationHistoryResponse(ArrayList<Question> questions, ArrayList<Boolean> answersResult, ArrayList<Integer> userAnswers) {
        this.questions = questions;
        this.answersResult=answersResult;
        this.userAnswers=userAnswers;
    }

}

