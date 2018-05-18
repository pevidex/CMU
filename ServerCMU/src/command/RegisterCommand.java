package command;

import java.security.PublicKey;

import response.Response;
import response.ResponseData;

public class RegisterCommand implements Commands{

    private static final long serialVersionUID = -8807331723807741905L;
    private String username;
    private String code;
    private byte[] pubbkey;

    public RegisterCommand(String code, String username, byte[] pbkBytes) {
        this.code = code;
        this.username = username;
        this.pubbkey = pbkBytes;
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
    
    public byte[] getPubkey() {
    	return pubbkey;
    }

}
