package command;

import response.CliResponse;
import response.Response;
import response.ResponseData;

/**
 * Created by Joao on 18/05/2018.
 */

public interface CliCommand {
    CliResponse handle(CommandClientHandler ch);
}
