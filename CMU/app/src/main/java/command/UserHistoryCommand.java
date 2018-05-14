package command;

import response.Response;

public class UserHistoryCommand implements Command {

    private static final long serialVersionUID = 2331723807741905L;
    private String username;

    public UserHistoryCommand(String username) {
        this.username = username;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
