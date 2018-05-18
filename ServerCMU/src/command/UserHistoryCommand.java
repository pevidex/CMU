package command;

import response.Response;
import response.ResponseData;

public class UserHistoryCommand implements Commands {

    private static final long serialVersionUID = 2331723807741905L;
    private String username;

    public UserHistoryCommand(String username) {
        this.username = username;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
