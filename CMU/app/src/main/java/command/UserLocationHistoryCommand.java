package command;

import response.Response;
import response.ResponseData;

public class UserLocationHistoryCommand implements Command {

    private static final long serialVersionUID = 2331723807741905L;
    private String username;
    private String location;

    public UserLocationHistoryCommand(String username, String location) {
        this.username = username;
        this.location=location;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
