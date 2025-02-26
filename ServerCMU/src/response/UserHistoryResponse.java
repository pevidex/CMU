package response;

import java.util.ArrayList;

import classes.Location;

public class UserHistoryResponse implements Response {

    private static final long serialVersionUID = 734423324276534155L;
    private ArrayList<Location> locations;

    public UserHistoryResponse(ArrayList<Location> locations, boolean success) {
        this.locations = locations;
    }

    public ArrayList<Location> getMessage() {
        return this.locations;
    }
}

