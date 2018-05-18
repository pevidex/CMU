package server;

import command.AgreeSecretKeyCommand;
import command.AnswersCommand;
import command.CommandHandler;
import command.GetLocationsCommand;
import command.GetQuestionsCommand;
import command.LoginCommand;
import command.RegisterCommand;
import command.UserHistoryCommand;
import command.UserLocationHistoryCommand;
import request.Request;
import request.KeyAgreeRequest;
import request.NormalRequest;
import response.AnswersResponse;
import response.GetLocationsResponse;
import response.GetQuestionsResponse;
import response.LoginResponse;
import response.RegisterResponse;
import response.Response;
import response.ResponseData;
import response.SecretKeyResponse;
import response.UserHistoryResponse;
import response.UserLocationHistoryResponse;
import security.CryptoManager;
import security.EncryptedObject;
import security.GenerateKeys;
import security.SignKeypair;
import security.SignedObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import classes.User;
import classes.AnsweredQuizz;
import classes.Location;
import classes.Question;
import classes.Quizz;

public class CommandHandlerImpl implements CommandHandler {

	ArrayList<User> users;
	ArrayList<String> available_codes;//codes generated for the users tickets
	ArrayList<Location> locations;
	HashMap<String, ArrayList<Question>> globalQuestions; 
	ArrayList<Quizz> quizzes;
	ArrayList<AnsweredQuizz> answeredQuizzes;
	PrivateKey serverPrk;//use for crypto
	PrivateKey signPrk;//use for sign
	HashMap<PublicKey,SecretKeySpec> userSessionKeyMap; 
	
