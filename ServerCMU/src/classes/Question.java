package classes;

public class Question implements QuestionInterface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 121L;
	String question; 
	String answer1, answer2, answer3, answer4; 
	int correctAnswer; 

	
	public Question(String q, String a1, String a2, String a3, String a4, int ca){
		this.question = q;
		this.answer1 = a1;
		this.answer2 = a2; 
		this.answer3 = a3;
		this.answer4 = a4;
		this.correctAnswer = ca;	
	}

	//Constructor without correct answer
	public Question(String q, String a1, String a2, String a3, String a4){
		this.question = q;
		this.answer1 = a1;
		this.answer2 = a2; 
		this.answer3 = a3;
		this.answer4 = a4;
		this.correctAnswer = 0;
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
		return this.answer1;
	}

	public void setAnswer1(String a1){
		this.answer1 = a1;
	}

	public String getAnswer2(){
		return this.answer2;
	}

	public void setAnswer2(String a2){
		this.answer2 = a2;
	}

	public String getAnswer3(){
		return this.answer3;
	}

	public void setAnswer3(String a3){
		this.answer3 = a3;
	}

	public String getAnswer4(){
		return this.answer4;
	}

	public void setAnswer4(String a4){
		this.answer4 = a4;
	}
	
	public int getCorrectAnswer(){
		return this.correctAnswer;
	}
	
	public void setCorrectAnswer(int ca){
		this.correctAnswer = ca;
	}
}
