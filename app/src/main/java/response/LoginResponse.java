package response;

public class LoginResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private String message;
    private boolean success;

    public LoginResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public String getMessage() {
        return this.message;
    }
}

