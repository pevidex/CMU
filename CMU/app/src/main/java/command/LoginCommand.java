package command;

import response.Response;
import response.ResponseData;

public class LoginCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private String username;
    private String code;

    public LoginCommand(String code, String username) {
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
