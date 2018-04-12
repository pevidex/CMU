package command;

import response.Response;

public interface CommandHandler {
    public Response handle(LoginCommand c);
    public Response handle(RegisterCommand c);
}