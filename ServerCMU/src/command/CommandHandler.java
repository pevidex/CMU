package command;

import response.Response;

public interface CommandHandler {
    public Response handle(LoginCommand c);
    public Response handle(RegisterCommand c);
    public Response handle(GetLocationsCommand c);
    public Response handle(GetQuestionsCommand c);
    public Response handle(AnswersCommand c);
    public Response handle(UserHistoryCommand c);
}