package server;

import command.CommandHandler;
import command.GetLocationsCommand;
import command.GetQuestionsCommand;
import command.LoginCommand;
import command.RegisterCommand;
import response.GetLocationsResponse;
import response.GetQuestionsResponse;
import response.LoginResponse;
import response.RegisterResponse;
import response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import classes.User;
import classes.Location;
import classes.Question;

public class CommandHandlerImpl implements CommandHandler {

	ArrayList<User> users;
	ArrayList<String> available_codes;//codes generated for the users tickets
	ArrayList<Location> locations;
	HashMap<String, List<Question>> globalQuestions; 
	
	CommandHandlerImpl(){
		users=new ArrayList<User>();
		available_codes = new ArrayList<String>();
		locations = new ArrayList<Location>();
		addLocations();
		addCodes();

		globalQuestions = new HashMap<String, List<Question>>();
		setGlobalQuestions();
	}
	
	@Override
	public Response handle(LoginCommand lc) {
		System.out.println("Received: LogIn Command");
		User u = findUser(lc.getUsername());
		if(u==null)
			return new LoginResponse("Login: invalid Username!",false, -1);
		if(u.getCode().equals(lc.getCode())){
			Random randomGenerator = new Random();
			int sessionId = randomGenerator.nextInt(1000);
			System.out.println("Login: User "+lc.getUsername()+" logged in!\n");
			return new LoginResponse("Login: Success!",true, sessionId);
		}
		return new LoginResponse("Login: invalid code",false, -1);
	}
	@Override
	public Response handle(RegisterCommand rc) {
		System.out.println("Received: Register Command");
		User u = findUser(rc.getUsername());
		String c = findCode(rc.getCode());
		if(u!=null)
			return new RegisterResponse("Register: Username already exists!",false);
		if(c==null)
			return new RegisterResponse("Register: Invalid Code!",false);
		User newU = new User(rc.getUsername(),c);
		users.add(newU);
		available_codes.remove(c); //can remove like this?
		System.out.println("Register: User "+rc.getUsername()+" registered!\n");
		return new RegisterResponse("Register: Registered with success!",true);
		
	}

	@Override
	public Response handle(GetQuestionsCommand qc){
		System.out.println("Received: Questions Command");
		String location = qc.getLocation();

		List<Question> questions = globalQuestions.get(location);	//Get questions for that location

		return new GetQuestionsResponse(questionsToClient(questions));
	}

	public User findUser(String username){
		for(User u: users){
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}
	
	public String findCode(String code){
		for(String c: available_codes){
			if(c.equals(code))
				return c;
		}
		return null;
	}
	public void addLocations(){
		locations.add(new Location("belem tower","res/1.jpg"));
		locations.add(new Location("cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"));
		locations.add(new Location("Rossio Square","https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Pra%C3%A7a_de_D._Pedro_IV.jpg/360px-Pra%C3%A7a_de_D._Pedro_IV.jpg"));
		locations.add(new Location("25 de Abril Bridge","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Ponte_25_de_Abril_Lisboa.jpg/330px-Ponte_25_de_Abril_Lisboa.jpg"));
		locations.add(new Location("belem tower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"));
		locations.add(new Location("cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"));
		locations.add(new Location("Rossio Square","https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Pra%C3%A7a_de_D._Pedro_IV.jpg/360px-Pra%C3%A7a_de_D._Pedro_IV.jpg"));
		locations.add(new Location("25 de Abril Bridge","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Ponte_25_de_Abril_Lisboa.jpg/330px-Ponte_25_de_Abril_Lisboa.jpg"));
		locations.add(new Location("belem tower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"));
		locations.add(new Location("cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"));
	}
	public void addCodes(){
		String[] codesList = new String[] {"code0", "code1", "code2", "code3"};
		available_codes.addAll(Arrays.asList(codesList));
	}

	@Override
	public Response handle(GetLocationsCommand c) {
		System.out.println(locations);
		return new GetLocationsResponse(locations);
	}

	//Set HardCoded Questions
	public void setGlobalQuestions(){
		Question q1 = new Question();
		q1.setQuestion("What is Ricardo's last name?");
		q1.setAnswer1("Trindade");
		q1.setAnswer2("Kovalchuk");
		q1.setAnswer3("Quintino");
		q1.setAnswer4("Xavier");
		q1.setCorrectAnswer("Xavier");

		List<Question> questions = new ArrayList<Question>();
		questions.add(q1);
		questions.add(q1);
		questions.add(q1);

		globalQuestions.put("belem tower", questions);
	}

	//Translate serverQuestions to clientQuestions (without correctAnswer)
	public List<Question> questionsToClient(List<Question> questions){
		List<Question> clientQuestions = new ArrayList<Question>();

		for (Question q : questions) {
			Question question = new Question(q.getQuestion(), q.getAnswer1(), q.getAnswer2(), q.getAnswer3(), q.getAnswer4());
			clientQuestions.add(question); 
		}

		return clientQuestions;
	}
}
