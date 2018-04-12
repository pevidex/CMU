package response;

import java.util.ArrayList;

import classes.Location;

public class GetLocationsResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private ArrayList<Location> locations=null;

    public GetLocationsResponse(ArrayList<Location> locations) {
    	this.locations=locations;
    }
    public ArrayList<Location> getLocations(){
    	return this.locations;
    }
}



