package command;

import request.Request;
import response.Response;
import response.ResponseData;

public interface CommandHandler {
    public ResponseData handle(LoginCommand c);
    public ResponseData handle(GetLocationsCommand c);
    public ResponseData handle(GetQuestionsCommand c);
    public ResponseData handle(AnswersCommand c);
    public ResponseData handle(UserHistoryCommand c);
    public ResponseData handle(UserLocationHistoryCommand c);
	public ResponseData handle(RegisterCommand rc);
	
	public Response handle(Request request);
}