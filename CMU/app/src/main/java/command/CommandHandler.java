package command;


import request.Request;
import response.ResponseData;
import response.Response;

public interface CommandHandler {
    public Response handle(LoginCommand c);
    public Response handle(RegisterCommand c);
    public Response handle(GetLocationsCommand c);
    public Response handle(GetQuestionsCommand c);
    public Response handle(AnswersCommand c);
    public Response handle(UserHistoryCommand c);
    public Response handle(UserLocationHistoryCommand c);
    public Response handle(Request request);

    public Response handle(ShareCommand c);
}