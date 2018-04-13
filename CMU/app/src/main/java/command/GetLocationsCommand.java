package command;

import response.Response;

public class GetLocationsCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;

    public GetLocationsCommand() {
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

}
