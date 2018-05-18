package request;

import command.Command;
import command.CommandHandler;
import response.Response;
import response.ResponseData;
import security.EncryptedObject;
import security.SignedObject;

public class Request implements Command{
    private static final long serialVersionUID = -8807331723807741905L;
    //private String nonce = GenerateNonce.randomString(30);
    byte[] pubkeyBytes;

    public Request(byte[] requesterKeyBytes){
        this.pubkeyBytes = requesterKeyBytes;

    }

    public byte[] getPubkeyBytes() {
        return pubkeyBytes;
    }

	@Override
	public Response handle(CommandHandler chi) {
		 return chi.handle(this);
	}

	//public String getNonce(){return nonce;}


	   

}
