package classes;

import java.util.ArrayList;
import java.util.List;

public class Question implements QuestionInterface{


	private static final long serialVersionUID = 121L;
	String question;
	List<String> answers = new ArrayList<>();
	int correctAnswer;//the index of options
	final private int NO_SET = 0;


	public Question(String q, String a1, String a2, String a3, String a4, int ca){
		this.question = q;
		answers.add(a1);
		answers.add(a2);
		answers.add(a3);
		answers.add(a4);
		this.correctAnswer = ca;
	}

	//Constructor without correct answer
	public Question(String q, String a1, String a2, String a3, String a4){
		this.question = q;
		answers.add(a1);
		answers.add(a2);
		answers.add(a3);
		answers.add(a4);
		this.correctAnswer = NO_SET;
	}

	public Question(){

	}

	public String getQuestion(){
		return this.question;
	}

	public void setQuestion(String q){
		this.question = q;
	}

	public String getAnswer1(){
		return this.answers.get(0);
	}

	public void setAnswer1(String a1){
		this.answers.add(0,a1);
	}

	public String getAnswer2(){
		return this.answers.get(1);
	}

	public void setAnswer2(String a2){
		this.answers.add(1,a2);
	}

	public String getAnswer3(){
		return this.answers.get(2);
	}

	public void setAnswer3(String a3){
		this.answers.add(2,a3);
	}

	public String getAnswer4(){
		return this.answers.get(3);
	}

	public void setAnswer4(String a4){
		this.answers.add(3, a4);
	}

	public String getAnsweri(int i){
		return this.answers.get(i);
	}

	public int getCorrectAnswer(){
		return this.correctAnswer;
	}

	public void setCorrectAnswer(int ca){
		this.correctAnswer = ca;
	}
}
