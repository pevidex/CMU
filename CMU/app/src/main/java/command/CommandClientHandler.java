package command;


import request.Request;
import response.CliResponse;
import response.ResponseData;
import response.Response;

public interface CommandClientHandler {
    public CliResponse handle(CliAnswersCommand c);

    public CliResponse handle(ShareCommand c);
}