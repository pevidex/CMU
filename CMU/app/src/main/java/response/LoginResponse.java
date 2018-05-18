package response;

public class LoginResponse implements ResponseData {

    private static final long serialVersionUID = 734457624276534179L;
    private String message;
    private boolean success;
    private int sessionId;

    public LoginResponse(String message, boolean success, int id) {
        this.message = message;
        this.success = success;
        this.sessionId = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public boolean getSuccess(){
        return this.success;
    }
    public String getMessage() {
        return this.message;
    }
}

