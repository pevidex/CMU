package response;

import command.GetQuestionsCommand;

public interface ResponseHandler {
    public void handle(LoginResponse lr);
    public void handle(RegisterResponse rr);
    public void handle(GetLocationsResponse lr);
    public void handle(GetQuestionsResponse qr);
    public void handle(AnswersResponse ar);
}
