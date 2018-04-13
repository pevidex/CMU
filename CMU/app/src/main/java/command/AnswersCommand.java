package command;

import java.util.ArrayList;
import java.util.List;

import response.Response;

public class AnswersCommand implements Command {

    private static final long serialVersionUID = -8907331723807741905L;

    private String location; //To know which questions are these answers for.
    private ArrayList<Integer> answers;

    public AnswersCommand() {
        answers = new ArrayList<Integer>();
    }

    public AnswersCommand(ArrayList<Integer> al, String l) {
        answers = al;
        location = l;
    }

    public ArrayList<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Integer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Integer s){
        answers.add(s);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

}