	CommandHandlerImpl(){
		userSessionKeyMap = new HashMap<>();
		users=new ArrayList<User>();
		available_codes = new ArrayList<String>();
		locations = new ArrayList<Location>();
		addLocations();
		addCodes();
		answeredQuizzes=new ArrayList<AnsweredQuizz>();
		quizzes = new ArrayList<Quizz>();
		globalQuestions = new HashMap<String, ArrayList<Question>>();
		
		setGlobalQuestions();
		
		GenerateKeys gk;
		try {
			gk = new GenerateKeys(1024);
			serverPrk =  gk.getPrivate("KeyPair/privateKey");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			SignKeypair sgk = new SignKeypair();
			//signPrk = null;
			signPrk = sgk.getPrivate("KeyPair/signPrk");
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public Response handle(Request request) {
		System.out.println("receive request");
		SecretKeySpec secretKey = null;
		ResponseData responseData = null;
		Response response = null;
		PublicKey userPbk = null; 
		byte[]  userPbkBytes = null;
		
		
		try {
			
			userPbkBytes = request.getPubkeyBytes();
			
			userPbk = CryptoManager.getPbkFromBytes(userPbkBytes,"DSA");
			
			if(request instanceof KeyAgreeRequest) {
				request = (KeyAgreeRequest) request;
				SignedObject o = ((KeyAgreeRequest) request).getSignedObject();
				if(o.verify(userPbkBytes)) {
					Object obj = o.getObject();
					if(obj instanceof AgreeSecretKeyCommand) {
						AgreeSecretKeyCommand akeyc = (AgreeSecretKeyCommand) obj;
						byte[] encryptedSK = akeyc.getEncryptedSK();
						secretKey = CryptoManager.revertSK(encryptedSK, serverPrk);
						userSessionKeyMap.put(userPbk, secretKey);
						
						responseData = new SecretKeyResponse();
					}else {
						System.out.println("not agree secret command");
						return null;
					}
				}else {
					System.out.println("verify fails - keyagree request");
					return null;
				}
			}else if(request instanceof NormalRequest) {
				request = (NormalRequest) request;
				EncryptedObject o = ((NormalRequest) request).getEncryptedObject();
				SecretKeySpec sessionKey = userSessionKeyMap.get(userPbk);
				SignedObject signedO = o.decrypt(sessionKey);
				if(signedO.verify(userPbkBytes)) {
					if(signedO.getObject() instanceof RegisterCommand) {
						System.out.println("register command");
						responseData = handle((RegisterCommand)signedO.getObject());												
					}else if(signedO.getObject() instanceof AnswersCommand) {
						responseData = handle((AnswersCommand) signedO.getObject());
					}else if(signedO.getObject() instanceof GetLocationsCommand) {
						responseData = handle((GetLocationsCommand) signedO.getObject());
					}else if(signedO.getObject() instanceof LoginCommand) {
						responseData = handle((LoginCommand) signedO.getObject());
					}else if(signedO.getObject() instanceof UserLocationHistoryCommand) {
						responseData = handle((UserLocationHistoryCommand)signedO.getObject());
					}else if(signedO.getObject() instanceof UserHistoryCommand) {
						responseData = handle((UserHistoryCommand) signedO.getObject());
					}else if(signedO.getObject() instanceof GetQuestionsCommand) {
						responseData = handle((GetQuestionsCommand) signedO.getObject());
					}
				}else {  
					System.out.println("verify fails - normal request");
					return null;
				}
			}
			
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | ClassNotFoundException | IOException | SignatureException | InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//return response
		SecretKeySpec sessionKey = userSessionKeyMap.get(userPbk);
		try {
			SignedObject responseSigned = new SignedObject(responseData, signPrk);
			EncryptedObject responseEncrypted = new EncryptedObject(responseSigned, sessionKey);
			response = new Response(responseEncrypted);
		}
		catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException
					| IllegalBlockSizeException|IOException | InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		
		} 

		return response;
	}
	
	@Override
	public ResponseData handle(LoginCommand lc) {
		ResponseData responseData;
		SignedObject responseSigned;
		System.out.println("Received: LogIn Command");
		User u = findUser(lc.getUsername());
		if(u==null)
		
			return new LoginResponse("Login: invalid Username!",false, -1);
		if(u.getCode().equals(lc.getCode())){
			Random randomGenerator = new Random();
			int sessionId = randomGenerator.nextInt(1000);
			//TODO save sessionId on the user class
			System.out.println("Login: User "+lc.getUsername()+" logged in!\n");
			return new LoginResponse("Login: Success!",true, sessionId);
		}
		
		return new LoginResponse("Login: invalid code",false, -1);
	}
	
	@Override
	public ResponseData handle(RegisterCommand rc) {
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
	public AnsweredQuizz findAnsweredQuizz(String location){
		for(AnsweredQuizz q: answeredQuizzes){
			if(q.getLocation().equals(location))
				return q;
		}
		return null;
	}
	
	public Quizz findQuizz(String location){
		for(Quizz q: quizzes){
			if(q.getLocation().equals(location))
				return q;
		}
		return null;
	}
	
	@Override
	public ResponseData handle(AnswersCommand ac){
		System.out.println("Received: Answers Command");
		
		String location = ac.getLocation();
		AnsweredQuizz answeredQuizz = findAnsweredQuizz(location);
		if(answeredQuizz ==null){
			answeredQuizz=new AnsweredQuizz(location);
			answeredQuizzes.add(answeredQuizz);
		}
		ArrayList<Question> questions = globalQuestions.get(location);
		ArrayList<Boolean> answersResult = new ArrayList<Boolean>();
		ArrayList<Integer> answers = ac.getAnswers();

		answeredQuizz.addUserAnswers(ac.getUserName(), answers);
		
		User u = findUser(ac.getUserName());
		u.addAnsweredQuizz(findQuizz(location));
		System.out.println("Added location=" + location + " to user=" + ac.getUserName());
		System.out.println("Questions size: " + questions.size());
		System.out.println("Ans size: " + answers.size());
		
		for(int i = 0; i < answers.size(); i++){
			System.out.println(i);
			int correctAnswer = questions.get(i).getCorrectAnswer();
			Boolean result = correctAnswer == answers.get(i);
			System.out.println("Answer="+ answers.get(i) + " Result="+ correctAnswer);

			answersResult.add(result);
		}

		answeredQuizz.addUserResult(ac.getUserName(), answersResult);
		return new AnswersResponse(answersResult);
	}

	@Override
	public ResponseData handle(GetQuestionsCommand qc){
		System.out.println("Received: Questions Command");
		String location = qc.getLocation();

		ArrayList<Question> questions = globalQuestions.get(location);	//Get questions for that location
		if(questions==null || questions.size()<1)			
			return new GetQuestionsResponse(null,false);
		return new GetQuestionsResponse(questionsToClient(questions),true);
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
		locations.add(new Location("BelemTower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"));
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

	public ResponseData handle(GetLocationsCommand c) {
		return new GetLocationsResponse(locations,true);
	}
	
	public ResponseData handle(UserHistoryCommand c) {
		System.out.println("Received History Command");
		User u = findUser(c.getUsername());
		if(u==null)
			return new UserHistoryResponse(null,false);
		ArrayList<Location> userLocations=new ArrayList<Location>();
		for(String s: u.getAnsweredLocations()){
			userLocations.add(findLocation(s));
			System.out.println("UserHistory.Sending " + s);
		}
		return new UserHistoryResponse(userLocations,true);
	}
	
	public Location findLocation(String name){
		for(Location l: locations){
			if(l.getName().equals(name))
				return l;
		}
		return null;
	}
	//Set HardCoded Questions
	public void setGlobalQuestions(){
		Question q1 = new Question();
		q1.setQuestion("What is Ricardo's last name?1");
		q1.setAnswer1("Trindade1");
		q1.setAnswer2("Kovalchuk1");
		q1.setAnswer3("Quintino1");
		q1.setAnswer4("Xavier1");
		q1.setCorrectAnswer(4);

		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(q1);
		
		Question q2 = new Question();
		q2.setQuestion("2What is Ricardo's last name?2");
		q2.setAnswer1("Trindade2");
		q2.setAnswer2("Kovalchuk2");
		q2.setAnswer3("Quintino2");
		q2.setAnswer4("Xavier2");
		q2.setCorrectAnswer(4);
		
		questions.add(q2);
		
		Question q3 = new Question();
		q3.setQuestion("3What is Ricardo's last name?3");
		q3.setAnswer1("Trindade3");
		q3.setAnswer2("Kovalchuk3");
		q3.setAnswer3("Quintino3");
		q3.setAnswer4("Xavier3");
		q3.setCorrectAnswer(4);
		
		questions.add(q3);
		System.out.println(questions.get(0).getQuestion()+questions.get(1).getQuestion());
		
		quizzes.add(new Quizz("BelemTower",questions));
		globalQuestions.put("BelemTower", questions);
	}

	//Translate serverQuestions to clientQuestions (without correctAnswer)
	public ArrayList<Question> questionsToClient(ArrayList<Question> questions){
		ArrayList<Question> clientQuestions = new ArrayList<Question>();

		for (Question q : questions) {
			Question question = new Question(q.getQuestion(), q.getAnswer1(), q.getAnswer2(), q.getAnswer3(), q.getAnswer4());
			clientQuestions.add(question); 
		}

		return clientQuestions;
	}
	@Override
	public ResponseData handle(UserLocationHistoryCommand c) {
		AnsweredQuizz answeredQuizz = findAnsweredQuizz(c.getLocation());
		Quizz quizz = findQuizz(c.getLocation());
		if(answeredQuizz==null || quizz ==null)
			return null;
		
		return new UserLocationHistoryResponse(quizz.getQuestions(),answeredQuizz.getUserResult(c.getUsername()),answeredQuizz.getUserAnswers(c.getUsername()));
	}
}
