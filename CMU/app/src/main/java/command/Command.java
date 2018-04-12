package command;

import java.io.Serializable;

import response.Response;

public interface Command extends Serializable {
	Response handle(CommandHandler ch);
}
