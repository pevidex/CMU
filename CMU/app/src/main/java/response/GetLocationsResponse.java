package response;

import java.util.ArrayList;

import classes.Location;


public class GetLocationsResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private ArrayList<Location> locations=null;
    private boolean success;

    public GetLocationsResponse(ArrayList<Location> locations, boolean success) {
        this.success=success;
        this.locations=locations;
    }
    public ArrayList<Location> getLocations(){
        return this.locations;
    }
    public boolean getSuccess() {return this.success;}
}



