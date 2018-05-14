package command;

import java.util.ArrayList;
import java.util.List;

import response.Response;

public class AnswersCommand implements Command {

    private static final long serialVersionUID = -8907331723807741905L;

    private String location; //To know which questions are these answers for.
    private ArrayList<Integer> answers;
    private String userName;

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public AnswersCommand() {
        answers = new ArrayList<Integer>();
    }
	public AnswersCommand(String location, ArrayList<Integer> answers, String userName)
	{
		this.location = location;
		this.answers = answers;
		this.userName = userName;
	}
    public AnswersCommand(ArrayList<Integer> al) {
        answers = al;
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
