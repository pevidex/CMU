package response;

import java.util.ArrayList;

import classes.Question;

public class GetQuestionsResponse implements ResponseData {

    private static final long serialVersionUID = 734457624276534179L;
    private ArrayList<Question> questions;
    private boolean success;

    public GetQuestionsResponse (ArrayList<Question> qs, boolean success) {
        this.questions = qs;
        this.success= success;
    }

    public ArrayList<Question> getQuestions(){
        return questions;
    }

    public void setQuestions(ArrayList<Question> qs){
        this.questions = qs;
    }
    public boolean getSuccess() {return this.success;}
}

