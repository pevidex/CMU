package classes;

import java.io.Serializable;

public class QuizItem implements Serializable{
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public QuizItem(String question, String option1, String option2, String option3,String option4){
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }
}
