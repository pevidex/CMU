package response;

import java.util.ArrayList;

public class UserHistoryResponse implements Response {

    private static final long serialVersionUID = 734423324276534155L;
    private ArrayList<String> locations;

    public UserHistoryResponse(ArrayList<String> locations, boolean success) {
        this.locations = locations;
    }

    public ArrayList<String> getMessage() {
        return this.locations;
    }
}


