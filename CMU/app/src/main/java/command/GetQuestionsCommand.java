package command;

import response.Response;
import response.ResponseData;

public class GetQuestionsCommand implements Commands {

    private static final long serialVersionUID = -8807331723807741905L;
    private String location;

    public GetQuestionsCommand(String location) {
        this.location = location;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
