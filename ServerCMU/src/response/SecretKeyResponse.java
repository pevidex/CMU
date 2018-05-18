package response;

import javax.crypto.SecretKey;

public class SecretKeyResponse implements ResponseData{
	private static final long serialVersionUID = 734423324276534155L;
	private String STATE_CODE;
	public SecretKeyResponse() {
		STATE_CODE = "ok";
	}
}
