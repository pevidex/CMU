package response;

import java.io.Serializable;

/**
 * Created by Joao on 18/05/2018.
 */

public class CliResponse implements Serializable {
    public Object getObj() {
        return obj;
    }

    Object obj;
    String STATE_CODE = null;

    public CliResponse(Object obj, String STATE_CODE) {
        this.obj = obj;
        this.STATE_CODE = STATE_CODE;
    }

    public String getSTATE_CODE() {
        return STATE_CODE;
    }



}
