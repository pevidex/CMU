package response;

import java.util.ArrayList;
import java.util.List;


public class AnswersResponse implements Response {

    private static final long serialVersionUID = 735457624276534179L;

    //Should we return the correct answer or just the result?
    private ArrayList<Boolean> answersResult;

    public AnswersResponse(ArrayList<Boolean> ar) {
        answersResult = ar;
    }

    public ArrayList<Boolean> getAnswersResult() {
        return answersResult;
    }

    public void setAnswersResult(ArrayList<Boolean> answersResult) {
        this.answersResult = answersResult;
    }
}



