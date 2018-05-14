package command;

import response.Response;

public class GetQuestionsCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private String location;

    public GetQuestionsCommand(String location) {
        this.location = location;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
