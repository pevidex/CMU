package command;

import java.io.Serializable;

import response.Response;
import response.ResponseData;

public interface Command extends Serializable {
	Response handle(CommandHandler ch);
}
