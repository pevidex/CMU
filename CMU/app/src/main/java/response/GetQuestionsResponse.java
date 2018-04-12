package response;

import java.util.List;

import classes.Question;

public class GetQuestionsResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private List<Question> questions;

    public GetQuestionsResponse (List<Question> qs) {
        this.questions = qs;
    }

    public List<Question> getQuestions(){
    	return questions;
    }

    public void setQuestions(List<Question> qs){
    	this.questions = qs;
    }
}

