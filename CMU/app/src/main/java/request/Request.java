package request;

import command.Command;
import command.CommandHandler;
import response.Response;

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

	//public String getNonce(){return nonce;}


    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

}
