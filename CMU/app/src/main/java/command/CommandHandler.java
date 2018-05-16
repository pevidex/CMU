package command;

import response.Response;

public interface CommandHandler {

    public Response handle(AnswersCommand lc);
}