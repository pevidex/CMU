package response;

public class UserHistoryResponse implements Response {

    private static final long serialVersionUID = 734423324276534179L;
    private String message;
    private boolean success;

    public UserHistoryResponse(String message, boolean success) {
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

