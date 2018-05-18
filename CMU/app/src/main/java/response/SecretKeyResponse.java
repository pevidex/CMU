package response;

import javax.crypto.SecretKey;


public class SecretKeyResponse implements ResponseData{
	private static final long serialVersionUID = 734423324276534155L;



	private String STATE_CODE = null;
	public SecretKeyResponse() {
		STATE_CODE = "ok";
	}

	public String getSTATE_CODE() {
		return STATE_CODE;
	}
}
