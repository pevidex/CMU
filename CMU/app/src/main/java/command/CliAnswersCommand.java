package command;

import java.util.ArrayList;

import request.NormalRequest;
import request.Request;
import response.CliResponse;
import response.Response;
import response.ResponseData;

public class CliAnswersCommand implements CliCommand {

    private static final long serialVersionUID = -8907331723807741905L;

    private NormalRequest req;

    public CliAnswersCommand(NormalRequest r){
        req = r;
    }

    public NormalRequest getReq() {
        return req;
    }

    public void setReq(NormalRequest req) {
        this.req = req;
    }

    @Override
    public CliResponse handle(CommandClientHandler ch) {
        return ch.handle(this);
    }
}
